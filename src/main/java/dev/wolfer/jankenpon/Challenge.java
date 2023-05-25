// Package Name

package dev.wolfer.jankenpon;

// External Libraries

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// Challenge Class

public class Challenge {
  private User userX;
  private User userY;
  private Move moveX;
  private Move moveY;
  private String result;

  public Challenge(User userX, User userY, Move moveX, Move moveY) {
    this.userX = userX;
    this.userY = userY;
    this.moveX = moveX;
    this.moveY = moveY;
    execute();
  }

  private void execute() {
    Integer x = moveX.getChoice();
    Integer y = moveY.getChoice();
    if (x == y) {
      userX.addTie();
      userY.addTie();
    } else if ((x == 0 && y == 2) || (x == 1 && y == 0) || (x == 2 && y == 1)) {
      userX.addWin();
      userY.addDefeat();
    } else {
      userX.addDefeat();
      userY.addWin();
    }
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultArray = mapper.createArrayNode();
    ((ObjectNode) resultArray.add(mapper.valueToTree(userX)).get(0)).put("choice", x);
    ((ObjectNode) resultArray.add(mapper.valueToTree(userY)).get(1)).put("choice", y);
    result = resultArray.toString();
  }

  public String getResult() {
    return result;
  }
}
