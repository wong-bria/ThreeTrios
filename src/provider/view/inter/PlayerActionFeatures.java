package provider.view.inter;

/**
 * Interface for player actions in Three Trios game.
 * Represents requests that a player can make during the game.
 */
public interface PlayerActionFeatures {
  /**
   * Request to select a card from player's hand.

   * @param cardIndex index of card in hand
   */
  void cardSelected(int cardIndex);

  /**
   * Request to deselect currently selected card.
   */
  void cardDeselected();

  /**
   * Called when a position on the grid is selected.

   * @param row grid row
   * @param col grid column
   */
  void positionSelected(int row, int col);
}