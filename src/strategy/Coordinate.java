package strategy;

/**
 * Represents a coordinate in a 2-dimensional plane.
 * Has an X and Y value.
 */
public class Coordinate {
  private final int x;
  private final int y;

  /**
   * Constructor for Coordinate.
   * @param x the x coordinate.
   * @param y the y coordinate
   */
  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x value of this coordinate.
   * @return the x coordinate.
   */
  public int getX() {
    return this.x;
  }

  /**
   * Returns the y value of this coordinate.
   * @return the y coordinate.
   */
  public int getY() {
    return this.y;
  }
}
