package controller;

import model.Card;
import view.Features;

/**
 * Controller interface for ThreeTrios. Serves as a listener for both the model and the view.
 * Controls the gameplay loop of ThreeTrios by coordinating user and machine inputs to
 * create changes in the game state.
 */
public interface ThreeTrioGameController<C extends Card> extends ModelNotificationListener,
        Features {
  /**
   * Notifies listeners that it is the player's turn.
   * Derived from ModelNotificationListener.
   * @param playerIndex The index of the player whose turn it is.
   */
  void notifyPlayerTurn(int playerIndex);

  /**
   * Notifies listeners that the game is over and declares the winner.
   * Derived from ModelNotificationListener.
   * @param winnerIndex The index of the winning player. (-1 for draw???)
   * @param winningScore The score of the winner.
   */
  void notifyGameOver(int winnerIndex, int winningScore);

  /**
   * Notifies listeners that an invalid move was attempted.
   * Derived from ModelNotificationListener.
   * @param playerIndex the player that attempted the invalid move.
   */
  void notifyInvalidMove(String message, int playerIndex);

  /**
   * Notifies listeners of a change in the game state (a move was made).
   * Derived from ModelNotificationListener.
   */
  void notifyGameStateUpdated();

  /**
   * Prints a message saying which player's hand and card index was clicked on.
   * Derived from Features.
   * @param playerIndex The index of the player that was clicked on. 0 index based.
   * @param cardIndex The index of the card that was clicked. 0 index based.
   */
  void handleCardClick(int playerIndex, int cardIndex);

  /**
   * Prints a message saying which coordinate in the grid was clicked on.
   * Derived from Features.
   * @param row The row in the grid. 0 index based.
   * @param col The col in the grid. 0 index based.
   */
  void handleCellClick(int row, int col);

  /**
   * Tells the listener to clear the currently selected card.
   */
  void clearSelectedCard();
}
