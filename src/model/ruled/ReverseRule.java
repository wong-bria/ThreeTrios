package model.ruled;

/**
 * Notifies the rule keeper that the reverse of the rules will be used.
 * Works functionally the same as FlipGreaterRule.
 */
public class ReverseRule extends FlipGreaterRule implements ThreeTrioRule {
  // This class does nothing. Only serves to notify the rule keeper that the reverse methods
  // will be used.
  @Override
  public boolean isMutuallyExclusive(ThreeTrioRule opposing) {
    return opposing instanceof FlipGreaterRule;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof ReverseRule;
  }

  @Override
  public int hashCode() {
    return 1; // handins required we override
  }
}
