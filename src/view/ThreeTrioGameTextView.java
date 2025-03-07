package view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.Card;
import model.Colors;
import model.Direction;
import model.ThreeTrioGameModel;
import model.Cells;
import strategy.Coordinate;

/**
 * Provides a text-based view for displaying the state of a ThreeTrioGameModel. This class
 * is responsible for rendering the grid, current player's hand, and current player's color.
 * To produce a visualization of a model, first initialize a model with the default constructor.
 * Then, call startGame on model with a gridFile and cardFile. To use our grid files, do
 * "BoardConfig" + File.separator + "Name of file". To use our card files, do
 * "CardConfig" + File.separator + "Name of file". Afterward, initialize a view that takes
 * in a model and Appendable. Finally, call name of view you made .toString();
 */
public class ThreeTrioGameTextView implements ThreeTrioGameView {
  private final ThreeTrioGameModel<?> model;
  private final Appendable appendable;

  /**
   * Constructs a view for the ThreeTrioGame, allowing states of the game to be displayed.
   *
   * @param model      The model that represents a game of ThreeTrioGame that will be played.
   * @param appendable Where the output destination where the game state will be rendered.
   * @throws IllegalArgumentException If model or appendable is null
   */
  public ThreeTrioGameTextView(ThreeTrioGameModel<?> model, Appendable appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Model and appendable can't be null");
    }
    this.model = model;
    this.appendable = appendable;
  }

  /**
   * Displays the state of the game as a text view.
   *
   * @return A text view representation of a ThreeTrioGame
   */
  public String toString() {
    StringBuilder buildState = new StringBuilder();

    // display player turn
    Colors turn = this.model.getPlayerColor(this.model.getTurn());

    buildState.append("Player: ").append(turn.toStringForName()).append("\n");

    // display grid
    List<List<Cells>> grid = this.model.getGrid();
    for (List<Cells> row : grid) {
      for (Cells cell : row) {
        if (cell.isHole()) {
          buildState.append("_");
        } else if (cell.getCard() == null) {
          buildState.append(" ");
        } else {
          buildState.append(cell.cellColor().toString());
        }
      }
      buildState.append("\n");
    }

    // display hand
    buildState.append("Hand:\n");
    List<? extends Card> currentPlayerHand = this.model.getCurrentPlayerHand();
    buildState.append(formatCards(currentPlayerHand));

    return buildState.toString();
  }

  private String formatCards(List<? extends Card> cards) {
    if (cards.isEmpty()) {
      return "";
    }

    StringBuilder formattedCards = new StringBuilder();

    for (Card card : cards) {
      formattedCards.append(card.getName() + " " + card.valueAt(Direction.NORTH)
              + " " + card.valueAt(Direction.SOUTH)
              + " " + card.valueAt(Direction.EAST) + " " + card.valueAt(Direction.WEST) + "\n");
    }
    // Remove the last newline character
    return formattedCards.substring(0, formattedCards.length() - 1);
  }


  @Override
  public void render() {
    this.addToAppendable(this.toString());
  }

  @Override
  public void setFeature(Features feature) {
    // Does nothing as this view does not require features
  }

  @Override
  public void displayErrorMsg(String errMsg) {
    this.addToAppendable(errMsg);
  }

  // Does the same thing as error message in this implementation.
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

  private void addToAppendable(String msg) {
    try {
      this.appendable.append(msg);
    }
    catch (IOException e) {
      throw new IllegalStateException("Appendable has not been set for rendering");
    }
  }

}
