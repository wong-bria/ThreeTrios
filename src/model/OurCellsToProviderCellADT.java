package model;

import player.PlayerADT;
import provider.model.inter.Card;
import provider.model.inter.Cell;
import provider.model.inter.CellEnum;
import provider.model.inter.Player;

/**
 * Object adapter for our Cells implementation to support the provider's Cell interface.
 */
public class OurCellsToProviderCellADT implements Cell {
  private final Cells adaptee;

  /**
   * Constructor to create an adapter for our Cells to Provider Cell interface.
   * @param adaptee Our implementation of a Cells object.
   */
  public OurCellsToProviderCellADT(Cells adaptee) {
    this.adaptee = adaptee;
  }

  @Override
  public boolean isEmpty() {
    return !adaptee.isHole() && !adaptee.hasCard();
  }

  @Override
  public boolean isPlayable() {
    return !adaptee.isHole();
  }

  @Override
  public void placeCard(Card card, Player owner) throws IllegalStateException {
    // This method is superfluous as it is not used in supporting the view.
  }

  @Override
  public void takeOver(Player newOwner) throws IllegalStateException {
    // This method is superfluous as it is not used in supporting the view.
  }

  @Override
  public CellEnum getType() {
    if (this.isPlayable()) {
      return CellEnum.CARD;
    } else {
      return CellEnum.HOLE;
    }
  }

  @Override
  public Card getCard() {
    if (this.isEmpty()) {
      return null;
    } else {
      model.Card card = adaptee.getCard();
      return new OurPlayableCardToProviderCardADT(card);
    }
  }

  @Override
  public Player getOwner() {
    if (this.isEmpty()) {
      return null;
    }
    return new PlayerADT(this.adaptee.cellColor());
  }
}
