package provider.view.inter;

import provider.model.inter.Player;

/**
 * Interface for model status notifications. Notifies listeners of game state changes.
 */
public interface ModelStatusFeatures {
  /**
   * Called when it's a player's turn.

   * @param player the player whose turn it is
   */
  void turnChanged(Player player);

  /**
   * Called when the game is over.

   * @param winner the winning player
   * @param score the winning score
   */
  void gameOver(Player winner, int score);
}