// Package Name

package dev.wolfer.jankenpon;

// External Libraries

import com.fasterxml.jackson.annotation.JsonProperty;

// Move Class

public class Move {
  private Integer choice;

  public Move(@JsonProperty("choice") Integer choice) {
    this.choice = choice % 3;
  }

  public Integer getChoice() {
    return choice;
  }
}
