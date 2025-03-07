package model;

import java.util.HashMap;

/**
 * A concrete Cells class representing a cell that is a hole in the Three Trio Game.
 * A HoleCell holds nothing and is meant to be a Cell in the grid where the user can't
 * place a card on.
 */
public class HoleCell implements Cells {
  /**
   * Constructor to create a HoleCell to be used as part of a grid to represent
   * a cell that the user can't place a card on.
   */
  public HoleCell() {
    // intentionally left blank since a HoleCell holds nothing and only represents
    // a Cell in the grid where the user can't place a card on.
  }

  @Override
  public Cells copyOf() {
    return new HoleCell();
  }

  @Override
  public void changeColor(Colors color) {
    // intentionally left empty since hole's can't change color nor does it have a color
  }

  @Override
  public Colors cellColor() {
    return null;
  }

  @Override
  public boolean putCard(Card card) {
    return false;
  }

  @Override
  public boolean isHole() {
    return true;
  }

  @Override
  public HashMap<Cells, Direction> getNeighbors() {
    return null;
  }

  @Override
  public boolean hasCard() {
    return false;
  }

  @Override
  public void addNeighbor(Cells cell, Direction direction) {
    // intentionally left blank since a hole cell doesn't need to know its neighbors
  }

  @Override
  public Card getCard() {
    return null;
  }
}
