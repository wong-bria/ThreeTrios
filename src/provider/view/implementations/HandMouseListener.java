package provider.view.implementations;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;

/**
 * Mouse listener for hand panel interactions.
 */
class HandMouseListener extends MouseAdapter {
  private final HandPanel handPanel;
  private final ReadOnlyThreesTrioModel model;
  private final Player player;

  public HandMouseListener(HandPanel handPanel, ReadOnlyThreesTrioModel model, Player player) {
    this.handPanel = handPanel;
    this.model = model;
    this.player = player;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (!model.getCurrentPlayer().equals(player)) {
      return;
    }
    int index = e.getY() / handPanel.getCardHeight();
    if (index < model.getPlayerHand(player).size()) {
      if (index == handPanel.getSelectedCardIndex()) {
        handPanel.setSelectedCardIndex(-1);
        if (handPanel.getFeatures() != null) {
          handPanel.getFeatures().cardDeselected();
        }
        System.out.println("Deselected card " + index + " in " + player.getColor() + "'s hand");
      } else {
        handPanel.setSelectedCardIndex(index);
        if (handPanel.getFeatures() != null) {
          handPanel.getFeatures().cardSelected(index);
        }
        System.out.println("Selected card " + index + " in " + player.getColor() + "'s hand");
      }
      handPanel.repaint();
    }
  }
}
