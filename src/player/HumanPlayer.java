package player;

import java.util.List;

import strategy.Coordinate;
import strategy.Tuple;

/**
 * A representation for a HumanPlayer and the actions they can take.
 * Since all human actions are derived from the view, this class serves as a placeholder.
 */
public class HumanPlayer implements PlayerActions {

  /*
   * Constructor. Requires no input, as there is nothing to instantiate.
   */
  public HumanPlayer() {
    // Nothing because there is nothing to instantiate.
  }

  @Override
  public List<Tuple<Coordinate, Integer>> playCard(int playerIdx) {
    throw new UnsupportedOperationException("Human players rely on the view!");
  }

  @Override
  public boolean isMachine() {
    return false;
  }
}
