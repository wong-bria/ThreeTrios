package model;

/**
 * Represents a card that can be played in Three Trios. Also contains the
 * operational and observational methods for a Card in a Three Trio Game.
 */
public interface Card {

  // Observations

  /**
   * Returns the name of a card.
   *
   * @return A String representing the name of a card
   */
  String getName();

  /**
   * Returns the value of the card given a direction.
   *
   * @return A Number representing the value of the card for the given direction.
   */
  Numbers valueAt(Direction direction);
}
