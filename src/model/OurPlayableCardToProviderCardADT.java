package model;

import provider.model.inter.DirectionEnum;

/**
 * Two-way card adapter for our and the provider's card implementation.
 * Takes in an existing Card implementation (our model) and adapts it to both interfaces.
 */
public class OurPlayableCardToProviderCardADT implements provider.model.inter.Card, Card {
  private final Card adaptee;

  /**
   * Constructor for a two-way card adapter between our and the
   * provider's card implementation.
   * @param adaptee A card implementation for our model.
   */
  public OurPlayableCardToProviderCardADT(Card adaptee) {
    this.adaptee = adaptee;
  }

  @Override
  public String getName() {
    return adaptee.getName();
  }

  @Override
  public Numbers valueAt(Direction direction) {
    return adaptee.valueAt(direction);
  }

  @Override
  public int getNorthValue() {
    return adaptee.valueAt(Direction.NORTH).toNum();
  }

  @Override
  public int getSouthValue() {
    return adaptee.valueAt(Direction.SOUTH).toNum();
  }

  @Override
  public int getEastValue() {
    return adaptee.valueAt(Direction.EAST).toNum();
  }

  @Override
  public int getWestValue() {
    return adaptee.valueAt(Direction.WEST).toNum();
  }

  @Override
  public boolean battle(provider.model.inter.Card opponent, DirectionEnum direction) {
    switch (direction) {
      case NORTH:
        return this.getNorthValue() > opponent.getSouthValue();
      case SOUTH:
        return this.getSouthValue() > opponent.getNorthValue();
      case EAST:
        return this.getEastValue() > opponent.getWestValue();
      case WEST:
        return this.getWestValue() > opponent.getEastValue();
      default:
        throw new IllegalArgumentException("unknown direction");
    }
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (that instanceof OurPlayableCardToProviderCardADT) {
      OurPlayableCardToProviderCardADT cast = (OurPlayableCardToProviderCardADT)that;
      return this.getName().equals(cast.getName())
              && this.getNorthValue() == cast.getNorthValue()
              && this.getSouthValue() == cast.getSouthValue()
              && this.getEastValue() == cast.getEastValue()
              && this.getWestValue() == cast.getWestValue()
              && this.hashCode() == that.hashCode();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.getName().hashCode()
            + this.getNorthValue()
            + this.getSouthValue()
            + this.getEastValue()
            + this.getWestValue();
  }
}