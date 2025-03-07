package view;

import javax.swing.JButton;

import model.Card;

/**
 * Represents a panel that displays a player's hand of cards. This panel also allows highlighting
 * a selected card, clearing highlights, and setting features for interactions.
 */
public interface PlayerPanel<C extends Card> {
  /**
   * Refreshes the panel with new data based on the state of the model.
   */
  void updatePanel();

  /**
   * Highlights the given card to indicate it's selected. This method is called
   * when a card is clicked on by a player.
   * @param cardButton The JButton representing the card to highlight.
   */
  void highlightCard(JButton cardButton);

  /**
   * Clears the highlight from the previously selected card. This method is called
   * when the selected card is deselected or a new card is selected.
   */
  void clearHighlight();

  /**
   * Assigns features that defines the interactions within the player panel.
   * This allows the panel to use the provided features for handling
   * user actions.
   * @param features The features that this panel will have.
   */
  void setFeatures(Features features);
}
