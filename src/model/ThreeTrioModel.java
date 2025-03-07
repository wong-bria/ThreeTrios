package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the states and behaviors of a game of ThreeTrioGame.
 * This model maintains the grid, players, deck to be distributed to players, game turn,
 * maximum hand size, last played coordinates, and the game state.
 * After a user plays a card, their hand slides down left.
 * Invariant: this.turn is always within the index bounds of the number of players. (0 or 1).
 */
public class ThreeTrioModel implements ThreeTrioGameModel<PlayableCard> {
  /*
  grid is 0 index based. 0,0 is the top left. As row increases--0 to 1 to 2...--row goes from top
  to bottom. As column increases--0 to 1 to 2...--column goes from left to right.
   */
  private final List<Colors> playerColors;
  protected final List<List<Cells>> grid;
  protected final List<ModelPlayer<PlayableCard>> players; // 0 index based where 0 is player 1.
  private List<PlayableCard> deck; // deck is 0 index based
  protected int turn; // 0 for player 1, 1 for player 2
  private int maxHandSize; // (Number of CardCell + 1) / 2
  protected Integer playedRow; // 0 index based
  protected Integer playedCol; // 0 index based
  private GameState gamestate;

  private enum GameState {
    NotStarted,
    Ongoing,
    Tie,
    PlayerOneWin,
    PlayerTwoWin
  }

  /**
   * Constructor to initialize a Three Trio Game but does not start it.
   */
  public ThreeTrioModel() {
    this.playedRow = -1;
    this.playedCol = -1;
    this.players = new ArrayList<>();
    this.deck = new ArrayList<>();
    this.grid = new ArrayList<>();
    this.gamestate = GameState.NotStarted;
    this.turn = 0;

    this.playerColors = new ArrayList<>();
    this.playerColors.add(Colors.Red);
    this.playerColors.add(Colors.Blue);
  }

  @Override
  public void startGame(List<List<Cells>> grid, List<PlayableCard> deck) {
    this.throwIfStarted();
    this.throwIfGameOver();
    this.gamestate = GameState.Ongoing;

    this.initializePlayers();
    for (List<Cells> rows : grid) {
      List<Cells> toAdd = new ArrayList<>();
      for (Cells cell : rows) {
        toAdd.add(cell.copyOf());
      }
      this.grid.add(toAdd);
    }
    this.deck = new ArrayList<>(deck);
    this.maxHandSize = (countCardCell() + 1) / 2;

    // N = count number of card cells on board. Then check if there is at least N+1 cards in deck
    // Fill each player hand with (N+1) / 2 cards.
    if (this.deck.size() < countCardCell() + 1) {
      throw new IllegalArgumentException("If there is N card cells on the board, there"
              + "must be at least N+1 cards in deck.");
    }

    // Distributes cards evenly to both hands
    this.distributeCards();

    // link the neighbors so each cell knows its neighbors
    this.linkNeighbors(this.grid);
  }

  // counts the number of card cells on board
  private int countCardCell() {
    int count = 0;
    for (List<Cells> row : this.grid) {
      for (Cells cell : row) {
        if (!cell.isHole()) {
          count++;
        }
      }
    }
    return count;
  }

  // Assuming only 2 player support.
  // Update later if more than 2 players are needed or if different players are added (ai).
  private void initializePlayers() {
    for (Colors playerColor : this.playerColors) {
      ModelPlayer<PlayableCard> newPlayer = new ModelPlayerImpl(playerColor);
      this.players.add(newPlayer);
    }
  }

  /**
   * Alternates the distribution of cards from player 1 then player 2 until both players
   * have exactly (N+1) / 2 cards in their hand. N = number of CardCell in grid.
   */
  private void distributeCards() {
    while (this.deck.size() > 1) {
      draw(0);
      draw(1);

      if (this.players.get(0).getHand().size() == this.maxHandSize
              && this.players.get(1).getHand().size() == this.maxHandSize) {
        return;
      }
    }
  }

  private void draw(int givenPlayerIdx) {
    ModelPlayer<PlayableCard> player = this.players.get(givenPlayerIdx);
    player.addCardToHand(this.deck.remove(0));
  }

  /**
   * Helper method to allow each cell to know its neighbors.
   */
  private void linkNeighbors(List<List<Cells>> grid) {
    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        Cells cell = grid.get(row).get(col);

        // Above neighbor
        if (this.isValid(row - 1, col)) {
          cell.addNeighbor(grid.get(row - 1).get(col), Direction.NORTH);
        }

        // Below neighbor
        if (this.isValid(row + 1, col)) {
          cell.addNeighbor(grid.get(row + 1).get(col), Direction.SOUTH);
        }

        // Left neighbor
        if (this.isValid(row, col - 1)) {
          cell.addNeighbor(grid.get(row).get(col - 1), Direction.WEST);
        }

        // Right neighbor
        if (this.isValid(row, col + 1)) {
          cell.addNeighbor(grid.get(row).get(col + 1), Direction.EAST);
        }
      }
    }
  }

  /**
   * Helper method to check if a given row and col is somewhere in the grid.
   *
   * @param row The row of the grid.
   * @param col The column of the grid.
   * @return true if the given row and col is in the grid. False otherwise.
   */
  private boolean isValid(int row, int col) {
    return row >= 0 && row < this.grid.size()
            && col >= 0 && col < this.grid.get(row).size();
  }

  @Override
  public void playCard(int handIdx, int row, int col) {
    this.throwIfNotStarted();
    if (!this.isValid(row, col)) {
      throw new IllegalArgumentException("Invalid row or column given.");
    }
    if (this.turn == 0 && (handIdx < 0 || handIdx >= this.players.get(0).getHand().size())) {
      throw new IllegalArgumentException("Invalid hand index");
    }
    if (this.turn == 1 && (handIdx < 0 || handIdx >= this.players.get(1).getHand().size())) {
      throw new IllegalArgumentException("Invalid hand index");
    }

    PlayableCard cardFromHand;
    if (this.turn == 0) {
      cardFromHand = this.players.get(0).getHand().get(handIdx);
    } else {
      cardFromHand = this.players.get(1).getHand().get(handIdx);
    }

    // if the condition results in false, then body will not execute and card will be placed on grid
    if (!this.grid.get(row).get(col).putCard(cardFromHand)) {
      throw new IllegalArgumentException("cell already has a card or is a hole");
    }

    this.playedRow = row;
    this.playedCol = col;

    // Remove card after playing
    if (this.turn == 0) {
      this.players.get(0).removeCardFromHand(cardFromHand);
    } else {
      this.players.get(1).removeCardFromHand(cardFromHand);
    }
    Colors colorToBe = this.players.get(this.turn).getColor();
    this.grid.get(playedRow).get(playedCol).changeColor(colorToBe);
  }

  @Override
  public boolean isGameOver() {
    this.throwIfNotStarted();
    return this.gamestate == GameState.Tie
            || this.gamestate == GameState.PlayerOneWin
            || this.gamestate == GameState.PlayerTwoWin;
  }


  @Override
  public boolean didPlayerOneWin() {
    this.throwIfGameNotOver();
    this.throwIfNotStarted();
    return this.gamestate == GameState.PlayerOneWin;
  }

  @Override
  public boolean didPlayerTwoWin() {
    this.throwIfGameNotOver();
    this.throwIfNotStarted();
    return this.gamestate == GameState.PlayerTwoWin;
  }

  // if isGameOver is true, and both didPlayerOneWin and didPlayerTwoWin are false, then tie
  protected void updateGameState() {
    for (List<Cells> rows : this.grid) {
      for (Cells cell : rows) {
        if (!cell.isHole() && !cell.hasCard()) {
          return; // Does nothing if the game isn't over
        }
      }
    }

    // Now that it is confirmed that every cell is either
    // a hole or is a cell with a card:
    this.determineWinner();
  }

  // count the number of cards in each player hand with the amount of cards that belong
  // to each player. Whoever owns more card wins. If equal, then there is a tie.
  private void determineWinner() {
    this.throwIfNotStarted();

    int player1TotalCards = this.players.get(0).getHand().size();
    int player2TotalCards = this.players.get(1).getHand().size();

    for (List<Cells> row : grid) {
      for (Cells cell : row) {
        if (!cell.isHole() && cell.hasCard()) {
          if (cell.cellColor() == Colors.Red) {
            player1TotalCards++;
          } else {
            player2TotalCards++;
          }
        }
      }
    }

    if (player1TotalCards > player2TotalCards) {
      this.gamestate = GameState.PlayerOneWin;
    } else if (player2TotalCards > player1TotalCards) {
      this.gamestate = GameState.PlayerTwoWin;
    } else {
      this.gamestate = GameState.Tie;
    }
  }

  @Override
  public void battle() {
    this.throwIfNotStarted();
    this.throwIfGameOver();
    Cells currentCell = grid.get(playedRow).get(playedCol);
    battleFromCell(currentCell);
    // only change turns after battles are finished
    this.turn = (this.turn + 1) % this.players.size();
    this.updateGameState();

  }

  private void battleFromCell(Cells cell) {
    Colors playerColor = this.players.get(turn).getColor();
    for (Map.Entry<Cells, Direction> entry : cell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      // the direction of the neighbor in relation to this cell
      Direction direction = entry.getValue();
      if (neighbor.isHole() || !neighbor.hasCard()) {
        continue;
      } else if (!neighbor.cellColor().equals(playerColor)) {
        int compareResult = compareCardValues(cell, neighbor, direction);
        if (compareResult <= 0) {
          // go to next entry in for each loop if current cell is equal or less than neighbor
          continue;
        }
        // this cell is greater than neighbor, so switch neighbor color and continue traversing
        neighbor.changeColor(playerColor);
        battleFromCell(neighbor);
      }
    }
  }

  protected int compareCardValues(Cells cell, Cells neighbor, Direction direction) {
    switch (direction) {
      case NORTH:
        return cell.getCard().valueAt(Direction.NORTH)
                .compareTo(neighbor.getCard().valueAt(Direction.SOUTH));
      case SOUTH:
        return cell.getCard().valueAt(Direction.SOUTH)
                .compareTo(neighbor.getCard().valueAt(Direction.NORTH));
      case EAST:
        return cell.getCard().valueAt(Direction.EAST)
                .compareTo(neighbor.getCard().valueAt(Direction.WEST));
      case WEST:
        return cell.getCard().valueAt(Direction.WEST)
                .compareTo(neighbor.getCard().valueAt(Direction.EAST));
      default:
        throw new IllegalArgumentException("Unknown direction");
    }
  }


  // OBSERVATIONS BELOW

  @Override
  public List<PlayableCard> getCurrentPlayerHand() {
    this.throwIfNotStarted();
    // Update in future if more than 2 players are present.
    if (this.turn == 0) {
      return new ArrayList<>(this.players.get(0).getHand());
    } else {
      return new ArrayList<>(this.players.get(1).getHand());
    }
  }

  @Override
  public List<List<Cells>> getGrid() {
    this.throwIfNotStarted();
    List<List<Cells>> gridCopy = new ArrayList<>();
    for (List<Cells> row : this.grid) {
      List<Cells> rowCopy = new ArrayList<>();
      for (Cells cell : row) {
        rowCopy.add(cell.copyOf());
      }
      gridCopy.add(rowCopy);
    }

    this.linkNeighbors(gridCopy);

    return gridCopy;
  }

  @Override
  public int getTurn() {
    this.throwIfNotStarted();
    return turn;
  }

  @Override
  public Colors getPlayerColor(int playerIdx) {
    if (playerIdx >= this.playerColors.size() || playerIdx < 0) {
      throw new IllegalArgumentException("Error: Player not found.");
    }
    else {
      return this.playerColors.get(playerIdx);
    }
  }

  @Override
  public int getScore(int playerIdx) {
    if (playerIdx < 0 || playerIdx >= this.players.size()) {
      throw new IllegalArgumentException("Invalid player index");
    }

    int playerTotalCards = this.players.get(playerIdx).getHand().size();

    for (List<Cells> row : grid) {
      for (Cells cell : row) {
        if (!cell.isHole() && cell.hasCard()) {
          if (cell.cellColor() == this.players.get(playerIdx).getColor()) {
            playerTotalCards++;
          }
        }
      }
    }

    return playerTotalCards;
  }

  @Override
  public int getFlipCount(int playerIdx, int handIdx, int row, int col) {
    if (playerIdx < 0 || playerIdx >= this.players.size()) {
      throw new IllegalArgumentException("Invalid player index");
    }
    if (!isValid(row, col) || this.grid.get(row).get(col).isHole()
            || this.grid.get(row).get(col).hasCard()) {
      throw new IllegalArgumentException("Invalid coordinate: either off the grid, "
              + "on a hole cell, or on a cell already containing a card");
    }
    if (handIdx < 0 || handIdx >= this.players.get(playerIdx).getHand().size()) {
      throw new IllegalArgumentException("Invalid hand index");
    }

    // use copy of grid to not change actual grid being played on
    List<List<Cells>> copyGrid = this.getGrid();
    copyGrid.get(row).get(col).changeColor(getPlayerColor(playerIdx));
    copyGrid.get(row).get(col).putCard(this.players.get(playerIdx).getHand().get(handIdx));

    // get cell from a copy of the grid to not affect grid used in game
    Cells cell = copyGrid.get(row).get(col);

    return helpCountFlips(cell, this.players.get(playerIdx).getColor());
  }

  private int helpCountFlips(Cells cell, Colors color) {
    int flips = 0;
    for (Map.Entry<Cells, Direction> entry : cell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      // the direction of the neighbor in relation to this cell
      Direction direction = entry.getValue();
      if (neighbor.isHole() || !neighbor.hasCard()) {
        continue;
      } else if (!neighbor.cellColor().equals(color)) {
        int compareResult = compareCardValues(cell, neighbor, direction);
        if (compareResult <= 0) {
          // go to next entry in for each loop if current cell is equal or less than neighbor
          continue;
        }
        // this cell is greater than neighbor, so add 1 to flips and change color of neighbor
        // and continue traversing
        neighbor.changeColor(color);
        flips += 1;
        flips += helpCountFlips(neighbor, color);
      }
    }
    return flips;
  }

  @Override
  public synchronized boolean checkLegal(int row, int col) {
    this.throwIfNotStarted();
    return this.isValid(row, col)
            && !this.grid.get(row).get(col).isHole()
            && !this.grid.get(row).get(col).hasCard();
  }

  @Override
  public Colors getCardOwner(int row, int col) {
    this.throwIfNotStarted();
    if (this.isValid(row, col) && this.grid.get(row).get(col).hasCard()) {
      return this.grid.get(row).get(col).cellColor();
    } else {
      throw new IllegalArgumentException("There is no cell in given coordinate.");
    }
  }

  @Override
  public List<PlayableCard> getPlayerHand(int playerIdx) {
    this.throwIfNotStarted();
    if (playerIdx < 0 || playerIdx >= this.players.size()) {
      throw new IllegalArgumentException("Invalid player index");
    }
    return this.players.get(playerIdx).getHand();
  }

  @Override
  public Optional<Card> getContentAtCell(int row, int col) {
    this.throwIfNotStarted();
    if (!isValid(row, col)) {
      throw new IllegalArgumentException("Invalid coordinate");
    }
    if (!this.grid.get(row).get(col).hasCard()) {
      return Optional.empty();
    } else {
      return Optional.of(this.grid.get(row).get(col).getCard());
    }
  }

  @Override
  public int getGridLength() {
    this.throwIfNotStarted();
    return this.grid.size();
  }

  @Override
  public int getGridWidth() {
    this.throwIfNotStarted();
    return this.grid.get(0).size();
  }

  @Override
  public int lastPlayedRow() {
    this.throwIfNotStarted();
    return this.playedRow;
  }

  @Override
  public int lastPlayedCol() {
    this.throwIfNotStarted();
    return this.playedCol;
  }

  // Private Methods
  protected void throwIfNotStarted() {
    if (this.gamestate == GameState.NotStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
  }

  private void throwIfStarted() {
    if (this.gamestate == GameState.Ongoing) {
      throw new IllegalStateException("Game has already started.");
    }
  }

  protected void throwIfGameOver() {
    if (this.gamestate == GameState.PlayerOneWin
            || this.gamestate == GameState.PlayerTwoWin
            || this.gamestate == GameState.Tie) {
      throw new IllegalStateException("Game is over.");
    }
  }

  private void throwIfGameNotOver() {
    if (this.gamestate != GameState.PlayerOneWin
            && this.gamestate != GameState.PlayerTwoWin
            && this.gamestate != GameState.Tie) {
      throw new IllegalStateException("Game is not over.");
    }
  }
}
