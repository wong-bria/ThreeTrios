package provider.view.implementations;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;

import javax.swing.JPanel;
import java.util.List;

import provider.view.inter.PlayerActionFeatures;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;
import provider.model.inter.Card;

/**
 * A panel for displaying a player's hand in the Threes Trio game.
 * Handles rendering the player's cards and handles actions.
 */
class HandPanel extends JPanel {
  private final ReadOnlyThreesTrioModel model;
  private final Player player;
  private int selectedCardIndex = -1;
  private static final int minCardHeight = 60;
  private PlayerActionFeatures features;

  /**
   * Constructs a HandPanel for the specified player and game model.
   *
   * @param model the game model providing the data for the hand
   * @param player the player whose hand is displayed in this panel
   */
  public HandPanel(ReadOnlyThreesTrioModel model, Player player) {
    this.model = model;
    this.player = player;
    addMouseListener(new HandMouseListener(this, model, player));
  }

  public boolean canInteract(Player currentPlayer) {
    return player.equals(currentPlayer) && player.equals(model.getCurrentPlayer());
  }

  @Override
  public Dimension getPreferredSize() {
    int parentHeight;
    if (getParent() != null) {
      parentHeight = getParent().getHeight();
    } else {
      parentHeight = minCardHeight * model.getPlayerHand(player).size();
    }
    int cardHeight = parentHeight / model.getPlayerHand(player).size();

    return new Dimension(cardHeight, parentHeight);
  }

  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    List<Card> hand = model.getPlayerHand(player);

    int cardHeight = getHeight() / hand.size();
    int cardWidth = getWidth();

    for (int i = 0; i < hand.size(); i++) {
      int y = i * cardHeight;
      CardUtil.drawHandCard(g2d, hand.get(i), 0, y, player, cardWidth, cardHeight);
    }

    if (selectedCardIndex >= 0 && selectedCardIndex < hand.size()) {
      int y = selectedCardIndex * cardHeight;
      g2d.setColor(Color.GRAY);
      g2d.setStroke(new BasicStroke(3));
      g2d.drawRect(0, y, cardWidth, cardHeight);
    }
  }

  /**
   * Calculates the height of an individual card based on the total component height
   * and the number of cards in the player's hand.
   *
   * @return the height of a single card
   */
  public int getCardHeight() {
    return getHeight() / model.getPlayerHand(player).size();
  }

  /**
   * Retrieves the index of the currently selected card.
   *
   * @return the index of the selected card
   */
  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  /**
   * Sets the index of the currently selected card.
   *
   * @param selectedCardIndex the index to set as the selected card
   */
  public void setSelectedCardIndex(int selectedCardIndex) {
    this.selectedCardIndex = selectedCardIndex;
  }

  /**
   * Retrieves the features associated with the player's action.
   *
   * @return the PlayerActionFeatures object representing the player's action features
   */
  public PlayerActionFeatures getFeatures() {
    return features;
  }

  /**
   * Checks if the current hand belongs to the player whose turn it is.
   *
   * @return true if the player's hand matches the current player, false otherwise
   */
  public boolean isOwnHand() {
    return player.equals(model.getCurrentPlayer());
  }

  /**
   * Assigns the specified features to the player's action.
   *
   * @param features the PlayerActionFeatures object containing the features to be assigned
   */
  public void addFeatures(PlayerActionFeatures features) {
    this.features = features;
  }
}