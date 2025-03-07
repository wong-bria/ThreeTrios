package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import controller.ModelNotificationListener;
import provider.model.inter.Card;
import provider.model.inter.Cell;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;

/**
 * Two-way adapter for our model implementation and the provider's implementation.
 * Takes in a ModelFeatures object and adapts it for both interfaces.
 * Delegates all previous ModelFeatures methods to the object passed in.
 */
public class OurReadToProviderReadADT implements ReadOnlyThreesTrioModel,
        ModelFeatures<PlayableCard> {
  private final ModelFeatures<PlayableCard> adaptee;
  private final List<Player> players;

  /**
   * Constructor for temporary models without Players.
   * Since a Player needs a Model to refer to, an intermediary model must
   * be created without Players.
   * @param adaptee A ModelFeatures object.
   */
  public OurReadToProviderReadADT(ModelFeatures<PlayableCard> adaptee) {
    Objects.requireNonNull(adaptee);
    this.adaptee = adaptee;
    this.players = new ArrayList<>();
  }

  /**
   * Default constructor for a readonly model adapter.
   * @param adaptee A ModelFeatures object.
   * @param red Red Player.
   * @param blue Blue Player.
   */
  public OurReadToProviderReadADT(ModelFeatures<PlayableCard> adaptee, Player red, Player blue) {
    Objects.requireNonNull(adaptee);
    this.adaptee = adaptee;
    this.players = new ArrayList<>();
    this.players.add(red);
    this.players.add(blue);
  }

  @Override
  public int getRows() {
    return adaptee.getGridLength();
  }

  @Override
  public int getCols() {
    return adaptee.getGridWidth();
  }

  @Override
  public Cell getCell(int row, int col) {
    Cells ourCell = adaptee.getGrid().get(row).get(col);
    return new OurCellsToProviderCellADT(ourCell);
  }

  @Override
  public int getCardCellCount() {
    return adaptee.getScore(0) - adaptee.getPlayerHand(0).size()
            + adaptee.getScore(1) - adaptee.getPlayerHand(1).size();
  }

  @Override
  public Player getCurrentPlayer() {
    return this.players.get(this.getTurn());
  }

  @Override
  public Player getRedPlayer() {
    return this.players.get(0);
  }

  @Override
  public Player getBluePlayer() {
    return this.players.get(1);
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    Objects.requireNonNull(player);
    int playerIdx;
    String firstPlayerColor = adaptee.getPlayerColor(0).toString();
    String givenPlayerColor = player.getColor();
    if (firstPlayerColor.equalsIgnoreCase(givenPlayerColor)) {
      playerIdx = 0;
    } else {
      playerIdx = 1;
    }

    List<Card> adaptedCards = new ArrayList<>();
    for (PlayableCard card : adaptee.getPlayerHand(playerIdx)) {
      adaptedCards.add(new OurPlayableCardToProviderCardADT(card));
    }
    return adaptedCards;
  }

  @Override
  public List<PlayableCard> getPlayerHand(int playerIdx) {
    return adaptee.getPlayerHand(playerIdx);
  }

  @Override
  public boolean isGameOver() {
    return adaptee.isGameOver();
  }

  @Override
  public boolean didPlayerOneWin() {
    return adaptee.didPlayerOneWin();
  }

  @Override
  public boolean didPlayerTwoWin() {
    return adaptee.didPlayerTwoWin();
  }

  @Override
  public List<PlayableCard> getCurrentPlayerHand() {
    return adaptee.getCurrentPlayerHand();
  }

  @Override
  public List<List<Cells>> getGrid() {
    return adaptee.getGrid();
  }

  @Override
  public int getTurn() {
    return adaptee.getTurn();
  }

  @Override
  public int getScore(int playerIdx) {
    return adaptee.getScore(playerIdx);
  }

  @Override
  public int getScore(Player player) {
    int playerIdx = -1;
    if (adaptee.getPlayerColor(0).toString().equalsIgnoreCase(
            player.getColor().substring(0, 1))) {
      playerIdx = 0;
    } else {
      playerIdx = 1;
    }
    return adaptee.getScore(playerIdx);
  }

  @Override
  public int getFlipCount(int playerIdx, int handIdx, int row, int col) {
    return adaptee.getFlipCount(playerIdx, handIdx, row, col);
  }

  @Override
  public boolean checkLegal(int row, int col) {
    return adaptee.checkLegal(row, col);
  }

  @Override
  public Colors getCardOwner(int row, int col) {
    return adaptee.getCardOwner(row, col);
  }

  @Override
  public Colors getPlayerColor(int playerIdx) {
    return adaptee.getPlayerColor(playerIdx);
  }


  @Override
  public Optional<model.Card> getContentAtCell(int row, int col) {
    return adaptee.getContentAtCell(row, col);
  }

  @Override
  public int getGridLength() {
    return adaptee.getGridLength();
  }

  @Override
  public int getGridWidth() {
    return adaptee.getGridWidth();
  }

  @Override
  public int lastPlayedRow() {
    return adaptee.lastPlayedRow();
  }

  @Override
  public int lastPlayedCol() {
    return adaptee.lastPlayedCol();
  }

  @Override
  public Player getWinner() {
    return null;
  }



  @Override
  public boolean isValidPosition(int row, int col) {
    return adaptee.checkLegal(row, col);
  }

  @Override
  public boolean canPlaceCard(int row, int col) {
    return adaptee.checkLegal(row, col);
  }

  @Override
  public int calculatePotentialFlips(Card card, int row, int col) {
    int playerIdx = -1;
    int cardIdx = -1;

    for (int i = 0; i < adaptee.getPlayerHand(0).size(); i++) {
      if (adaptee.getPlayerHand(0).get(i).equals(card)) {
        playerIdx = 0;
        cardIdx = i;
      }
    }

    for (int i = 0; i < adaptee.getPlayerHand(1).size(); i++) {
      if (adaptee.getPlayerHand(1).get(i).equals(card)) {
        playerIdx = 1;
        cardIdx = i;
      }
    }
    return adaptee.getFlipCount(playerIdx, cardIdx, row, col);
  }

  @Override
  public int addModelNotificationListener(ModelNotificationListener listener) {
    return adaptee.addModelNotificationListener(listener);
  }

  @Override
  public void startGame(List<List<Cells>> grid, List<PlayableCard> deck) {
    adaptee.startGame(grid, deck);
  }

  @Override
  public void playCard(int handIdx, int row, int col) {
    adaptee.playCard(handIdx, row, col);
  }

  @Override
  public void battle() {
    adaptee.battle();
  }

  @Override
  public void informStartListener() {
    adaptee.informStartListener();
  }
}
