package model;

import java.util.List;

/**
 * Representation for what a Player in the Three Trio Game.
 * A player has a hand and a color to represent and help identify the cards they own in a game.
 * @param <C> The type of cards a player has.
 */
public interface ModelPlayer<C extends Card> {
  /**
   * Adds the given card to the player's hand.
   * @param card A card to be added to the player's hand.
   */
  void addCardToHand(C card);

  /**
   * Remove the given card from the player's hand.
   * @param card A card to be removed from a player's hand.
   * @throws IllegalArgumentException if given card is not in hand
   */
  void removeCardFromHand(C card);

  /**
   * Gets a player's hand.
   * Modifying returned list has no effect on the list.
   * @return A list of cards representing a player's hand.
   */
  List<C> getHand();

  /**
   * Returns the player's color: either Red or Blue.
   * @return The color representing the player.
   */
  Colors getColor();
}
