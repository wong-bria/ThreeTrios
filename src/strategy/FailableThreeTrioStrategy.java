package strategy;

import model.Card;
import model.ReadOnlyThreeTrioGameModel;

/**
 * Represents a strategy a non-player would take when playing ThreeTrio.
 * The strategy can fail if no valid moves are available according to the strategy.
 * The uppermost, leftmost open position with card index 0 will be returned when
 * the strategy fails.
 */
public interface FailableThreeTrioStrategy<C extends Card> {

  /**
   * Selects a tile to play on based on the description of the strategy.
   * @param model the model to be played on.
   * @param playerIdx the index of the player this strategy is for.
   * @return a tuple containing the best coordinate and hand index for the given player.
   */
  Tuple<Coordinate, Integer> selectTile(ReadOnlyThreeTrioGameModel<C> model, int playerIdx);
}
