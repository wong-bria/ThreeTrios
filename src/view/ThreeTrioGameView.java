package view;

import java.util.Map;

import strategy.Coordinate;

/**
 * Behaviors needed for a view of the Three Trio Game implementation
 * that transmits information to the user.
 */
public interface ThreeTrioGameView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   */
  void render();

  /**
   * Takes in features to pass to its components (the panels) so the view
   * can handle user actions and respond through the provided features.
   * @param feature The feature that the components will have.
   */
  void setFeature(Features feature);

  /**
   * Takes in an error message and displays the message.
   * The way the message is displayed depends on the implementation.
   * @param errMsg the error message to be displayed.
   */
  void displayErrorMsg(String errMsg);

  /**
   * Takes in a win message and displays the message.
   * Uses the values to create a win message.
   * The way the message is displayed depends on the implementation.
   * @param winMsg the message containing information on the winner and score.
   */
  void displayWinMsg(String winMsg);

  void showHints();

  void setHints(Map<Coordinate, Integer> hintFlips);
}
