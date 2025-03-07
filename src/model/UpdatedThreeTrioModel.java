package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import controller.ModelNotificationListener;

/**
 * An updated version of the ThreeTrioModel that supports notifications.
 * Pings listeners of any updates that occur while the game is running.
 * Extends ThreeTrioModel, as it is a ThreeTrioModel, relying on the previous
 * model's implementations.
 */
public class UpdatedThreeTrioModel implements ModelFeatures<PlayableCard> {
  private final List<ModelNotificationListener> listeners;
  private final ThreeTrioGameModel<PlayableCard> existing;

  /**
   * Constructor for UpdatedThreeTrioModel that calls constructor of parent
   * and creates an empty list of listeners.
   */
  public UpdatedThreeTrioModel() {
    this(new ThreeTrioModel());
  }

  public UpdatedThreeTrioModel(ThreeTrioGameModel<PlayableCard> existing) {
    this.existing = existing;
    this.listeners = new ArrayList<>();
  }

  /**
   * Adds a listener to the model that listens for game notifications.
   * @param listener The listener to add to the model's list of listeners.
   */
  @Override
  public int addModelNotificationListener(ModelNotificationListener listener) {
    this.listeners.add(listener);
    return listeners.size() - 1;
  }

  @Override
  public void startGame(List<List<Cells>> grid, List<PlayableCard> deck) {
    this.existing.startGame(grid, deck);
  }

  @Override
  public void informStartListener() {
    // Why did we move the notify call to here?
    // Because the provider's code does not run unless the game is already started.
    // So we need an alternative where rendering begins after all controllers are added.
    this.notifyGameStateUpdated();
    this.notifyPlayerTurn(0);
  }

  @Override
  public void battle() {
    this.existing.battle();
    this.updateGameState();
    this.notifyPlayerTurn(this.existing.getTurn());
  }


  protected void updateGameState() {
    this.notifyGameStateUpdated();

    if (this.isGameOver()) {
      if (this.didPlayerOneWin()) {
        this.notifyGameOver(0);
      }
      else if (this.didPlayerTwoWin()) {
        this.notifyGameOver(1);
      }
      else {
        this.notifyGameOver(-1);
      }
    }
  }

  // Called in model's start game and after a card is played
  protected void notifyPlayerTurn(int playerIndex) {
    for (ModelNotificationListener listener: this.listeners) {
      listener.notifyPlayerTurn(playerIndex);
    }
  }

  // Called after battles are completed
  private void notifyGameStateUpdated() {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyGameStateUpdated();
    }
  }

  // Only called in determineWinner() when the game is known to be over
  // -1 for draw, 0,1 for winner index
  private void notifyGameOver(int winnerIdx) {
    for (ModelNotificationListener listener : this.listeners) {
      int score;
      if (winnerIdx == -1) {
        score = this.getScore(0);
      }
      else {
        score = this.getScore(winnerIdx);
      }
      listener.notifyGameOver(winnerIdx, score);
    }
  }

  @Override
  public void playCard(int handIdx, int row, int col) {
    if (!checkLegal(row, col)) {
      String errMsg = "Invalid move!";
      Cells cell = this.getGrid().get(row).get(col);
      if (cell.isHole()) {
        errMsg += " The tile you are trying to place this card on is a hole!";
      }
      else {
        errMsg += " The tile you are trying to place this card on already has a card!";
      }
      notifyInvalidMove(errMsg);
    }
    else {
      this.existing.playCard(handIdx, row, col);
    }
  }

  private void notifyInvalidMove(String errMsg) {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyInvalidMove(errMsg, this.getTurn());
    }
  }

  @Override
  public boolean isGameOver() {
    return this.existing.isGameOver();
  }

  @Override
  public boolean didPlayerOneWin() {
    return this.existing.didPlayerOneWin();
  }

  @Override
  public boolean didPlayerTwoWin() {
    return this.existing.didPlayerTwoWin();
  }

  @Override
  public List<PlayableCard> getCurrentPlayerHand() {
    return this.existing.getCurrentPlayerHand();
  }

  @Override
  public List<List<Cells>> getGrid() {
    return this.existing.getGrid();
  }

  @Override
  public int getTurn() {
    return this.existing.getTurn();
  }

  @Override
  public int getScore(int playerIdx) {
    return this.existing.getScore(playerIdx);
  }

  @Override
  public int getFlipCount(int playerIdx, int handIdx, int row, int col) {
    return this.existing.getFlipCount(playerIdx, handIdx, row, col);
  }

  @Override
  public boolean checkLegal(int row, int col) {
    return this.existing.checkLegal(row, col);
  }

  @Override
  public Colors getCardOwner(int row, int col) {
    return this.existing.getCardOwner(row, col);
  }

  @Override
  public Colors getPlayerColor(int playerIdx) {
    return this.existing.getPlayerColor(playerIdx);
  }

  @Override
  public List<PlayableCard> getPlayerHand(int playerIdx) {
    return this.existing.getPlayerHand(playerIdx);
  }

  @Override
  public Optional<Card> getContentAtCell(int row, int col) {
    return this.existing.getContentAtCell(row, col);
  }

  @Override
  public int getGridLength() {
    return this.existing.getGridLength();
  }

  @Override
  public int getGridWidth() {
    return this.existing.getGridWidth();
  }

  @Override
  public int lastPlayedRow() {
    return this.existing.lastPlayedRow();
  }

  @Override
  public int lastPlayedCol() {
    return this.existing.lastPlayedCol();
  }
}
