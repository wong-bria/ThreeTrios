package provider.model.inter;

/**
 * Interface representing the game grid for Three Trios.
 * The grid consists of a rectangular array of cells that can be either
 * holes (unplayable spaces) or card cells (empty or containing cards).

 * Default access scope is used to restrict implementation to the model package,
 * while allowing classes within the same package to implement this interface.
 */
interface GridModel {
  /**
   * Checks if the given position exists within the grid boundaries.
   *
   * @param row the row index to check
   * @param col the column index to check
   * @return true if the position is within grid boundaries, false otherwise
   */
  boolean isValidPosition(int row, int col);

  /**
   * Checks if a card can be placed at the specified position.
   *
   * @param row the row index to check
   * @param col the column index to check
   * @return true if a card can be placed at the position, false otherwise
   */
  boolean canPlaceCard(int row, int col);

  /**
   * Places a card at the specified position with the given owner.
   *
   * @param row the row index for card placement
   * @param col the column index for card placement
   * @param card the card to place
   * @param owner the player who owns the card
   * @throws IllegalArgumentException if the position is invalid or already occupied
   */
  void placeCard(int row, int col, Card card, Player owner);

  /**
   * Gets the cell at the specified position.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the cell at the specified position
   * @throws IllegalArgumentException if the position is invalid
   */
  Cell getCell(int row, int col);

  /**
   * Getter for the number of rows in the grid.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Getter for the number of columns in the grid.
   *
   * @return the number of columns
   */
  int getCols();

  /**
   * Gets the total number of card cells in the grid.
   *
   * @return the number of card cells
   */
  int getCardCellCount();

  /**
   * Checks if all card cells in the grid are filled.
   * The game ends when the grid is full.
   *
   * @return true if all card cells contain cards, false otherwise
   */
  boolean isFull();
}
