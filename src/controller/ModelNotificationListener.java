package controller;

/**
 * Interface for publishing notifications from the model to interested listeners.
 * These notifications include events such as whether it is your turn, game
 * over messages, invalid move warnings, and if the game state updates. (add more if needed)
 */
public interface ModelNotificationListener {
  /**
   * Notifies listeners of a change in the game state (a move was made).
   */
  void notifyGameStateUpdated();

  /**
   * Notifies listeners that it is the player's turn.
   * @param playerIndex The index of the player whose turn it is.
   */
  void notifyPlayerTurn(int playerIndex);

  /**
   * Notifies listeners that an invalid move was attempted.
   * @param message the error message in response to the invalid move.
   * @param playerIndex the player that attempted the invalid move.
   */
  void notifyInvalidMove(String message, int playerIndex);

  /**
   * Notifies listeners that the game is over and declares the winner.
   * @param winnerIndex The index of the winning player. (-1 for draw???)
   * @param winningScore The score of the winner.
   */
  void notifyGameOver(int winnerIndex, int winningScore);
}
