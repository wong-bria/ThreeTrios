package model.ruled;

import model.Cells;
import model.Direction;

/**
 * A rule where Aces can be overtaken when faced with a 1.
 * Reversible s.t. 1s can be overtaken with Aces.
 */
public class FallenAceRule implements ThreeTrioRule {
  // To note: A comparison value of -9/9 is used.
  // -9/9 is only achievable when taking the difference between an Ace (10) and 1
  // because we do not support negative values.
  @Override
  public boolean satisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard() || !neighbor.hasCard()) {
      return false;
    }

    int forCard = input.getCard().valueAt(toNeighbor).toNum();
    int forNeighbor = neighbor.getCard().valueAt(toNeighbor.getOpposite()).toNum();
    //return forCard - forNeighbor == -9;

    int difference = forCard - forNeighbor;

    // true when this is greater than other but not A vs 1, or 1 vs A
    return (difference > 0 && difference != 9) || difference == -9;

  }

  @Override
  public boolean reverseSatisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard() || !neighbor.hasCard()) {
      return false;
    }

    int forCard = input.getCard().valueAt(toNeighbor).toNum();
    int forNeighbor = neighbor.getCard().valueAt(toNeighbor.getOpposite()).toNum();
    //return forCard - forNeighbor == 9;

    int difference = forCard - forNeighbor;

    // true when this is less than other
    return (difference < 0 && difference != -9) || difference == 9;
  }

  @Override
  public boolean isMutuallyExclusive(ThreeTrioRule opposing) {
    return false;
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
    return that instanceof FallenAceRule;
  }
}
