package view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Colors;
import model.ReadOnlyThreeTrioGameModel;

/**
 * Represents the panel displaying a player's hand of cards in a Three Trio game.
 * This panel organizes the cards vertically and allows for highlighting selected
 * cards and handing user interactions with cards.
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class PlayerHandPanel<C extends Card> extends JPanel implements PlayerPanel<C> {
  private final Colors playerColor;
  private final ReadOnlyThreeTrioGameModel<C> model;
  private final List<JButton> cardButtons;
  private final List<C> cards;
  private final int playerIndex; // 0 index based
  private Features features;
  // The parent view used to prevent a card in each hand being highlighted,
  // so manages card highlight
  private boolean hasInitialized;
  private JButton previouslyHighlighted;

  /**
   * Constructor for PlayerHandPanel for a specified player in the game.
   * The panel displays the player's hand of cards vertically.
   * @param model The game model which provides the player's hand and game state.
   * @param playerIdx The index of the player.
   */
  public PlayerHandPanel(ReadOnlyThreeTrioGameModel<C> model, int playerIdx) {
    this.model = model;
    this.cardButtons = new ArrayList<>();
    this.cards = new ArrayList<>();
    this.playerIndex = playerIdx;
    this.hasInitialized = false;
    this.previouslyHighlighted = null;
    this.playerColor = this.model.getPlayerColor(playerIdx);
  }

  /**
   * Builds the player's hand by adding buttons for each card.
   */
  public void initializePanel() {
    super.setVisible(true);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    List<C> playerHand = this.model.getPlayerHand(playerIndex);

    for (C card : playerHand) {
      this.cards.add(card);

      CardToJButton cardButton = new CardToJButton(card, playerColor);
      cardButton.setBackground(playerColor.toColor());
      ActionListener listener = e -> handleCardClick(cardButton);
      cardButton.addActionListener(listener);
      this.cardButtons.add(cardButton);
      cardButton.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
      this.add(cardButton);
    }
  }

  /**
   * Updates the panel by removing cards that are no long part of the player's hand.
   */
  @Override
  public void updatePanel() {
    super.setVisible(false);
    if (!this.hasInitialized) {
      this.initializePanel();
      this.hasInitialized = true;
    }

    int removalIdx = -1;
    List<C> currentCardState = model.getPlayerHand(playerIndex);
    for (int index = 0; index < currentCardState.size(); index++) {
      C cardInModel = currentCardState.get(index);
      C cardHere = this.cards.get(index);
      if (!cardInModel.equals(cardHere)) {
        removalIdx = index;
        break;
      }
    }

    if (removalIdx == -1 && this.cards.size() > currentCardState.size()) {
      removalIdx = this.cards.size() - 1;
    }

    if (removalIdx != -1) {
      JButton removedButton = this.cardButtons.remove(removalIdx);
      this.remove(removedButton);
      this.cards.remove(removalIdx);
    }
    super.setVisible(true);
  }

  @Override
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Highlights the clicked card.
   * @param cardButton The button representing the clicked card.
   */
  private void handleCardClick(JButton cardButton) {
    if (this.model.getTurn() == this.playerIndex) {
      int indexOf = this.cardButtons.indexOf(cardButton);
      this.features.handleCardClick(playerIndex, indexOf);
      this.toggleHighlight(cardButton);
    }
  }

  @Override
  public void highlightCard(JButton cardButton) {
    cardButton.setBackground(Color.WHITE);
  }

  @Override
  public void clearHighlight() {
    for (JButton button : cardButtons) {
      button.setBackground(playerColor.toColor());
    }
    this.features.clearSelectedCard();
  }

  private void toggleHighlight(JButton toHighlight) {
    if (previouslyHighlighted == null || previouslyHighlighted != toHighlight) {
      if (previouslyHighlighted != null) {
        previouslyHighlighted.setBackground(playerColor.toColor());
      }
      previouslyHighlighted = toHighlight;
      this.highlightCard(previouslyHighlighted);
    }
    else {
      this.clearHighlight();
      previouslyHighlighted = null;
      toHighlight.setBackground(playerColor.toColor());
    }
  }

  @Override
  protected void paintComponent(Graphics graphic) {
    super.paintComponent(graphic);
    Graphics2D g2d = (Graphics2D) graphic.create();
  }
}
