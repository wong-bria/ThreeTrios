package strategy;

import model.Card;
import model.ReadOnlyThreeTrioGameModel;

import java.util.List;

/**
 * A strategy attempting to flip the most cards possible in one turn.
 * If no flips are possible (most flip of 0), then select the uppermost,
 * leftmost open position with card index 0.
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class FlipMostCardsStrategy<C extends Card> implements FailableThreeTrioStrategy<C> {
  /**
   * Selects the tile that would flip the most cards.
   * @param model the model to be played on.
   * @param playerIdx the index of the player this strategy is for.
   * @return A Tuple of Coordinate to Integer representing the coordinate and card index
   *         the user should play.
   */
  @Override
  public Tuple<Coordinate, Integer> selectTile(ReadOnlyThreeTrioGameModel<C> model, int playerIdx) {
    List<C> cards = model.getPlayerHand(playerIdx);
    int mostFlipped = 0;
    Coordinate coordinate = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE);
    int selectedCardIdx = -1;

    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col)) { // only check non hole cell/ card cell w/o card
          for (int cardIdx = 0; cardIdx < cards.size(); cardIdx++) {
            int flipCount = model.getFlipCount(playerIdx, cardIdx, row, col);
            if (flipCount > mostFlipped) {
              mostFlipped = flipCount;
              coordinate = new Coordinate(row, col);
              selectedCardIdx = cardIdx;
            } else if (flipCount == mostFlipped) {
              // Break tie with uppermost-leftmost coordinates
              if (row < coordinate.getX()
                      || (row == coordinate.getX() && col < coordinate.getY())) {
                coordinate = new Coordinate(row, col);
                selectedCardIdx = cardIdx;
              } else if (row == coordinate.getX()
                      && col == coordinate.getY() && cardIdx < selectedCardIdx) {
                // If the coordinates are the same, choose the card with the smallest index
                selectedCardIdx = cardIdx;
              }
            }
          }
        }
      }
    }
    // If no valid moves were found, select the uppermost-leftmost
    // open position and card at index 0
    if (coordinate.getX() == Integer.MAX_VALUE) {
      for (int row = 0; row < model.getGridLength(); row++) {
        for (int col = 0; col < model.getGridWidth(); col++) {
          if (model.checkLegal(row, col)) {
            return new Tuple<>(new Coordinate(row, col), 0);
          }
        }
      }
    }

    return new Tuple<>(coordinate, selectedCardIdx);
  }


}