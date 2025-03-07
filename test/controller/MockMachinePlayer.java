package controller;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import player.PlayerActions;
import strategy.Coordinate;
import strategy.Tuple;

/**
 * A mock version of a MachinePlayer used to help simulate a MachinePlayer
 * using their playCard method to select a cell and card to play.
 * @param <C> A type of card.
 */
public class MockMachinePlayer<C extends Card> implements PlayerActions {
  private final Coordinate coordinate;
  private final int handIndex;

  /**
   * The constructor for a MockMachinePlayer.
   * @param coordinate The coordinate of the cell to play at.
   * @param handIndex The card index from this player's hand to play onto the grid.
   */
  public MockMachinePlayer(Coordinate coordinate, int handIndex) {
    this.coordinate = coordinate;
    this.handIndex = handIndex;
  }

  @Override
  public List<Tuple<Coordinate, Integer>> playCard(int playerIdx) {
    Tuple<Coordinate, Integer> fake = new Tuple<>(this.coordinate, this.handIndex);
    List<Tuple<Coordinate, Integer>> fakeList = new ArrayList<>();
    fakeList.add(fake);
    return fakeList;
  }

  @Override
  public boolean isMachine() {
    return true;
  }
}
