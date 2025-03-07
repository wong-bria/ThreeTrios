package old.controller;

import model.Card;
import model.ThreeTrioGameModel;
import view.Features;
import view.GameView;

/**
 * An interface for controllers of ThreeTrioGame.
 * Controls the flow of user interaction and gameplay.
 * @param <C> Any class that extends the Model's Card class.
 */
public interface ThreeTrioGameController<C extends Card> extends Features {
  /**
   * Starts a new game of ThreeTrio given a model, grid name, and card name.
   * @param model the model used for this instance of ThreeTrio.
   * @param gridName the grid file used in this instance in path BoardConfig/...
   * @param cardName the card file used in this instance in path CardConfig/...
   */
  void startGame(ThreeTrioGameModel<C> model, String gridName, String cardName);

  // Make constructor take in a view and setFeature to this.
  void setView(GameView view);
}