package player;

import java.util.List;

import strategy.Coordinate;
import strategy.Tuple;

/**
 * Interface representing the different actions a player (human or robot)
 * can take during a game.
 */
public interface PlayerActions {
  /**
   * Communicates a list of possible moves this player is intended to make.
   * @param playerIdx the index of the player this action is representing.
   * @return A list of possibilities for the move to play..
   * @throws UnsupportedOperationException when the player's decision is communicated via the view.
   */
  List<Tuple<Coordinate, Integer>> playCard(int playerIdx);

  /**
   * Communicates whether these actions are for a machine player.
   * @return true if these actions are for a machine player.
   */
  boolean isMachine();
}
