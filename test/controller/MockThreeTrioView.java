package controller;

import java.io.IOException;
import java.util.Map;

import model.Card;
import strategy.Coordinate;
import view.Features;
import view.ThreeTrioGameView;

/**
 * A mock of ThreeTrioGameView. Records the inputs of method calls in the appendable.
 * @param <C> A type of card.
 */
public class MockThreeTrioView<C extends Card> implements ThreeTrioGameView {
  private final Appendable appendable;
  private Features feature;

  /**
   * Constructor. Takes in an appendable and sets the field to it.
   * @param appendable the appendable to append data to.
   */
  MockThreeTrioView(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void render() {
    this.addToAppendable("Render");
  }

  @Override
  public void setFeature(Features feature) {
    this.feature = feature;
  }

  @Override
  public void displayErrorMsg(String errMsg) {
    this.addToAppendable(errMsg);
  }

  @Override
  public void displayWinMsg(String winMsg) {
    this.addToAppendable(winMsg);
  }

  @Override
  public void showHints() {
    // intentionally left blank
  }

  @Override
  public void setHints(Map<Coordinate, Integer> hintFlips) {
    // intentionally left blank
  }

  // A public method for this mock that takes in coordinates and passes it to the controller.
  public void handCellClick(int row, int col) {
    this.feature.handleCellClick(row, col);
  }

  // A public method for this mock that takes in a cardIdx and passes it to the controller.
  public void handCardClick(int playerIdx, int cardIdx) {
    this.feature.handleCardClick(playerIdx, cardIdx);
  }

  private void addToAppendable(String msg) {
    try {
      this.appendable.append(msg);
    } catch (IOException e) {
      throw new IllegalStateException("Appendable cannot be written to.");
    }
  }
}
