package model;

import java.util.HashMap;

/**
 * Operational and observational methods for a Cell in the game of ThreeTrioGame.
 */
public interface Cells {
  /**
   * Returns a copy of this cell.
   * Mutating the copy will not affect the actual cell.
   *
   * @return A copy of this cell.
   */
  Cells copyOf();

  /**
   * Changes the current color of the cell to the given color.
   *
   * @param color The color that the cell's color will be changed to.
   */
  void changeColor(Colors color);

  /**
   * Returns the color of a cell.
   *
   * @return A Color representing the color of a cell
   */
  Colors cellColor();

  /**
   * Attempts to put a card onto the current cell.
   * Does nothing if the cell is a hole cell or the cell is already occupied.
   * @param card the card being played.
   * @return true iff the card was successfully put into the cell.
   */
  boolean putCard(Card card);

  /**
   * Determines if a Cell is a hole or not.
   * @return True is Cell is a HoleCell. False otherwise.
   */
  boolean isHole();

  /**
   * Determines if there is a card on a cell or not.
   * @return True if there is a card on a cell. False otherwise.
   */
  boolean hasCard();

  /**
   * Adds a neighboring cell to this cell's list of neighbor cells with a direction
   * to specify which neighbor it is.
   * @param cell The cell to be added to this cell's list of neighbor cells.
   */
  void addNeighbor(Cells cell, Direction direction);

  /**
   * Returns the card associated with this cell if it exists.
   * @return The card in this cell.
   */
  Card getCard();

  /**
   * Returns a HashMap of neighboring Cells and their direction in relation to
   * whichever cell calls this method. Modifying the returned HashMap has no effect
   * on a Cell's neighbors since returned HashMap is a copy.
   * @return A mapping between the neighboring Cells and their direction relative to
   *         where the cell calling this method is.
   */
  HashMap<Cells, Direction> getNeighbors();
}