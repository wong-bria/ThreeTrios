package player;

import java.util.List;
import java.util.Objects;

import model.Colors;
import provider.model.inter.Card;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;
import strategy.InfailableThreeTrioStrategy;

/**
 * Object adapter for our PlayerActions to our provider's Player interface.
 */
public class PlayerADT implements Player {
  private final PlayerActions playerActions;
  private final ReadOnlyThreesTrioModel model;
  private final Colors color;

  /**
   * Constructor for the Player adapter.
   * @param color The color of this player.
   */
  public PlayerADT(Colors color) {
    this.playerActions = null;
    this.model = null;
    this.color = color;
  }

  /**
   * Constructor for the Player Adapter.
   * @param playerActions A human or machine player.
   * @param model A read only version of the game model.
   * @param color The color of the player.
   */
  public PlayerADT(PlayerActions playerActions, ReadOnlyThreesTrioModel model, Colors color) {
    this.playerActions = playerActions;
    this.model = model;
    this.color = color;
  }

  @Override
  public String getColor() {
    String colorAsStr = this.color.toString();
    if (colorAsStr.equals("R")) {
      return "RED";
    }
    return "BLUE";
  }

  @Override
  public void addCardToHand(Card card) throws IllegalArgumentException {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public void removeCardFromHand(Card card) throws IllegalStateException {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public boolean hasCard(Card card) {
    Objects.requireNonNull(this.model);
    return this.model.getPlayerHand(this).contains(card);
  }

  @Override
  public int getHandSize() {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
    return 0;
  }

  @Override
  public Card[] getHand() {
    Objects.requireNonNull(this.model);
    List<Card> cards = this.model.getPlayerHand(this);
    return cards.toArray(new Card[0]);
  }

  @Override
  public void incrementScore() {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public void decrementScore() {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public int getScore() {
    Objects.requireNonNull(this.model);
    return this.model.getScore(this);
  }

  @Override
  public boolean canMove() {
    Objects.requireNonNull(this.model);
    return this.model.getCurrentPlayer().equals(this);
  }

  @Override
  public void clearHand() {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public boolean isAiPlayer() {
    Objects.requireNonNull(this.playerActions);
    return this.playerActions.isMachine();
  }

  @Override
  public void setStrategy(InfailableThreeTrioStrategy<?> strategy) {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
  }

  @Override
  public InfailableThreeTrioStrategy<?> getStrategy() {
    // This method is superfluous because we will not be using PlayerADT except for the view.
    // The view does not call this method.
    return null;
  }
}
