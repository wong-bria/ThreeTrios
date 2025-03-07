package strategy;

import java.util.List;

import model.Card;
import model.Cells;
import model.Direction;
import model.ReadOnlyThreeTrioGameModel;

/**
 * A strategy where the corners of the grid are selected first if they are legal moves.
 * Also, consider which card is the hardest to flip in that corner.
 * This strategy evaluates each card for each corner coordinate by their "difficulty to flip."
 * Comparisons are made using the sum of each card's exposed values. The card with the greatest
 * sum and the corresponding corner are then selected and returned.
 * When calculating each card's exposed values, only include it if it's facing
 * an empty card cell.
 * Example: if a card's east value is facing a hole cell of card cell with a card in it,
 * then don't include it in calculations.
 * If no corner available, select uppermost, leftmost available position with card
 * index 0.
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class CornerStrategy<C extends Card> implements FailableThreeTrioStrategy<C> {
  private List<C> cards;
  private int greatestValue;
  private int indexOfGreatest;
  private Coordinate selectedCell;

  @Override
  public Tuple<Coordinate, Integer> selectTile(ReadOnlyThreeTrioGameModel<C> model, int playerIdx) {
    List<List<Cells>> grid = model.getGrid();
    this.cards = model.getPlayerHand(playerIdx);
    this.greatestValue = -1;
    this.indexOfGreatest = -1;
    this.selectedCell = null;
    int lastXIdx = grid.size() - 1;
    int lastYIdx = grid.get(lastXIdx).size() - 1;

    if (model.checkLegal(0, 0)) { // top left
      Coordinate topLeft = new Coordinate(0, 0);
      this.valueAtDir(topLeft, model);
    }
    if (model.checkLegal(0, lastYIdx)) { // top right
      Coordinate topRight = new Coordinate(0, lastYIdx);
      this.valueAtDir(topRight, model);
    }
    if (model.checkLegal(lastXIdx, 0)) { // bottom left
      Coordinate bottomLeft = new Coordinate(lastXIdx, 0);
      this.valueAtDir(bottomLeft, model);
    }
    if (model.checkLegal(lastXIdx, lastYIdx)) { // bottom right
      Coordinate bottomRight = new Coordinate(lastXIdx, lastYIdx);
      this.valueAtDir(bottomRight, model);
    }
    // Check if there are valid corners where both exposed sides are not facing
    // empty card cells. If none, find the uppermost, leftmost open position
    if (selectedCell == null) {
      if (model.checkLegal(0, 0)) {
        return new Tuple<>(new Coordinate(0, 0), 0);
      }
      if (model.checkLegal(0, lastYIdx)) {
        return new Tuple<>(new Coordinate(0, lastYIdx), 0);
      }
      if (model.checkLegal(lastXIdx, 0)) {
        return new Tuple<>(new Coordinate(lastXIdx, 0), 0);
      }
      if (model.checkLegal(lastXIdx, lastYIdx)) {
        return new Tuple<>(new Coordinate(lastXIdx, lastYIdx), 0);
      }
      for (int row = 0; row < model.getGridLength(); row++) {
        for (int col = 0; col < model.getGridWidth(); col++) {
          if (model.checkLegal(row, col)) {
            // Return the first valid position and card index 0
            return new Tuple<>(new Coordinate(row, col), 0);
          }
        }
      }
    }
    return new Tuple<>(this.selectedCell, this.indexOfGreatest);
  }

  /**
   * Selects the best card for the given corner by iterating through the player's deck.
   * If the sum of the exposed values is greater than the greatest sum so far, this Strategy's
   * fields are updated accordingly.
   * @param corner the coordinate of the corner selected.
   * @param model the model to be played on.
   */
  private void valueAtDir(Coordinate corner, ReadOnlyThreeTrioGameModel<C> model) {
    for (int index = 0; index < this.cards.size(); index += 1) {
      Card card = cards.get(index);
      int length = model.getGridLength() - 1;
      int width = model.getGridWidth() - 1;
      int value = 0;

      if (corner.getX() == 0 && corner.getY() == 0) {
        if (model.checkLegal(corner.getX() + 1, corner.getY())) {
          value = value + card.valueAt(Direction.SOUTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() + 1)) {
          value = value + card.valueAt(Direction.EAST).toNum();
        }
      } else if (corner.getX() == 0 && corner.getY() == width) {
        if (model.checkLegal(corner.getX() + 1, corner.getY())) {
          value = value + card.valueAt(Direction.SOUTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() - 1)) {
          value = value + card.valueAt(Direction.WEST).toNum();
        }
      } else if (corner.getX() == length && corner.getY() == 0) {
        if (model.checkLegal(corner.getX() - 1, corner.getY())) {
          value = value + card.valueAt(Direction.NORTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() + 1)) {
          value = value + card.valueAt(Direction.EAST).toNum();
        }
      } else if (corner.getX() == length && corner.getY() == width) {
        if (model.checkLegal(corner.getX() - 1, corner.getY())) {
          value = value + card.valueAt(Direction.NORTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() - 1)) {
          value = value + card.valueAt(Direction.WEST).toNum();
        }
      }

      if (value > this.greatestValue) {
        this.greatestValue = value;
        this.indexOfGreatest = index;
        this.selectedCell = corner;
      }
    }
  }
  
  
}
