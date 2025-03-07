package model;

import java.util.HashMap;

/**
 * A concrete Cells class representing a cell that can contain a card in the Three Trio Game.
 * A CardCell can either have one card or no card represented by null. It also has a HashMap
 * called neighbors that maps neighboring cells to the direction that are in relation to this
 * CardCell. Example: There is a neighboring cell to the right of this cell, then the
 * neighboring cell is East of this cell.
 */
public class CardCell implements Cells {
  private Card card;
  private final HashMap<Cells, Direction> neighbors;
  private Colors color;

  /**
   * Constructor to create a CardCell to be used as part of a grid to be played on.
   */
  public CardCell() {
    this.neighbors = new HashMap<>();
    this.color = null;
  }

  /**
   * Constructor to create a CardCell with a card.
   * @param card A Card that this CardCell will contain.
   */
  public CardCell(Card card) {
    this.card = card;
    this.neighbors = new HashMap<>();
    this.color = null;
  }

  @Override
  public Cells copyOf() {
    CardCell copy = new CardCell(card);
    copy.changeColor(this.cellColor());
    return copy;
  }

  @Override
  public void changeColor(Colors color) {
    this.color = color;
  }

  @Override
  public Colors cellColor() {
    return this.color;
  }

  @Override
  public void addNeighbor(Cells cell, Direction direction) {
    this.neighbors.put(cell, direction);
  }

  @Override
  public boolean putCard(Card card) {
    if (this.card == null) {
      this.card = card;
      return true;
    }
    return false;
  }

  @Override
  public boolean isHole() {
    return false;
  }

  @Override
  public Card getCard() {
    return this.card;
  }

  @Override
  public HashMap<Cells, Direction> getNeighbors() {
    return new HashMap<>(this.neighbors);
  }

  @Override
  public boolean hasCard() {
    return card != null;
  }
}
