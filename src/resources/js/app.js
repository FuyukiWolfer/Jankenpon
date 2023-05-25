const dataItem = {}

let matchType = null

updateDataItem()

document.addEventListener('DOMContentLoaded', (event) => {
  getUserId() ? dataItem.menuPage.removeAttribute('hidden') : dataItem.authPage.removeAttribute('hidden')
})

dataItem.startButton.addEventListener('click', async (event) => {
  try {
    const username = dataItem.usernameInput.value
    if (getUserId()) return alert('Already logged.')
    if (username.length < 3 || username.length > 20) return alert('The name must be between 3 to 20 characters long.')
    const request = await fetch('/api/users', { headers: { 'Content-Type': 'application/json; charset=utf-8' }, body: JSON.stringify({ name: username }), method: 'POST' })
    if (request.status === 201) {
      dataItem.authPage.setAttribute('hidden', '')
      dataItem.menuPage.removeAttribute('hidden')
    } else {
      alert('Unable to complete the operation, please try again!')
    }
  } catch (err) {
    alert('An unexpected error has occurred.')
    console.error(err)
  }
})

dataItem.vsComputerButton.addEventListener('click', async (event) => {
  try {
    if (!getUserId) return window.location.reload()
    if (!await updateComputerMatch()) return
    dataItem.menuPage.setAttribute('hidden', '')
    dataItem.matchPage.removeAttribute('hidden')
    matchType = 'computer'
  } catch (err) {
    alert('An unexpected error has occurred.')
    console.error(err)
  }
})

dataItem.rockMove.addEventListener('click', (event) => {
  if (matchType === 'computer') computerMatch(0)
})

dataItem.paperMove.addEventListener('click', (event) => {
  if (matchType === 'computer') computerMatch(1)
})

dataItem.scissorMove.addEventListener('click', (event) => {
  if (matchType === 'computer') computerMatch(2)
})

dataItem.leave.addEventListener('click', (event) => {
  matchType = null
  dataItem.matchPage.setAttribute('hidden', '')
  dataItem.menuPage.removeAttribute('hidden')
})

async function computerMatch (choice) {
  try {
    const request = await fetch('/api/challenge', { headers: { 'Content-Type': 'application/json; charset=utf-8' }, body: JSON.stringify({ choice }), method: 'POST' })
    if (request.status === 200) {
      const result = await request.json()
      let me = null
      let vs = null
      if (result[0].id === getUserId()) {
        me = result[0]
        vs = result[1]
      } else {
        me = result[1]
        vs = result[0]
      }
      if (me.choice === vs.choice) {
        alert('Tie!')
      } else if ((me.choice === 0 && vs.choice === 2) || (me.choice === 1 && vs.choice === 0) || (me.choice === 2 && vs.choice === 1)) {
        alert(`${me.name} won!`)
      } else {
        alert(`${vs.name} won!`)
      }
      await updateComputerMatch()
    } else {
      alert('Unable to complete the operation, please try again!')
    }
  } catch (err) {
    alert('An unexpected error has occurred.')
    console.error(err)
  }
}

async function updateComputerMatch () {
  const computerRequest = await fetch('/api/computer')
  if (computerRequest.status !== 200) return alert('Unable to complete the operation, please try again!')
  const userRequest = await fetch('/api/users/' + getUserId())
  if (userRequest.status !== 200) return alert('Unable to complete the operation, please try again!')
  updateMatch(await userRequest.json(), await computerRequest.json())
  return true
}

function updateMatch (me, vs) {
  dataItem.meName.innerHTML = me.name
  dataItem.meMatches.innerHTML = me.wins + me.ties + me.defeats
  dataItem.meWins.innerHTML = me.wins
  dataItem.meTies.innerHTML = me.ties
  dataItem.meDefeats.innerHTML = me.defeats
  dataItem.vsName.innerHTML = vs.name
  dataItem.vsMatches.innerHTML = vs.wins + vs.ties + vs.defeats
  dataItem.vsWins.innerHTML = vs.wins
  dataItem.vsTies.innerHTML = vs.ties
  dataItem.vsDefeats.innerHTML = vs.defeats
}

function updateDataItem () {
  const elements = document.querySelectorAll('[data-item]')
  for (const key of Object.keys(dataItem)) { delete dataItem[key] }
  for (const element of elements) { dataItem[element.getAttribute('data-item')] = element }
}

function getUserId () {
  const userId = document.cookie.split('; ').find((row) => row.startsWith('user='))?.split('=')[1] || null
  if (!userId) return userId
  fetch('/api/users/' + userId).then((result) => {
    if (userId && result.status !== 200) {
      document.cookie = 'user=0; max-age=0'
      window.location.reload()
    }
  })
  return userId
}
