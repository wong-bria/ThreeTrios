package provider.model.inter;

/**
 * Enum representing card directions.
 */
public enum DirectionEnum {
  NORTH(0),
  SOUTH(1),
  EAST(2),
  WEST(3);

  private final int index;

  /**
   * Constructs a direction with the specified index.
   *
   * @param index the numeric index for this direction
   */
  DirectionEnum(int index) {
    this.index = index;
  }

  /**
   * Returns the opposite direction of the current direction.

   * @return the opposite direction
   * @throws IllegalStateException if the current direction is invalid
   */
  public DirectionEnum getOpposite() {
    switch (this) {
      case NORTH: return SOUTH;
      case SOUTH: return NORTH;
      case EAST: return WEST;
      case WEST: return EAST;
      default: throw new IllegalStateException("Invalid direction");
    }
  }

  /**
   * Gets the numeric index associated with this direction.
   *
   * @return the index of this direction
   */
  public int getIndex() {
    return index;
  }
}