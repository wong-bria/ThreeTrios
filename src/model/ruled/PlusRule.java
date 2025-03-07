package model.ruled;

import java.util.HashMap;
import java.util.Map;

import model.Cells;
import model.Direction;

/**
 * A rule where cards are flipped if at least two opposing pairs of adjacent cards
 * have the same sum with the current card in their respective opposing directions.
 * Example: https://piazza.com/class/m0e615cu4ekgo/post/1297
 */
public class PlusRule implements ThreeTrioRule {
  @Override
  public boolean satisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    if (!input.hasCard() || !neighbor.hasCard()) {
      return false;
    }

    return checkPlusRule(input);
  }

  private boolean checkPlusRule(Cells input) {
    Map<Cells, Direction> neighbors = input.getNeighbors();

    // Map to store sums for each direction.
    Map<Direction, Integer> directionSums = new HashMap<>();

    // Calculate sums for valid neighbors.
    for (Map.Entry<Cells, Direction> entry : neighbors.entrySet()) {
      Cells neighbor = entry.getKey();
      Direction dir = entry.getValue();

      if (neighbor.hasCard()) {
        int inputValue = input.getCard().valueAt(dir).toNum();
        int neighborValue = neighbor.getCard().valueAt(dir.getOpposite()).toNum();
        int sum = inputValue + neighborValue;

        directionSums.put(dir, sum);
      }
    }

    // Count occurrences of each sum.
    Map<Integer, Integer> sumCounts = new HashMap<>();
    for (int sum : directionSums.values()) {
      sumCounts.put(sum, sumCounts.getOrDefault(sum, 0) + 1);
    }

    // Check if at least one sum occurs in 2 or more opposing directions.
    for (int count : sumCounts.values()) {
      if (count >= 2) {
        return true;
      }
    }

    return false;
  }

  // This is not affected by reverses, so it will not do anything different.
  @Override
  public boolean reverseSatisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor) {
    return this.satisfiesFlip(input, neighbor, toNeighbor);
  }

  @Override
  public boolean isMutuallyExclusive(ThreeTrioRule opposing) {
    return opposing instanceof SameRule;
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
    return that instanceof PlusRule;
  }
}
