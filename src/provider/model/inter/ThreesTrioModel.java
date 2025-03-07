package provider.model.inter;

import java.util.List;

import provider.view.inter.ModelStatusFeatures;

/**
 * Mutable interface for the Three Trios game model.
 * Extends the read-only interface and adds methods that modify game state.
 */
public interface ThreesTrioModel extends ReadOnlyThreesTrioModel {
  /**
   * Makes a move in the game.

   * @param card card to play
   * @param row row to place card
   * @param col column to place card
   * @throws IllegalArgumentException if move is invalid
   * @throws IllegalStateException if game is over
   */
  void makeMove(Card card, int row, int col);

  /**
   * Switches to the next player.
   */
  void switchPlayer();

  /**
   * Deals a list of cards to the players in the game.
   *
   * @param cards A list of cards to be dealt to the players.
   */
  void dealCards(List<Card> cards);

  /**
   * Places a card on the grid at the specified position, with the given owner.
   *
   * @param row The row where the card will be placed.
   * @param col The column where the card will be placed.
   * @param card The card to be placed.
   * @param owner The player who owns the card.
   */
  void placeCard(int row, int col, Card card, Player owner);

  /**
   * Flips a card at a given position, changing the ownership to a new player.
   *
   * @param row The row of the card to flip.
   * @param col The column of the card to flip.
   * @param newOwner The new player who will own the card after flipping.
   */
  void flipCard(int row, int col, Player newOwner);

  /**
   * Creates a copy of the current game state.

   * @return new game model with copied state
   */
  ThreesTrioModel copy();

  /**
   * Checks if a move is valid.

   * @param card card to play
   * @param row row to place card
   * @param col column to place card
   * @return true if move is valid, false otherwise
   */
  boolean isValidMove(Card card, int row, int col);

  /**
   * Add a listener for model status changes.

   * @param listener the listener to add
   */
  void addModelStatusListener(ModelStatusFeatures listener);

  /**
   * Remove a listener for model status changes.

   * @param listener the listener to remove
   */
  void removeModelStatusListener(ModelStatusFeatures listener);

  /**
   * Starts the game and notifies listeners of initial state.
   */
  void startGame();

  /**
   * Executes a move for the computer player based on the set strategy.
   *
   * @throws IllegalStateException if no strategy has been set for the computer player
   */
  void makeComputerMove();
}