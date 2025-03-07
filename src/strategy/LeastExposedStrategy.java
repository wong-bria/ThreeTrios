package strategy;

import model.Card;
import model.Cells;
import model.Direction;
import model.ReadOnlyThreeTrioGameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A strategy where cells that are the least exposed are chosen.
 * For every direction of a card on a cell, count how much cards
 * in the opponent's hand can flip the card in that direction.
 * Get the sum and find the coordinate of the cell and hand index of the card
 * with the lowest sum (least exposed).
 *
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class LeastExposedStrategy<C extends Card> implements InfailableThreeTrioStrategy<C> {
  @Override
  public List<Tuple<Coordinate, Integer>> selectTile(ReadOnlyThreeTrioGameModel<C> model,
                                                     int playerIdx) {
    int sidesExposed = Integer.MAX_VALUE;
    List<Tuple<Coordinate, Integer>> bestMoves = new ArrayList<>();
    List<List<Cells>> grid = model.getGrid();
    List<C> cards = model.getPlayerHand(playerIdx);

    for (int row = 0; row < model.getGridLength(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        Cells currCell = grid.get(row).get(col);

        // Check if the move is legal
        if (model.checkLegal(row, col)) {
          // Evaluate the exposure for each card
          for (int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
            Card currentCard = model.getPlayerHand(playerIdx).get(cardIndex);
            int exposure = countExposure(currCell, model, playerIdx, currentCard);

            // If a new least exposed card is found
            if (exposure < sidesExposed) {
              sidesExposed = exposure;
              bestMoves.clear(); // Clear previous best moves
              bestMoves.add(new Tuple<>(new Coordinate(row, col), cardIndex));
            } else if (exposure == sidesExposed) {
              // Add to best moves if there's a tie
              bestMoves.add(new Tuple<>(new Coordinate(row, col), cardIndex));
            }
          }
        }
      }
    }

    return bestMoves; // Return all the best moves found
  }

  // for each direction, figure out how many of the opponent’s cards can flip them
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
        if (compareResult < 0) { // if this card's direction can be flipped by opponent
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


}
