package provider.model.inter;

/**
 * Interface representing a card in the Three Trios game.
 * Each card has a unique name and four attack values corresponding to
 * the cardinal directions (North, South, East, West). Attack values
 * range from 1-10 (where 'A' represents 10).
 */
public interface Card {
  /**
   * Gets the name of this card.
   *
   * @return the card's name
   */
  String getName();

  /**
   * Gets the attack value for the North direction.
   *
   * @return the North attack value (1-10)
   */
  int getNorthValue();

  /**
   * Gets the attack value for the South direction.
   *
   * @return the South attack value (1-10)
   */
  int getSouthValue();

  /**
   * Gets the attack value for the East direction.
   *
   * @return the East attack value (1-10)
   */
  int getEastValue();

  /**
   * Gets the attack value for the West direction.
   *
   * @return the West attack value (1-10)
   */
  int getWestValue();

  /**
   * Executes a battle between this card and an opponent card in the specified direction.
   * The battle compares this card's attack value in the given direction against
   * the opponent's attack value in the opposite direction.
   *
   * @param opponent the card being attacked
   * @param direction the direction of attack from this card's perspective
   * @return true if this card wins the battle (has higher attack value), false otherwise
   */
  boolean battle(Card opponent, DirectionEnum direction);
}