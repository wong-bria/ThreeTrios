package provider.view.inter;

import java.awt.Dimension;

/**
 * Interface for representing the view of the Three Trios game. The view is responsible for
 * displaying the current state of the game and providing methods for refreshing the display.
 */
public interface ThreesTrioView {
  /**
   * Refreshes the view with current game state.
   */
  void refresh();

  /**
   * Gets the preferred size for this view.
   */
  Dimension getPreferredSize();

  /**
   * Add listener for player actions.

   * @param features the features listener to add
   */
  void addFeatures(PlayerActionFeatures features);

  /**
   * Sets the title of the view window.

   * @param title the title to set
   */
  void setTitle(String title);
}