package view;

/**
 * Represents the different behaviors a frame in the Three Trio game needs:
 * Displaying the game, toggling highlight between cards in different players' hand,
 * and passing along features to its components (the panels).
 */
public interface GameView extends ThreeTrioGameView {
  /**
   * Make the view visible.
   * @param isVisible True if you want view to be visible. False otherwise.
   */
  void setVisible(boolean isVisible);
}
