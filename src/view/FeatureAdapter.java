package view;

import provider.view.inter.PlayerActionFeatures;

/**
 * Object adapter for our Features implementation.
 * Supports the provider's PlayerActionFeatures interface.
 */
public class FeatureAdapter implements PlayerActionFeatures {
  private final Features feature;
  private final int playerIndex;

  /**
   * Constructor for the Feature adapter.
   * @param feature A Feature from our implementation.
   * @param playerIndex A player's index.
   */
  public FeatureAdapter(Features feature, int playerIndex) {
    this.feature = feature;
    this.playerIndex = playerIndex;
  }

  @Override
  public void cardSelected(int cardIndex) {
    this.feature.handleCardClick(playerIndex, cardIndex);
  }

  @Override
  public void cardDeselected() {
    this.feature.clearSelectedCard();
  }

  @Override
  public void positionSelected(int row, int col) {
    this.feature.handleCellClick(row, col);
  }
}
