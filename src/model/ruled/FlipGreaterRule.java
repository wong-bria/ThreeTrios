package model.ruled;

import model.Cells;
import model.Direction;

/**
 * The default playing rule.
 * Cards are flipped iff the newly placed card has an opposing value greater than its neighbor's
 * value. (i.e. newly placed card's North value is 8, neighbor's South value is 3, and newly placed
 * card is directly below neighbor).
 */
public class FlipGreaterRule implements ThreeTrioRule {

  @Override
  public boolean satisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard() || !neighbor.hasCard()) {
      return false;
    }

    int forCard = input.getCard().valueAt(toNeighbor).toNum();
    int forNeighbor = neighbor.getCard().valueAt(toNeighbor.getOpposite()).toNum();
    return forCard > forNeighbor;
  }

  // Reverses the logic, where the card only flips if the newly placed card's opposing value is
  // less than the neighbor.
  @Override
  public boolean reverseSatisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard() || !neighbor.hasCard()) {
      return false;
    }

    int forCard = input.getCard().valueAt(toNeighbor).toNum();
    int forNeighbor = neighbor.getCard().valueAt(toNeighbor.getOpposite()).toNum();
    return forCard < forNeighbor;
  }

  @Override
  public boolean isMutuallyExclusive(ThreeTrioRule opposing) {
    return opposing instanceof ReverseRule;
  }

  @Override
  public boolean allowCombo() {
    return true;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof FlipGreaterRule;
  }
}
