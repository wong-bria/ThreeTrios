package view;

/**
 * Represents the different Features in our view:
 * Being able to click a card, and being able to click a cell.
 */
public interface Features {
  /**
   * Notifies listeners of which player selected which card.
   * @param playerIndex The index of the player that was clicked on. 0 index based.
   * @param cardIndex The index of the card that was clicked. 0 index based.
   */
  void handleCardClick(int playerIndex, int cardIndex);

  /**
   * Notifies listeners of which row and column was selected.
   * @param row The row in the grid. 0 index based.
   * @param col The col in the grid. 0 index based.
   */
  void handleCellClick(int row, int col);

  /**
   * Tells the listener to clear the currently selected card.
   */
  void clearSelectedCard();

  /**
   * Toggles between true and false for if a user wants to show hints.
   * @param letter The key that the user pressed.
   */
  void toggleHint(char letter);
}
