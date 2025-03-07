package provider.model.inter;

/**
 * Interface representing a cell in the Threes Trio game.
 * A cell can either be a hole or a playable card cell. This interface defines
 * methods to manage cell properties, place cards, and manage card ownership.
 */
public interface Cell {

  /**
   * Checks if this cell is empty and available for card placement.
   *
   * @return true if the cell is an empty card cell, false otherwise
   */
  boolean isEmpty();

  /**
   * Checks if this cell can accept card placements.
   *
   * @return true if this is a card cell, false if it's a hole
   */
  boolean isPlayable();

  /**
   * Places a card in this cell with the specified owner.
   *
   * @param card the card to place
   * @param owner the player who owns the card
   * @throws IllegalStateException if the cell is not playable or already contains a card
   */
  void placeCard(Card card, Player owner) throws IllegalStateException;

  /**
   * Changes the ownership of the card in this cell.
   *
   * @param newOwner the player taking ownership of the card
   * @throws IllegalStateException if the cell doesn't contain a card
   */
  void takeOver(Player newOwner) throws IllegalStateException;

  /**
   * Gets the type of this cell.
   *
   * @return the cell type (HOLE or CARD)
   */
  CellEnum getType();

  /**
   * Gets the card in this cell, if any.
   *
   * @return the card in this cell, or null if empty
   */
  Card getCard();

  /**
   * Gets the owner of the card in this cell, if any.
   *
   * @return the owner of the card, or null if cell is empty
   */
  Player getOwner();
}
