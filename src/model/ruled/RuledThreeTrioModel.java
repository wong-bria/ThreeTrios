package model.ruled;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Cells;
import model.Colors;
import model.Direction;
import model.PlayableCard;
import model.ThreeTrioGameModel;
import model.ThreeTrioModel;

/**
 * A new ThreeTrioGameModel implementation that supports ThreeTrioRules.
 * Accepts a set of rules so no duplicate rules are fed into the model.
 * Checks for mutual exclusivity upon construction.
 */
public class RuledThreeTrioModel extends ThreeTrioModel
        implements ThreeTrioGameModel<PlayableCard> {
  private final Set<ThreeTrioRule> rules;
  private final Set<ThreeTrioRule> comboRules;
  private final boolean reverse;


  /**
   * Constructor to construct a RuledThreeTrioModel.
   * @param rules The rules that this game will be played with.
   * @throws IllegalArgumentException if some rules are found to be mutually exclusive.
   */
  public RuledThreeTrioModel(Set<ThreeTrioRule> rules) {
    areRulesExclusive(rules);
    this.rules = new HashSet<>(rules);
    this.comboRules = new HashSet<>();
    this.addComboRules();
    this.reverse = hasReverse(rules);
  }

  private void addComboRules() {
    for (ThreeTrioRule rule : this.rules) {
      if (rule.allowCombo()) {
        this.comboRules.add(rule);
      }
    }
  }

  private boolean hasReverse(Set<ThreeTrioRule> rules) {
    for (ThreeTrioRule rule : rules) {
      if (rule instanceof ReverseRule) {
        return true;
      }
    }
    return false;
  }

  private void areRulesExclusive(Set<ThreeTrioRule> rules) {
    List<ThreeTrioRule> asList = new ArrayList<>(rules);
    for (int index = 0; index < asList.size(); index++) {
      ThreeTrioRule rule = asList.get(index);
      for (int iter = 0; iter < asList.size(); iter++) {
        ThreeTrioRule nextRule = asList.get(iter);
        if (index != iter && rule.isMutuallyExclusive(nextRule)) {
          throw new IllegalArgumentException("Error: Rules given are mutually exclusive.");
        }
      }
    }
  }

  @Override
  public void battle() {
    super.throwIfNotStarted();
    super.throwIfGameOver();
    Cells currentCell = super.grid.get(playedRow).get(playedCol);
    this.battleFromCell(false, currentCell); // Calls method in this calls so rules apply.
    super.turn = (super.turn + 1) % super.players.size();
    super.updateGameState();
  }

  private void battleFromCell(boolean combo, Cells cell) {
    Colors playerColor = super.getPlayerColor(super.getTurn());
    for (Map.Entry<Cells, Direction> entry : cell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      // the direction of the neighbor in relation to this cell
      Direction direction = entry.getValue();
      if (neighbor.isHole() || !neighbor.hasCard()) {
        continue;
      } else if (!neighbor.cellColor().equals(playerColor)) {
        if (this.evaluateByRules(combo, cell, neighbor, direction)) {
          neighbor.changeColor(playerColor);
          // Future calls (i.e. after this battle with neighbors) will not allow non-combo rules
          battleFromCell(true, neighbor);
        }
      }
    }
  }

  private boolean evaluateByRules(boolean combo, Cells cell, Cells neighbor, Direction direction) {
    boolean passes = false;
    // Applies combo rules if combo is true (excludes Same, Plus). Otherwise, uses non-combo rules.
    Set<ThreeTrioRule> rulesToApply = combo ? this.comboRules : this.rules;

    for (ThreeTrioRule rule : rulesToApply) {
      if (this.reverse) {
        passes |= rule.reverseSatisfiesFlip(cell, neighbor, direction);
      } else {
        passes |= rule.satisfiesFlip(cell, neighbor, direction);
      }
    }

    return passes;
  }

  @Override
  public int getFlipCount(int playerIdx, int handIdx, int row, int col) {
    if (playerIdx < 0 || playerIdx >= this.players.size()) {
      throw new IllegalArgumentException("Invalid player index");
    }
    if (!super.checkLegal(row, col)) {
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

    return helpCountFlips(false, cell, this.players.get(playerIdx).getColor());
  }

  private int helpCountFlips(boolean combo, Cells cell, Colors color) {
    int flips = 0;
    for (Map.Entry<Cells, Direction> entry : cell.getNeighbors().entrySet()) {
      Cells neighbor = entry.getKey();
      // the direction of the neighbor in relation to this cell
      Direction direction = entry.getValue();
      if (neighbor.isHole() || !neighbor.hasCard()) {
        continue;
      } else if (!neighbor.cellColor().equals(color)) {
        if (this.evaluateByRules(combo, cell, neighbor, direction)) {
          neighbor.changeColor(color);
          flips += 1;
          flips += helpCountFlips(true, neighbor, color);
        }
      }
    }
    return flips;
  }
}