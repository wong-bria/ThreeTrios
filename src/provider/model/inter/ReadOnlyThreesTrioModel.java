package provider.model.inter;

import java.util.List;

/**
 * Read-only interface for the Three Trios game model.
 * Contains only methods that observe the game state without modifying it.
 */
public interface ReadOnlyThreesTrioModel {

  /**
   * Gets the number of rows in the game grid.
   *
   * @return The number of rows in the grid.
   */
  int getRows();

  /**
   * Gets the number of columns in the game grid.
   *
   * @return The number of columns in the grid.
   */
  int getCols();

  /**
   * Gets the cell at the specified row and column.
   *
   * @param row The row of the cell.
   * @param col The column of the cell.
   * @return The cell at the specified position.
   */
  Cell getCell(int row, int col);

  /**
   * Gets the total count of cards present in the game grid.
   *
   * @return The number of cards in the grid.
   */
  int getCardCellCount();

  /**
   * Gets the current player whose turn it is.
   *
   * @return The current player.
   */
  Player getCurrentPlayer();


  /**
   * Gets the red player in the game.
   *
   * @return The red player.
   */
  Player getRedPlayer();

  /**
   * Gets the blue player in the game.
   *
   * @return The blue player.
   */
  Player getBluePlayer();

  /**
   * Gets the hand of the specified player.
   *
   * @param player The player whose hand is requested.
   * @return The list of cards in the player's hand.
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Checks if the game is over.
   *
   * @return True if the game is over, otherwise false.
   */
  boolean isGameOver();

  /**
   * Gets the winner of the game, if there is one.
   *
   * @return The player who won the game, or null if the game is not over.
   */
  Player getWinner();

  /**
   * Gets the score of the specified player.
   *
   * @param player The player whose score is requested.
   * @return The score of the player.
   */
  int getScore(Player player);

  /**
   * Checks if a given position (row, col) is a valid position on the grid.
   *
   * @param row The row to check.
   * @param col The column to check.
   * @return True if the position is valid, otherwise false.
   */
  boolean isValidPosition(int row, int col);

  /**
   * Checks if a card can be placed at the specified position (row, col).
   *
   * @param row The row where the card is to be placed.
   * @param col The column where the card is to be placed.
   * @return True if the card can be placed at the position, otherwise false.
   */
  boolean canPlaceCard(int row, int col);

  /**
   * Calculates number of cards that would be flipped if card is played.

   * @return number of cards that would be flipped
   */
  int calculatePotentialFlips(Card card, int row, int col);
}