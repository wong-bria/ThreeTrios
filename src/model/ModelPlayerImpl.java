package model;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation for a ModelPlayer.
 * Holds information on the hand of each player and the color of the player.
 */
public class ModelPlayerImpl implements ModelPlayer<PlayableCard> {
  private final List<PlayableCard> hand;
  private final Colors color; // represents the color a player is

  /**
   * Constructor.
   * Creates a new object that holds the information of a player in a Three Trios game.
   */
  public ModelPlayerImpl(Colors color) {
    this.hand = new ArrayList<>();
    this.color = color;
  }

  @Override
  public void addCardToHand(PlayableCard card) {
    this.hand.add(card);
  }

  @Override
  public void removeCardFromHand(PlayableCard card) {
    if (!this.hand.contains(card)) {
      throw new IllegalArgumentException("Given card is not in Player's hand");
    }
    this.hand.remove(card);
  }

  @Override
  public List<PlayableCard> getHand() {
    return new ArrayList<>(this.hand);
  }

  @Override
  public Colors getColor() {
    return this.color;
  }
}
