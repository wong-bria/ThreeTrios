package player;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.ReadOnlyThreeTrioGameModel;
import strategy.Coordinate;
import strategy.FailableThreeTrioStrategy;
import strategy.InfailableThreeTrioStrategy;
import strategy.Tuple;

/**
 * Represents a machine player and the actions they can take.
 * Is constructed with one of Infailable or Failable strategies.
 * Outputs are reliant on the strategy given and the model to run the strategy on.
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class MachinePlayer<C extends Card> implements PlayerActions {
  private final ReadOnlyThreeTrioGameModel<C> model;
  private final InfailableThreeTrioStrategy<C> infailable;
  private final FailableThreeTrioStrategy<C> failable;

  /**
   * Constructor that takes in an infailable strategy.
   * @param model the model to run the strategy on.
   * @param infailable the strategy to run.
   */
  public MachinePlayer(ReadOnlyThreeTrioGameModel<C> model,
                       InfailableThreeTrioStrategy<C> infailable) {
    this.model = model;
    this.infailable = infailable;
    this.failable = null;
  }

  /**
   * Constructor that takes in an failable strategy.
   * @param model the model to run the strategy on.
   * @param failable the strategy to run.
   */
  public MachinePlayer(ReadOnlyThreeTrioGameModel<C> model,
                       FailableThreeTrioStrategy<C> failable) {
    this.model = model;
    this.infailable = null;
    this.failable = failable;
  }

  @Override
  public List<Tuple<Coordinate, Integer>> playCard(int playerIdx) {
    if (failable != null) {
      List<Tuple<Coordinate, Integer>> moves = new ArrayList<>();
      Tuple<Coordinate, Integer> output = this.failable.selectTile(this.model, playerIdx);
      moves.add(output);
      return moves;
    }
    else {
      return this.infailable.selectTile(this.model, playerIdx);
    }
  }

  @Override
  public boolean isMachine() {
    return true;
  }
}
