package model;

import java.awt.Color;

/**
 * Represents the different playable colors.
 */
public enum Colors {
  Red,
  Blue;

  /**
   * Returns the name of the color in all uppercase as a String.
   * @return "RED" if the color is Red, "BLUE" if the color is Blue.
   */
  public String toStringForName() {
    switch (this) {
      case Red:
        return "RED";
      case Blue:
        return "BLUE";
      default:
        return "";
    }
  }

  /**
   * Returns a short string representation of the color.
   * @return "R" if the color is Red, "B" if the color is Blue.
   */
  public String toString() {
    switch (this) {
      case Red:
        return "R";
      case Blue:
        return "B";
      default:
        return "";
    }
  }

  /**
   * Transforms this enum representation of color to the awt version of color.
   * @return the java.awt version of Color that this represents.
   */
  public Color toColor() {
    switch (this) {
      case Red:
        return Color.RED;
      case Blue:
        return Color.BLUE;
      default:
        return null;
    }
  }
}
