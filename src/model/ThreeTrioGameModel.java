package model;

import java.util.List;

/**
 * Represents a game model for a Three Trio game. A game consists of a grid,
 * players, and a deck which will be used to distribute to each players hand evenly.
 * @param <C> The type of card used
 */
public interface ThreeTrioGameModel<C extends Card> extends ReadOnlyThreeTrioGameModel<C> {

  /**
   * Starts the game with the given grid and card config files.
   * @param grid the grid being copied for this instance of ThreeTrio.
   * @param deck the deck being copied for this instance of ThreeTrio.
   * @throws IllegalStateException If the game has started or the game is over.
   * @throws IllegalArgumentException If given deck does not have enough cards to fill each
   *                                  players hand. If deck size is < number of CardCell on grid.
   */
  void startGame(List<List<Cells>> grid, List<C> deck);

  /**
    * Plays the card at the given card index at the position (row,col).
    * Model determines which player is playing.
    * @param handIdx The hand index for a card that the user wants to play. 0 index based.
    * @param row The row of the grid. 0 index based.
    * @param col The column of the grid. 0 index based.
    * @throws IllegalArgumentException if handIdx < 0 or out of bounds.
    * @throws IllegalArgumentException if row or col < 0 or out of bounds
    * @throws IllegalArgumentException if cell already has a card or is a hole
    * @throws IllegalStateException if the game has not started.
    */
  void playCard(int handIdx, int row, int col);

  /**
   * Compares the values of the placed card to neighboring enemy cards.
   * Side effect: If neighbor of this cell with a card that has a lower attack value
   * than the card just played, then switch the neighbor cell card color to the color
   * of the card just played. Traverse to neighboring cells.
   * @throws IllegalStateException if the game has not started or is over.
   */
  void battle();
}
