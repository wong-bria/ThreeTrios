package view;

import java.util.Map;

import strategy.Coordinate;
import javax.swing.JOptionPane;

import provider.view.inter.ThreesTrioView;

/**
 * Adapter for our provider's view to our GUI view implementation.
 */
public class ProviderViewAdapter implements GameView {
  private final ThreesTrioView adaptee;
  private final int playerIndex;

  /**
   * Constructor.
   * @param adaptee the view to adapt, an instanceof our provider's view.
   * @param playerIndex the index of the player this view is for.
   */
  public ProviderViewAdapter(ThreesTrioView adaptee, int playerIndex) {
    this.adaptee = adaptee;
    this.playerIndex = playerIndex;
  }

  @Override
  public void setVisible(boolean isVisible) {
    // does nothing
  }

  @Override
  public void render() {
    this.adaptee.refresh();
  }

  @Override
  public void setFeature(Features feature) {
    this.adaptee.addFeatures(new FeatureAdapter(feature, this.playerIndex));
  }

  @Override
  public void displayErrorMsg(String errMsg) {
    JOptionPane.showMessageDialog(null, errMsg, errMsg, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayWinMsg(String winMsg) {
    JOptionPane.showMessageDialog(null, winMsg, winMsg, JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showHints() {
    // intentionally left blank. (don't need)
  }

  @Override
  public void setHints(Map<Coordinate, Integer> hintFlips) {
    // intentionally left blank. (don't need)
  }
}
