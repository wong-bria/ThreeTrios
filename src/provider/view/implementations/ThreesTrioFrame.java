package provider.view.implementations;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import provider.view.inter.PlayerActionFeatures;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;
import provider.view.inter.ThreesTrioView;

/**
 * The main frame for displaying the Three Trios game. This class is responsible for setting up
 * the graphical user interface (GUI) to interact with the game. It extends JFrame and implements
 * the ThreesTrioView interface.
 */
public class ThreesTrioFrame extends JFrame implements ThreesTrioView {
  private final ReadOnlyThreesTrioModel model;
  private final GridPanel gridPanel;
  private final HandPanel playerHandPanel;
  private final HandPanel redHandPanel;
  private final HandPanel blueHandPanel;
  private final Player player;
  private PlayerActionFeatures features;

  /**
   * Constructor for initializing the game frame with the given game model..
   *
   * @param model The game model to be displayed in the frame.
   */
  public ThreesTrioFrame(ReadOnlyThreesTrioModel model, Player player) {
    this.model = model;
    this.player = player;

    gridPanel = new GridPanel(model);
    redHandPanel = new HandPanel(model, model.getRedPlayer());
    blueHandPanel = new HandPanel(model, model.getBluePlayer());

    playerHandPanel = new HandPanel(model, player);

    setLayout(new BorderLayout(0, 0));
    add(gridPanel, BorderLayout.CENTER);
    add(redHandPanel, BorderLayout.WEST);
    add(blueHandPanel, BorderLayout.EAST);

    setPreferredSize(new Dimension(768, 768));
    pack();
    setLocationRelativeTo(null);
    this.setVisible(true); // Provider's view isn't visible unless this is called.
  }

  @Override
  public void refresh() {
    gridPanel.repaint();
    redHandPanel.repaint();
    blueHandPanel.repaint();
    playerHandPanel.repaint();
    updateTitle();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(700, 768); // Default window size
  }

  /**
   * Updates the window title to indicate whose turn it is.
   */
  private void updateTitle() {
    String status;
    if (model.getCurrentPlayer().equals(player)) {
      status = "Your Turn";
    } else {
      status = "Waiting...";
    }
    setTitle(player.getColor() + "'s View - " + status);
  }

  @Override
  public void addFeatures(PlayerActionFeatures features) {
    gridPanel.addFeatures(features);
    if (player.equals(model.getRedPlayer())) {
      redHandPanel.addFeatures(features);
    } else {
      blueHandPanel.addFeatures(features);
    }
  }

  @Override
  public void setTitle(String title) {
    super.setTitle(title);
  }

  /**
   * Resets the card selection for both the red and blue hand panels.
   * This sets the selected card index to -1, indicating no card is selected.
   */
  public void resetHandPanelSelection() {
    redHandPanel.setSelectedCardIndex(-1);
    blueHandPanel.setSelectedCardIndex(-1);
    playerHandPanel.setSelectedCardIndex(-1);
  }
}
