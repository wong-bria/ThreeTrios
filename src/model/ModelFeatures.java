package model;

import controller.ModelNotificationListener;

/**
 * Represents a new Model type that supports notification features.
 * Allows the model to ping any listeners (controllers) of game state updates.
 * @param <C> the type of card being used in this model.
 */
public interface ModelFeatures<C extends Card> extends ThreeTrioGameModel<C> {
  /**
   * Adds a listener to this model. The listener will be notified of any updates.
   * @param listener the listener being added.
   * @return the index of the player this listener will represent.
   */
  int addModelNotificationListener(ModelNotificationListener listener);

  /**
   * Informs listeners that the game has started.
   * Previously hidden in startGame, but de-coupled to support views that require
   * models to have already started to work.
   */
  void informStartListener();
}
