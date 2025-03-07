package model.ruled;

import java.util.HashMap;

import model.Card;
import model.Cells;
import model.Direction;

/**
 * A rule where all surrounding cards can be flipped.
 * This only occurs if two or more surrounding cards share the same opposing values as the
 * newly placed card.
 * Example:
 * A is below B and left of C.
 * A.North = 10, B.South = 10
 * A.East = 4, C.West = 4
 * A flips all surrounding cards that are not owned by the player
 * Requires that at least two cards satisfy this condition before allowing flips.
 */
public class SameRule implements ThreeTrioRule {
  @Override
  public boolean satisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard()) {
      return false;
    }

    Card card = input.getCard();
    HashMap<Cells, Direction> directNeighbors = input.getNeighbors();
    int numSatisfied = 0;
    for (Cells cell : directNeighbors.keySet()) {
      Direction toCard = directNeighbors.get(cell);
      if (cell.hasCard() && this.sameOpposingValue(card, cell.getCard(), toCard)) {
        numSatisfied += 1;
      }
    }

    return numSatisfied >= 2;
  }

  // This is not affected by reverses, so it will not do anything different.
  @Override
  public boolean reverseSatisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    return this.satisfiesFlip(input, neighbor, toNeighbor);
  }

  private boolean sameOpposingValue(Card card, Card neighbor, Direction toCard) {
    //return card.valueAt(toCard.getOpposite()) == neighbor.valueAt(toCard);
    return card.valueAt(toCard) == neighbor.valueAt(toCard.getOpposite());
  }

  @Override
  public boolean isMutuallyExclusive(ThreeTrioRule opposing) {
    return opposing instanceof PlusRule;
  }

  @Override
  public boolean allowCombo() {
    return false;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof SameRule;
  }
}
