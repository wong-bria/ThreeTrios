package strategy;

import java.util.List;

import model.Card;
import model.ReadOnlyThreeTrioGameModel;

/**
 * Represents a strategy a non-player would take when playing ThreeTrio.
 * The strategy always returns a non-empty List of the quantity of Tuple of Coordinate to Integer.
 */
public interface InfailableThreeTrioStrategy<C extends Card> {
  /**
   * Selects a tile to play on. The selection criteria varies by strategy.
   * @param model the model to be played on.
   * @param playerIdx the player this strategy is for.
   * @return a list of tuples containing the best coordinate(s) and card indices.
   */
  List<Tuple<Coordinate, Integer>> selectTile(ReadOnlyThreeTrioGameModel<C> model, int playerIdx);
}