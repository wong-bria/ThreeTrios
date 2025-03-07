package model;

/**
 * Represents the direction a cell can be in relation to a neighboring cell.
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Gets the opposite direction of this direction.
   * @return The opposite of this direction.
   */
  public Direction getOpposite() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      default:
        return EAST;
    }
  }
}
