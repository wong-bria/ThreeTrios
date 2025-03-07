package model.ruled;

import model.Cells;
import model.Direction;

/**
 * Represents a ThreeTrioRule: a rule that determines if a card cell should be flipped.
 * The truth value depends on the implementation.
 * Some implementations are mutually exclusive with other implementations.
 */
public interface ThreeTrioRule {
  /**
   * Returns true if the given inputs satisfy the rule.
   * @param input the intended card cell to be judged.
   * @param neighbor the neighboring cell being compared with.
   * @param toNeighbor the direction to get to the neighbor.
   * @return true iff the rule is satisfied.
   */
  boolean satisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor);

  /**
   * Returns true if the reverse of the rule is true.
   * @param input the intended card cell to be judged.
   * @param neighbor the neighboring cell being compared with.
   * @param toNeighbor the direction to get to the neighbor.
   * @return true iff the inverse of the rule is satisfied.
   */
  boolean reverseSatisfiesFlip(Cells input, Cells neighbor, Direction toNeighbor);

  /**
   * Returns true iff this rule is mutually exclusive with other mutually exclusive rules.
   * @param opposing the rule being checked against.
   * @return true iff this rule is mutually exclusive.
   */
  boolean isMutuallyExclusive(ThreeTrioRule opposing);

  /**
   * Returns true if this rule is allowed in combos. Otherwise, return false.
   * @return true iff this rule is allowed in combos.
   */
  boolean allowCombo();
}
