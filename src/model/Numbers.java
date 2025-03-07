package model;

/**
 * Represents the different numbers a card can have.
 */
public enum Numbers {
  One,
  Two,
  Three,
  Four,
  Five,
  Six,
  Seven,
  Eight,
  Nine,
  A; // A represents the number 10

  /**
   * Returns a String representation of a number.
   * @return "1" for One, "2" for Two, ..., "9" for Nine, and "A" for value of 10.
   */
  public String toString() {
    switch (this) {
      case One:
        return "1";
      case Two:
        return "2";
      case Three:
        return "3";
      case Four:
        return "4";
      case Five:
        return "5";
      case Six:
        return "6";
      case Seven:
        return "7";
      case Eight:
        return "8";
      case Nine:
        return "9";
      default:
        return "A";
    }
  }

  /**
   * Returns a number representation of a Number.
   * @return an int representing a number.
   */
  public int toNum() {
    if (this.toString().equals("A")) {
      return 10;
    }
    else {
      return Integer.parseInt(this.toString());
    }
  }
}
