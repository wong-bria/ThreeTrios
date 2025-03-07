package provider.model.inter;

import strategy.InfailableThreeTrioStrategy;

/**
 * Interface representing a player in the Threes Trio game.
 * A player can manage their hand of cards, maintain their score, and execute game strategies.
 * FOR HW8: We NEED to change the strategy to our strategy because the strategies are not provided!
 */
public interface Player {

  /**
   * Gets the player's color.
   *
   * @return the player's color as a string
   */
  String getColor();

  /**
   * Adds a card to the player's hand.
   *
   * @param card the ThreesTrioCard to add
   * @throws IllegalArgumentException if the card is null
   */
  void addCardToHand(Card card) throws IllegalArgumentException;

  /**
   * Removes a card from the player's hand.
   *
   * @param card the ThreesTrioCard to remove
   * @throws IllegalStateException if the card is not in the player's hand
   */
  void removeCardFromHand(Card card) throws IllegalStateException;

  /**
   * Checks if the player has a specific card in their hand.
   *
   * @param card the ThreesTrioCard to check
   * @return true if the card is in the player's hand, false otherwise
   */
  boolean hasCard(Card card);

  /**
   * Gets the number of cards in the player's hand.
   *
   * @return the number of cards in the player's hand
   */
  int getHandSize();

  /**
   * Gets a copy of the player's hand.
   *
   * @return an array of ThreesTrioCard objects representing the player's hand
   */
  Card[] getHand();

  /**
   * Increments the player's score by 1.
   */
  void incrementScore();

  /**
   * Decrements the player's score by 1.
   */
  void decrementScore();

  /**
   * Gets the player's current score.
   *
   * @return the player's current score
   */
  int getScore();

  /**
   * Checks if the player has cards in their hand and can make a move.
   *
   * @return true if the player has cards, false otherwise
   */
  boolean canMove();

  /**
   * Clears all cards from the player's hand.
   */
  void clearHand();

  /**
   * Checks if the player is an AI-controlled player.
   *
   * @return true if the player is AI-controlled, false if the player is human
   */
  boolean isAiPlayer();

  /**
   * Sets the strategy for the Threes Trio game.
   *
   * @param strategy the ThreesTrioStrategy representing the strategy to use
   */
  void setStrategy(InfailableThreeTrioStrategy<?> strategy);

  /**
   * Gets the current strategy for the Threes Trio game.
   *
   * @return the ThreesTrioStrategy representing the current game strategy
   */
  InfailableThreeTrioStrategy<?> getStrategy();
}
