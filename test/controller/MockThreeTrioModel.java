package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Card;
import model.Cells;
import model.Colors;
import model.ModelFeatures;

/**
 * A mock version of three trio. Records inputs of method calls in the given appendable.
 * @param <C> A type of card.
 */
class MockThreeTrioModel<C extends Card> implements ModelFeatures<C> {
  private int playerIdx;
  private final List<ModelNotificationListener> listeners;
  private final Appendable appendable;

  /**
   * Default constructor. Sets the current appendable to the given one.
   * @param appendable the appendable to append data to.
   */
  MockThreeTrioModel(Appendable appendable) {
    this.playerIdx = -1; // starting at -1 so future addModelNot... will start at 0.
    this.appendable = appendable;
    this.listeners = new ArrayList<>();
  }

  @Override
  public int addModelNotificationListener(ModelNotificationListener listener) {
    this.playerIdx += 1;
    this.listeners.add(listener);
    return this.playerIdx;
  }

  @Override
  public void informStartListener() {
    // does nothing
  }

  // A publicly accessible method for this mock. Calls notifyPlayerTurn using the given parameter.
  public void notifyPlayerTurn(int playerIdx) {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyPlayerTurn(playerIdx);
    }
  }

  // A publicly accessible method for this mock. Calls notifyGameOver using the given parameters.
  public void notifyWin(int playerIdx, int score) {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyGameOver(playerIdx, score);
    }
  }

  // A publicly accessible method for this mock. Calls notifyGameStateUpdated.
  public void notifyGameStateUpdated() {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyGameStateUpdated();
    }
  }

  // A publicly accessible method for this mock. Calls notifyInvalidMove with the given parameters.
  public void notifyInvalidMove(String msg, int playerIdx) {
    for (ModelNotificationListener listener : this.listeners) {
      listener.notifyInvalidMove(msg, playerIdx);
    }
  }

  private void addToAppendable(String str) {
    try {

      this.appendable.append(str);
    } catch (IOException e) {
      throw new IllegalStateException("Appendable not writable");
    }
  }

  @Override
  public void startGame(List<List<Cells>> grid, List<C> deck) {
    // does nothing, as there is nothing to start
  }

  @Override
  public void playCard(int handIdx, int row, int col) {
    this.addToAppendable("Play card: " + handIdx + " at " + row + "," + col);
    this.notifyPlayerTurn(-1); // To stop while loop
  }

  @Override
  public void battle() {
    this.addToAppendable("Battle");
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public boolean didPlayerOneWin() {
    return false;
  }

  @Override
  public boolean didPlayerTwoWin() {
    return false;
  }

  @Override
  public List<C> getCurrentPlayerHand() {
    return List.of();
  }

  @Override
  public List<List<Cells>> getGrid() {
    return List.of();
  }

  @Override
  public int getTurn() {
    return 0;
  }

  @Override
  public int getScore(int playerIdx) {
    return 0;
  }

  @Override
  public int getFlipCount(int playerIdx, int handIdx, int row, int col) {
    return 0;
  }

  @Override
  public boolean checkLegal(int row, int col) {
    return true;
  }

  @Override
  public Colors getCardOwner(int row, int col) {
    return null;
  }

  @Override
  public Colors getPlayerColor(int playerIdx) {
    return Colors.Red;
  }

  @Override
  public List<C> getPlayerHand(int playerIdx) {
    return List.of();
  }

  @Override
  public Optional<Card> getContentAtCell(int row, int col) {
    return Optional.empty();
  }

  @Override
  public int getGridLength() {
    return 0;
  }

  @Override
  public int getGridWidth() {
    return 0;
  }

  @Override
  public int lastPlayedRow() {
    return 0;
  }

  @Override
  public int lastPlayedCol() {
    return 0;
  }
}
