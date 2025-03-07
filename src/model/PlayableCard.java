package model;

/**
 * Represents a card that can be used in the Three Trio Game.
 */
public class PlayableCard implements Card {
  private final String name;
  private final Numbers northValue;
  private final Numbers southValue;
  private final Numbers eastValue;
  private final Numbers westValue;

  /**
   * Constructor to create a PlayableCard that can be used as part of a player's
   * hand and to be played onto a board.
   * @param name The name of a card as a String.
   * @param northValue The north attack value of this card.
   * @param southValue The south attack value of this card.
   * @param eastValue The east attack value of this card.
   * @param westValue The west attack value of this card.
   */
  public PlayableCard(String name, Numbers northValue, Numbers southValue, Numbers eastValue,
                      Numbers westValue) {
    this.name = name;
    this.northValue = northValue;
    this.southValue = southValue;
    this.eastValue = eastValue;
    this.westValue = westValue;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Numbers valueAt(Direction direction) {
    switch (direction) {
      case NORTH:
        return this.northValue;
      case SOUTH:
        return this.southValue;
      case EAST:
        return this.eastValue;
      case WEST:
        return this.westValue;
      default:
        return null;
    }
  }
}
