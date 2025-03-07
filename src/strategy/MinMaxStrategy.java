package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Card;
import model.Cells;
import model.Colors;
import model.Direction;
import model.ReadOnlyThreeTrioGameModel;

/**
 * A strategy that attempts to minimize the effect of the opponent's next move.
 * Tries every possible location with every card then guesses the opponent's strategy.
 * If opponent cards are in corners, then guess corner strategy. If opponent's score
 * is greater than this player's score, then guess flip most strategy. If opponent
 * just played to a spot with the least exposure, guess the least exposed strategy.
 * Otherwise, guess min max strategy.
 * Once opponent's strategy is guessed, simulate where the opponent would play next
 * based on where we just played, and calculate the amount of flips the opponent would
 * get as a result of the location and card we picked. The location and card index we picked
 * that will result in the fewest amount of flips for the opponent will be returned in a
 * list.
 * If we guess opponent is using min max strategy, then simulate where opponent would play,
 * which also simulates where we would play since opponent is using same strategy.
 * We will try to maximize our score
 * by minimizing the score of the opponent after opponent simulates our move in an attempt to
 * minimize the total amount of flips we can make.
 *
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class MinMaxStrategy<C extends Card> implements InfailableThreeTrioStrategy<C> {
  private int greatestValue; // corner
  private Coordinate selectedCell; // corner

  @Override
  public List<Tuple<Coordinate, Integer>> selectTile(ReadOnlyThreeTrioGameModel<C> model,
                                                     int playerIdx) {
    this.greatestValue = -1;
    this.selectedCell = null;
    int bestScore = Integer.MAX_VALUE; // the opponent's max score. we want to minimize it
    List<Tuple<Coordinate, Integer>> bestMoves = new ArrayList<>();

    // iterate through all legal moves for current player
    List<C> playerHand = model.getPlayerHand(playerIdx);
    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col)) {
          for (int cardIdx = 0; cardIdx < playerHand.size(); cardIdx++) {

            // now we simulate our move
            List<List<Cells>> copyGrid = model.getGrid();
            copyGrid.get(row).get(col).changeColor(model.getPlayerColor(playerIdx));
            copyGrid.get(row).get(col).putCard(model.getPlayerHand(playerIdx).get(cardIdx));

            // now evaluate opponent's best move
            int opponentIdx = (playerIdx + 1) % 2;
            int opponentBestScore = evalOppsBestMove(copyGrid, opponentIdx, model);

            // minimize the score of the opponent's best move
            if (opponentBestScore < bestScore) {
              bestScore = opponentBestScore;
              bestMoves.clear();
              bestMoves.add(new Tuple<>(new Coordinate(row, col), cardIdx));
            } else if (opponentBestScore == bestScore) {
              bestMoves.add(new Tuple<>(new Coordinate(row, col), cardIdx));
            }
          }
        }
      }
    }

    return bestMoves;
  }

  // first guess opponent strategy. Then simulate what opponent's best moves are
  // depending on the strategy that could give them the highest score possible.
  private int evalOppsBestMove(List<List<Cells>> grid,
                               int opponentIdx, ReadOnlyThreeTrioGameModel<C> model) {
    // Determine the opponent's strategy based on the current board state
    String strategy = determineStrategy(grid, opponentIdx, model);

    // Simulate the opponent's move based on the determined strategy
    switch (strategy) {
      case "CornerStrategy":
        return simulateCornerStrategy(model, opponentIdx, grid);
      case "FlipMostCardsStrategy":
        return simulateFlipMostCardsStrategy(model, opponentIdx, grid);
      case "LeastExposedStrategy":
        return simulateLeastExposedStrategy(model, opponentIdx, grid);
      default:
        return simulateMinimaxStrategy(model, opponentIdx, grid);
    }
  }

  private String determineStrategy(List<List<Cells>> grid, int opponentIdx,
                                   ReadOnlyThreeTrioGameModel<C> model) {
    Colors oppColor = model.getPlayerColor(opponentIdx);
    int maxWidth = model.getGridWidth() - 1;
    int maxLength = model.getGridLength() - 1;

    if (grid.get(0).get(0).cellColor() == oppColor
            || grid.get(maxLength).get(maxWidth).cellColor() == oppColor
            || grid.get(0).get(maxWidth).cellColor() == oppColor
            || grid.get(maxLength).get(0).cellColor() == oppColor) {
      return "CornerStrategy";
    } else if (model.getScore(opponentIdx) > model.getScore((opponentIdx + 1) % 2)) {
      return "FlipMostCardsStrategy";
    } else if (guessLeast(model, opponentIdx, grid)) {
      return "LeastExposedStrategy";
    } else {
      return "MinimaxStrategy"; // Default guess if no other patterns are found
    }
  }


  private boolean guessLeast(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                             List<List<Cells>> grid) {
    if ((model.lastPlayedRow() == -1) && (model.lastPlayedCol() == -1)) {
      return false;
    }
    // lastPlayedRow and lastPlayedCol should allow null since it needs to check
    // here: if minmax is first player then no moves were made, so
    // this would check for null and return false

    Card curCard = grid.get(model.lastPlayedRow()).get(model.lastPlayedCol()).getCard();
    int originalExposure = calculateExposure(model, model.lastPlayedRow(),
            model.lastPlayedCol(), curCard, opponentIdx, grid);
    List<C> opponentHand = model.getPlayerHand(opponentIdx);

    // iterate through whole board
    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col)) {
          // iterate through opponent entire hand
          for (int cardIndex = 0; cardIndex < opponentHand.size(); cardIndex++) {
            int currentExposure = calculateExposure(model, row, col,
                    model.getPlayerHand(opponentIdx).get(cardIndex), opponentIdx, model.getGrid());

            // check if there is a card that has a lower exposure than the card played
            if (currentExposure < originalExposure) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  // changed so that exposure is only increased
  // by checking neighbor card cells without card
  private int calculateExposure(ReadOnlyThreeTrioGameModel<C> model, int row, int col,
                                Card card, int opponentIdx, List<List<Cells>> grid) {
    int exposure = 0;
    Cells currCell = grid.get(row).get(col);
    for (Map.Entry<Cells, Direction> entry : currCell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      Direction direction = entry.getValue();
      if (neighbor.isHole() || neighbor.hasCard()) {
        continue;
      }
      else { // neighbor is card cell without card
        // so look at how many of opponent's card can flip this one
        for (Card cards : model.getPlayerHand((opponentIdx + 1) % 2)) {
          int compareResults = compareCardValues(card, cards, direction);
          if (compareResults < 0) {
            exposure++;
          }
        }

      }
    }

    return exposure;
  }

  private int compareCardValuesCorner(Cells cell, Cells neighbor, Direction direction) {
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

  private int compareCardValues(Card curCard, Card neighbor, Direction direction) {
    switch (direction) {
      case NORTH:
        return curCard.valueAt(Direction.NORTH)
                .compareTo(neighbor.valueAt(Direction.SOUTH));
      case SOUTH:
        return curCard.valueAt(Direction.SOUTH)
                .compareTo(neighbor.valueAt(Direction.NORTH));
      case EAST:
        return curCard.valueAt(Direction.EAST)
                .compareTo(neighbor.valueAt(Direction.WEST));
      case WEST:
        return curCard.valueAt(Direction.WEST)
                .compareTo(neighbor.valueAt(Direction.EAST));
      default:
        throw new IllegalArgumentException("Unknown direction");
    }
  }

  // simulate where opponent places card based on where this player placed at selectedTile
  private int simulateCornerStrategy(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                                     List<List<Cells>> grid) {
    int length = grid.size() - 1;
    int width = grid.get(0).size() - 1;
    List<C> cards = model.getPlayerHand(opponentIdx);

    placeTopLeft(model, grid, cards);
    placeTopRight(model, grid, width, cards);
    placeBotLeft(model, grid, length, cards);
    placeBotRight(model, grid, length, width, cards);

    // Check if there are valid corners where both exposed sides are not facing
    // empty card cells. If none, find the uppermost, leftmost open position
    if (selectedCell == null) {
      if (model.checkLegal(0, 0) && !grid.get(0).get(0).hasCard()) {
        grid.get(0).get(0).changeColor(model.getPlayerColor(opponentIdx));
        grid.get(0).get(0).putCard(model.getPlayerHand(opponentIdx).get(0));
        return flipCount(opponentIdx, 0, 0, model, grid);
      }
      if (model.checkLegal(0, width) && !grid.get(0).get(width).hasCard()) {
        grid.get(0).get(width).changeColor(model.getPlayerColor(opponentIdx));
        grid.get(0).get(width).putCard(model.getPlayerHand(opponentIdx).get(0));
        return flipCount(opponentIdx, 0, width, model, grid);
      }
      if (model.checkLegal(length, 0) && !grid.get(length).get(0).hasCard()) {
        grid.get(length).get(0).changeColor(model.getPlayerColor(opponentIdx));
        grid.get(length).get(0).putCard(model.getPlayerHand(opponentIdx).get(0));
        return flipCount(opponentIdx, length, 0, model, grid);
      }
      if (model.checkLegal(length, width) && !grid.get(length).get(width).hasCard()) {
        grid.get(length).get(width).changeColor(model.getPlayerColor(opponentIdx));
        grid.get(length).get(width).putCard(model.getPlayerHand(opponentIdx).get(0));
        return flipCount(opponentIdx, length, width, model, grid);
      }
      for (int row = 0; row < model.getGridLength(); row++) {
        for (int col = 0; col < model.getGridWidth(); col++) {
          if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
            grid.get(row).get(col).changeColor(model.getPlayerColor(opponentIdx));
            grid.get(row).get(col).putCard(model.getPlayerHand(opponentIdx).get(0));
            return flipCount(opponentIdx, row, col, model, grid);
          }
        }
      }
    }
    return countCornerOrLastAvailable(model, opponentIdx, grid);
  }

  private int countCornerOrLastAvailable(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                                         List<List<Cells>> grid) {
    boolean filled = true;
    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
          filled = false;
        }
      }
    }
    if (filled) {
      return 0;
    } else {
      return flipCount(opponentIdx, selectedCell.getX(), selectedCell.getY(), model, grid);
    }
  }

  private void placeBotRight(ReadOnlyThreeTrioGameModel<C> model,
                             List<List<Cells>> grid, int length, int width, List<C> cards) {
    if (model.checkLegal(length, width) && !grid.get(length).get(width).hasCard()) { // bottom right
      Coordinate bottomRight = new Coordinate(length, width);
      this.valueAtDir(bottomRight, cards, model);
    }
  }

  private void placeBotLeft(ReadOnlyThreeTrioGameModel<C> model,
                            List<List<Cells>> grid, int length, List<C> cards) {
    if (model.checkLegal(length, 0) && !grid.get(length).get(0).hasCard()) { // bottom left
      Coordinate bottomLeft = new Coordinate(length, 0);
      this.valueAtDir(bottomLeft, cards, model);
    }
  }

  private void placeTopRight(ReadOnlyThreeTrioGameModel<C> model,
                             List<List<Cells>> grid, int width, List<C> cards) {
    if (model.checkLegal(0, width) && !grid.get(0).get(width).hasCard()) { // top right
      Coordinate topRight = new Coordinate(0, width);
      this.valueAtDir(topRight, cards, model);
    }
  }

  private void placeTopLeft(ReadOnlyThreeTrioGameModel<C> model,
                            List<List<Cells>> grid, List<C> cards) {
    if (!grid.get(0).get(0).isHole() && !grid.get(0).get(0).hasCard()) {
      Coordinate topLeft = new Coordinate(0, 0); // top left
      this.valueAtDir(topLeft, cards, model);
    }
  }

  private int flipCount(int oppIdx, int row, int col,
                        ReadOnlyThreeTrioGameModel<C> model, List<List<Cells>> grid) {
    Colors color = model.getPlayerColor(oppIdx);
    Cells cell2 = grid.get(row).get(col).copyOf();

    return helpCountFlips(cell2, color);
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
        int compareResult = compareCardValuesCorner(cell, neighbor, direction);
        if (compareResult <= 0) {
          // go to next entry in for each loop if current cell is equal or less than neighbor
          continue;
        }
        // this cell is greater than neighbor, so add 1 to flips and change color of neighbor
        // and continue traversing
        neighbor.changeColor(color);
        flips += helpCountFlips(neighbor, color) + 1;
      }
    }
    return flips;
  }

  private void valueAtDir(Coordinate corner, List<C> cards, ReadOnlyThreeTrioGameModel<C> model) {
    for (int index = 0; index < cards.size(); index += 1) {
      Card card = cards.get(index);
      int length = model.getGridLength() - 1;
      int width = model.getGridWidth() - 1;
      int value = 0;

      if (corner.getX() == 0 && corner.getY() == 0) {
        if (model.checkLegal(corner.getX() + 1, corner.getY())) {
          value = value + card.valueAt(Direction.SOUTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() + 1)) {
          value = value + card.valueAt(Direction.EAST).toNum();
        }
      } else if (corner.getX() == 0 && corner.getY() == width) {
        if (model.checkLegal(corner.getX() + 1, corner.getY())) {
          value = value + card.valueAt(Direction.SOUTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() - 1)) {
          value = value + card.valueAt(Direction.WEST).toNum();
        }
      } else if (corner.getX() == length && corner.getY() == 0) {
        if (model.checkLegal(corner.getX() - 1, corner.getY())) {
          value = value + card.valueAt(Direction.NORTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() + 1)) {
          value = value + card.valueAt(Direction.EAST).toNum();
        }
      } else if (corner.getX() == length && corner.getY() == width) {
        if (model.checkLegal(corner.getX() - 1, corner.getY())) {
          value = value + card.valueAt(Direction.NORTH).toNum();
        }
        if (model.checkLegal(corner.getX(), corner.getY() - 1)) {
          value = value + card.valueAt(Direction.WEST).toNum();
        }
      }

      if (value > this.greatestValue) {
        this.greatestValue = value;
        this.selectedCell = corner;
      }
    }
  }

  private int simulateFlipMostCardsStrategy(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                                            List<List<Cells>> grid) {
    List<C> cards = model.getPlayerHand(opponentIdx);
    int mostFlipped = -1;
    Coordinate coordinate = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE);
    int selectedCardIdx = -1;
    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        // only check non hole cell/ card cell w/o card
        if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
          for (int cardIdx = 0; cardIdx < cards.size(); cardIdx++) {
            grid.get(row).get(col).changeColor(model.getPlayerColor(opponentIdx));
            grid.get(row).get(col).putCard(model.getPlayerHand(opponentIdx).get(cardIdx));
            int flipCount = flipCount(opponentIdx, row, col, model, grid);
            if (flipCount > mostFlipped) {
              mostFlipped = flipCount;
              coordinate = new Coordinate(row, col);
              selectedCardIdx = cardIdx;
            } else if (flipCount == mostFlipped) {
              if (row < coordinate.getX()
                      || (row == coordinate.getX() && col < coordinate.getY())) {
                coordinate = new Coordinate(row, col);
                selectedCardIdx = cardIdx;
              } else if (row == coordinate.getX()
                      && col == coordinate.getY() && cardIdx < selectedCardIdx) {
                selectedCardIdx = cardIdx;
              }
            }
          }
        }
      }
    }
    // If no valid moves were found, select the uppermost-leftmost w/ card at index 0
    if (coordinate.getX() == Integer.MAX_VALUE) {
      for (int row = 0; row < model.getGridLength(); row++) {
        for (int col = 0; col < model.getGridWidth(); col++) {
          if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
            grid.get(row).get(col).changeColor(model.getPlayerColor(opponentIdx));
            grid.get(row).get(col).putCard(model.getPlayerHand(opponentIdx).get(0));
            return flipCount(opponentIdx, row, col, model, grid);
          }
        }
      }
    }
    return mostFlipped;
  }

  private void printGrid(List<List<Cells>> grid) {
    for (int r = 0; r < grid.size(); r += 1) {
      for (int c = 0; c < grid.get(r).size(); c += 1) {
        Colors color = grid.get(r).get(c).cellColor();
        if (grid.get(r).get(c).isHole()) {
          System.out.print("H");
        }
        else if (color == null) {
          System.out.print("N");
        }
        else {
          System.out.print(color);
        }
        System.out.print(" | ");
      }
      System.out.println();
    }
  }

  private int simulateLeastExposedStrategy(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                                           List<List<Cells>> grid) {
    System.out.println("Using least exposed");
    int sidesExposed = Integer.MAX_VALUE;
    Coordinate bestCoordinate = new Coordinate(-1, -1);

    List<C> opponentHand = model.getPlayerHand(opponentIdx);
    this.printGrid(grid);

    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
          Cells currentCell = grid.get(row).get(col);

          for (int cardIdx = 0; cardIdx < opponentHand.size(); cardIdx++) {
            Card currentCard = opponentHand.get(cardIdx);
            int currentExposure = countExposure(currentCell, model, opponentIdx, currentCard);

            if (currentExposure < sidesExposed) {
              sidesExposed = currentExposure;
              bestCoordinate = new Coordinate(row, col);
            } else if (currentExposure == sidesExposed) {
              // In case of a tie, select the uppermost, leftmost move
              if (row < bestCoordinate.getX()
                      || (row == bestCoordinate.getX() && col < bestCoordinate.getY())) {
                bestCoordinate = new Coordinate(row, col);
              }
            }
          }
        }
      }
    }
    return flipCount(opponentIdx, bestCoordinate.getX(), bestCoordinate.getY(), model, grid);
  }

  private int countExposure(Cells currCell, ReadOnlyThreeTrioGameModel<C> model, int playerIdx,
                            Card curCard) {
    int exposure = 0;

    for (Map.Entry<Cells, Direction> entry : currCell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      Direction direction = entry.getValue();
      if (neighbor.isHole() || neighbor.cellColor() == model.getPlayerColor(playerIdx)) {
        continue;
      } else if (neighbor.hasCard() && neighbor.cellColor() != model.getPlayerColor(playerIdx)) {
        int compareResult = compareCardValues(curCard, neighbor.getCard(), direction);
        if (compareResult < 0) { // if this card's direction is can be flipped by opponent
          exposure++;
        }
      } else { // neighbor is card cell without card
        // so look at how many of opponent's card can flip this one
        for (Card card : model.getPlayerHand((playerIdx + 1) % 2)) {
          int compareResults = compareCardValues(curCard, card, direction);
          if (compareResults < 0) {
            exposure++;
          }
        }

      }
    }

    return exposure;
  }

  private List<List<Cells>> copyGrid(List<List<Cells>> grid) {
    List<List<Cells>> output = new ArrayList<>();
    for (List<Cells> rows : grid) {
      List<Cells> a = new ArrayList<>();
      for (Cells cells : rows) {
        a.add(cells.copyOf());
      }
      output.add(a);
    }

    for (int i = 0; i < output.size(); i++) {
      for (int j = 0; j < output.get(i).size(); j++) {
        Cells c = output.get(i).get(j);
        if (j > 0) {
          c.addNeighbor(output.get(i).get(j - 1), Direction.WEST);
        }
        if (j < output.get(i).size() - 2) {
          c.addNeighbor(output.get(i).get(j + 1), Direction.EAST);
        }
        if (i > 0) {
          c.addNeighbor(output.get(i - 1).get(j), Direction.NORTH);
        }
        if (i < output.size() - 2) {
          c.addNeighbor(output.get(i + 1).get(j), Direction.SOUTH);
        }
      }
    }

    return output;
  }

  private int simulateMinimaxStrategy(ReadOnlyThreeTrioGameModel<C> model, int opponentIdx,
                                      List<List<Cells>> grid) {
    System.out.println("Using minimax");
    this.printGrid(grid);
    int maxScore = Integer.MIN_VALUE; // opponent wants to maximize their score
    List<C> opponentHand = model.getPlayerHand(opponentIdx);
    System.out.println("Starting now!");
    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
          for (int cardIdx = 0; cardIdx < opponentHand.size(); cardIdx++) {
            List<List<Cells>> copyOfCopy = new ArrayList<>(grid);
            this.printGrid(copyOfCopy);
            System.out.println("----------------");
            // Simulate the move
            Cells cell = copyOfCopy.get(row).get(col);
            Card card = opponentHand.get(cardIdx);
            cell.changeColor(model.getPlayerColor(opponentIdx));
            cell.putCard(card);

            // Evaluate the move by checking our response/ our opponent's opponent's response
            int playerIdx = (opponentIdx + 1) % 2;
            int score = evaluatePlayerResponse(copyOfCopy, playerIdx, model);

            // Update maxScore with the highest score found
            maxScore = Math.max(maxScore, score);
          }
        }
      }
    }

    return maxScore;
  }

  // Helper method to evaluate the best move the player can make in response
  private int evaluatePlayerResponse(List<List<Cells>> grid, int playerIdx,
                                     ReadOnlyThreeTrioGameModel<C> model) {
    int maxScore = Integer.MIN_VALUE; // We want to maximize the player's best score
    List<C> playerHand = model.getPlayerHand(playerIdx);

    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.checkLegal(row, col) && !grid.get(row).get(col).hasCard()) {
          for (int cardIdx = 0; cardIdx < playerHand.size(); cardIdx++) {
            List<List<Cells>> copyOfCopy = new ArrayList<>(grid);

            // Simulate the player's move
            Cells cell = copyOfCopy.get(row).get(col);
            Card card = playerHand.get(cardIdx);
            cell.changeColor(model.getPlayerColor(playerIdx));
            cell.putCard(card);

            int score = calculateMoveScore(cell, model, playerIdx);

            // update minScore with the lowest score found
            maxScore = Math.max(maxScore, score);
          }
        }
      }
    }

    return maxScore;
  }

  private int calculateMoveScore(Cells cell, ReadOnlyThreeTrioGameModel<C> model, int playerIdx) {
    return helpCountFlips(cell, model.getPlayerColor(playerIdx));
  }


}