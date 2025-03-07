package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import model.Card;
import model.Colors;
import model.ModelFeatures;
import player.PlayerActions;
import strategy.Coordinate;
import strategy.Tuple;
import view.ThreeTrioGameView;

/**
 * Implementation for the controller interface. Represents a controller for a given player.
 * Uses notifications from ModelNotificationListener and Features to determine next moves.
 * Takes in user input from either the view (for Human players) or the PlayerActions field.
 * Updates the game state when its this player's turn.
 * Updates the view when the game state changes.
 *
 * @param <C> an implementation of a class extending Card.
 */
public class ThreeTrioPlayerControllerImpl<C extends Card> implements ThreeTrioGameController<C> {
  private final ModelFeatures<C> model;
  private final PlayerActions player;
  private final ThreeTrioGameView view;
  private final int playerIdx;
  private volatile boolean isTurn;
  private volatile Integer selectedCard;
  private volatile Coordinate selectedCell;
  private boolean hint;

  /**
   * Constructor for this controller.
   *
   * @param model  the model this controller changes the game state of.
   * @param player the player this controller is for.
   * @param view   the view that needs to be updated when the game state changes.
   */
  public ThreeTrioPlayerControllerImpl(ModelFeatures<C> model, PlayerActions player,
                                       ThreeTrioGameView view) {
    this.model = model;
    this.player = player;
    this.view = view;
    this.view.setFeature(this);
    this.isTurn = false;
    this.playerIdx = this.model.addModelNotificationListener(this);
    this.hint = false;
  }

  @Override
  public void handleCardClick(int playerIndex, int cardIndex) {
    this.selectedCell = null;
    if (playerIndex == this.playerIdx && !this.player.isMachine()) {
      if (!this.isTurn) {
        this.view.displayErrorMsg("It's not your turn!");
        return;
      }

      this.selectedCard = cardIndex;

      if (hint) {
        this.updateHints();
      }
    }
  }

  @Override
  public void handleCellClick(int row, int col) {
    if (!this.player.isMachine()) {
      if (!this.isTurn) {
        this.view.displayErrorMsg("It's not your turn!");
        return;
      } else if (this.selectedCard == null) {
        this.view.displayErrorMsg("Please select a card first.");
      }

      this.selectedCell = new Coordinate(row, col);
    }
  }

  @Override
  public void notifyPlayerTurn(int playerIndex) {
    this.isTurn = playerIndex == this.playerIdx;
    this.selectedCard = null;
    this.updateHints();

    // Waits for player move.
    while (this.isTurn && !this.model.isGameOver()) {
      // get info from machine
      if (this.player.isMachine()) {
        this.getMachineCoords();
      }

      // get info from handle methods or above
      if (this.selectedCell != null && this.selectedCard != null) {
        this.isTurn = false;
        // playCard may call notifyInvalidMove which sets this.isTurn back to true.
        this.model.playCard(this.selectedCard, this.selectedCell.getX(), this.selectedCell.getY());
        if (!this.isTurn) {
          this.model.battle(); // battle and transition to next turn if valid move
        }
        this.selectedCell = null; // clearing cell to prevent infinite loop
        this.selectedCard = null;
        this.updateHints();
      }
    }
  }

  private void getMachineCoords() {
    List<Tuple<Coordinate, Integer>> strategyOutput = this.player.playCard(this.playerIdx);
    for (Tuple<Coordinate, Integer> tuple : strategyOutput) {
      int row = tuple.getKey().getX();
      int col = tuple.getKey().getY();
      if (this.model.checkLegal(row, col)) {
        this.selectedCell = tuple.getKey();
        this.selectedCard = tuple.getValue();
        break;
      }
    }
  }


  @Override
  public void notifyGameOver(int winnerIndex, int winningScore) {
    this.isTurn = false;
    String message = "Game is over!";
    if (winnerIndex == -1) {
      message += " There is a tie between the players. Each player had a score of: "
              + winningScore;
    } else {
      Colors color = this.model.getPlayerColor(winnerIndex);
      message += " Player " + color + " won with a score of: " + winningScore;
    }

    this.view.displayWinMsg(message);
  }

  @Override
  public void notifyInvalidMove(String message, int playerIdx) {
    if (this.playerIdx == playerIdx) {
      this.view.displayErrorMsg(message);
      this.isTurn = !this.model.isGameOver();
    }
  }

  @Override
  public void notifyGameStateUpdated() {
    this.view.render();
  }

  @Override
  public void clearSelectedCard() {
    this.selectedCard = null;
  }

  @Override
  public void toggleHint(char letter) {
    if ((letter == 'q' && playerIdx % 2 == 0) || (letter == 'w' && playerIdx % 2 == 1)) {
      this.hint = !hint;
      this.view.showHints();
    }
  }

  private void updateHints() {
    if (this.selectedCard == null || !hint) {
      this.view.setHints(new HashMap<>());
      return;
    }

    Map<Coordinate, Integer> hintNums = new HashMap<>();

    List<Coordinate> playableCells = new ArrayList<>();
    for (int row = 0; row < this.model.getGridLength(); row++) {
      for (int col = 0; col < this.model.getGridWidth(); col++) {
        if (this.model.checkLegal(row, col)) {
          playableCells.add(new Coordinate(row, col));
        }
      }
    }
    List<Integer> numFlip = new ArrayList<>();
    for (Coordinate cell : playableCells) {
      int flips = this.model.getFlipCount(playerIdx, this.selectedCard, cell.getX(), cell.getY());
      numFlip.add(flips);
    }
    for (int i = 0; i < playableCells.size(); i++) {
      hintNums.put(playableCells.get(i), numFlip.get(i));
    }
    this.view.setHints(hintNums);
  }
}
