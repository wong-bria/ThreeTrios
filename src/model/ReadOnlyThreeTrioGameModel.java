package model;

import java.util.List;
import java.util.Optional;

/**
 * Represents a read-only version of the ThreeTrioGameModel.
 * Only observations on the game's current state can be made.
 * Modifications to the objects outputted by the methods do not affect the state of the game.
 * @param <C> the type of card being used.
 */
public interface ReadOnlyThreeTrioGameModel<C extends Card> {
  // Observations

  /**
   * Returns if the game is over (all empty card cells are filled).
   * @return true if the game has ended and false otherwise
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  /**
   * Returns if the game is won by the first player. Win if number of cards on board first player
   * owns + number of cards in first player hand is > number of cards on board second player owns
   * + number of cards in second player hand.
   * @return true if the game has been won by first player, or false otherwise
   * @throws IllegalStateException if the game has not started or the game is not over
   */
  boolean didPlayerOneWin();

  /**
   * Returns if the game is won by the second player. Win if number of cards on board second player
   * owns + number of cards in second player hand is > number of cards on board first player owns
   * + number of cards in first player hand.
   * @return true if the game has been won by second player, or false otherwise
   * @throws IllegalStateException if the game has not started or the game is not over
   */
  boolean didPlayerTwoWin();

  /**
   * Returns a copy of the hand in the game. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   * @return a new list containing the cards in the player's hand in the same order
   *     as in the current state of the game.
   * @throws IllegalStateException if the game has not started
   */
  List<C> getCurrentPlayerHand();

  /**
   * Returns a copy of the grid representing the current state of the game.
   * Modifying the returned list has no effect on the game.
   * @return a copy of the representation of the grid.
   * @throws IllegalStateException if the game has not started.
   **/
  List<List<Cells>> getGrid();

  /**
   * Gets whichever players turn it is. Return their player index.
   * @return The index representing which players turn it is.
   * @throws IllegalStateException if the game has not started.
   */
  int getTurn();

  /**
   * Gets the indicated player's score (total cards they own) based on given player index
   * in the game (0 for player1 and 1 for player2).
   * @param playerIdx The index of a player. If playerI, then playerIdx = I-1.
   * @return A score represented by an int on the total amount of cards a player owns.
   * @throws IllegalArgumentException given player index < 0 or >= number of players
   */
  int getScore(int playerIdx);

  /**
   * Finds the number of cards the current player can flip by playing selected card
   * at given coordinate.
   * @param playerIdx The index of a player. If playerI, then playerIdx = I-1.
   * @param handIdx The card to be played on the board from user hand. 0 index based.
   * @param row The row of the grid. 0 index based. Grows downward.
   * @param col The column of the grid. 0 index based. Grows right.
   * @return A count of how many cards are flipped.
   * @throws IllegalArgumentException given player index < 0 or >= number of players
   * @throws IllegalArgumentException Coordinates not on grid, on a hole cell, or on cell that
   *                                  already contains a card.
   * @throws IllegalArgumentException handIdx < 0 or >= number of cards in current player hand.
   */
  int getFlipCount(int playerIdx, int handIdx, int row, int col);

  /**
   * Checks if a player can play at the given coordinate: Within the grid, not a hole cell,
   * and cell does not contain a card.
   * @param row The row of the grid. 0 index based.
   * @param col The column of the grid. 0 index based.
   * @return True if a user can play at given coordinate. False otherwise.
   * @throws IllegalStateException if game has not started.
   */
  boolean checkLegal(int row, int col);

  /**
   * Checks who owns a card in a cell at given coordinate.
   * @param row The row of the grid. 0 index based.
   * @param col The column of the grid. 0 index based.
   * @return Red if card owned by player 1. Blue if card owned by player 2.
   * @throws IllegalStateException if the game has not started.
   * @throws IllegalArgumentException if no card is in the given cell.
   * @throws IllegalArgumentException if the coordinate is not on the grid.
   */
  Colors getCardOwner(int row, int col);

  /**
   * Returns the color of the player at the given index.
   * @return the color of the given player.
   * @throws IllegalArgumentException if playerIdx is out of bounds.
   */
  Colors getPlayerColor(int playerIdx);

  /**
   * Returns a copy of indicated player's hand in the game. This means modifying the
   * returned list or the cards in the list has no effect on the game.
   * @return a new list containing the cards in the indicated player's hand in the
   *         same order as in the current state of the game.
   * @throws IllegalStateException if the game has not started.
   * @throws IllegalArgumentException given player index < 0 or >= number of players
   */
  List<C> getPlayerHand(int playerIdx);

  /**
   * Gets the content of a cell at the given coordinate.
   * @param row The row of the grid. 0 index based.
   * @param col The column of the grid. 0 index based.
   * @return an Optional containing a card if the cell contains a card,
   *         or Optional.empty otherwise.
   * @throws IllegalStateException if the game has not started.
   * @throws IllegalArgumentException if coordinate is not on the grid.
   */
  Optional<Card> getContentAtCell(int row, int col);

  /**
   * Finds the length of the grid (the number of rows).
   * @return An int representing the length of the grid.
   * @throws IllegalStateException if the game has not started.
   */
  int getGridLength();

  /**
   * Finds the width of the grid (the number of columns).
   * @return An int representing the width of the grid.
   * @throws IllegalStateException if the game has not started.
   */
  int getGridWidth();

  /**
   * Finds row of the card that was last played.
   * @return An int representing the row of the card last played.
   * @throws IllegalStateException if the game has not started.
   */
  int lastPlayedRow();

  /**
   * Finds the column of the card that was last played.
   * @return An int representing the column of the card last played.
   * @throws IllegalStateException if the game has not started.
   */
  int lastPlayedCol();
}
