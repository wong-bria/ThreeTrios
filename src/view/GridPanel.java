package view;

import java.util.Map;

import strategy.Coordinate;

/**
 * Represents a panel that displays and manages a grid for the game. The grid panel
 * handles interactions with the cells and updates to match the model's current state.
 */
public interface GridPanel {
  /**
   * Refreshes the grid to match the model's current state.
   */
  void updateAllCells();

  /**
   * Assigns features that defines the interactions within the grid panel.
   * This allows the panel to use the provided features for handling
   * user actions.
   *
   * @param features The features that this panel will have.
   */
  void setFeatures(Features features);

  /**
   * Toggles between having hints on or off.
   */
  void toggleHints();

  /**
   * Sets the hint values: The number of flips and the coordinate.
   * @param hintFlips A map of each cell's coordinate that is playable and
   *                  the number of flips possible in each of those coordinates.
   */
  void setHints(Map<Coordinate, Integer> hintFlips);
}
