// Package Name

package dev.wolfer.jankenpon;

// Java Libraries

import java.util.Date;

// External Libraries

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

// User Class

public class User {
  @JsonProperty(access = Access.READ_ONLY)
  private Long id;
  private String name;
  @JsonProperty(access = Access.READ_ONLY)
  private Integer wins;
  @JsonProperty(access = Access.READ_ONLY)
  private Integer ties;
  @JsonProperty(access = Access.READ_ONLY)
  private Integer defeats;

  public User(@JsonProperty("name") String name) {
    this.id = new Date().getTime();
    this.name = name;
    this.wins = 0;
    this.ties = 0;
    this.defeats = 0;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getWins() {
    return wins;
  }

  public Integer getTies() {
    return ties;
  }

  public Integer getDefeats() {
    return defeats;
  }

  public void addWin() {
    wins++;
  }

  public void addTie() {
    ties++;
  }

  public void addDefeat() {
    defeats++;
  }
}
