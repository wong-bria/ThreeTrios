package view;

import model.Cells;
import model.Colors;
import model.ReadOnlyThreeTrioGameModel;
import model.Card;
import strategy.Coordinate;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * A panel representing the game grid in the Three Trio game.
 * This panel manages the layout of cells and their behaviors when interacted with:
 * Clicking a cell on a grid.
 */
public class GameGridPanel extends JPanel implements GridPanel {
  private final ReadOnlyThreeTrioGameModel<?> model;
  private final Map<JButton, Coordinate> playableCells;
  private Features features;
  private boolean hasInitializedGrid;

  /**
   * Constructor to construct a GameGridPanel, and to initialize
   * the grid layout and set up the cells.
   *
   * @param model The read-only game model used for creating the grid panel.
   */
  public GameGridPanel(ReadOnlyThreeTrioGameModel<?> model) {
    this.model = model;
    this.playableCells = new HashMap<>();
    this.hasInitializedGrid = false;
  }

  private void initializeGrid() {
    int rows = this.model.getGridLength();
    int cols = this.model.getGridWidth();
    this.setLayout(new GridLayout(rows, cols));

    List<List<Cells>> gameGrid = this.model.getGrid();
    for (int row = 0; row < this.model.getGridLength(); row += 1) {
      for (int col = 0; col < this.model.getGridWidth(); col += 1) {
        JButton gridCell = new JButton();
        Cells currentCell = gameGrid.get(row).get(col);
        if (currentCell.isHole()) {
          gridCell.setBackground(Color.GRAY);
        } else {
          gridCell.setBackground(Color.YELLOW);
        }
        int rowClick = row;
        int colClick = col;
        gridCell.addActionListener(e -> handleCellClick(rowClick, colClick));
        this.playableCells.put(gridCell, new Coordinate(row, col));
        this.add(gridCell);
      }
    }
  }

  private void handleCellClick(int row, int col) {
    this.features.handleCellClick(row, col);
  }


  @Override
  public void updateAllCells() {
    super.setVisible(false);
    if (!this.hasInitializedGrid) {
      this.initializeGrid();
      this.hasInitializedGrid = true;
    }

    Map<JButton, Coordinate> newCopy = new HashMap<>();
    // Only need to update non-hole cells.
    for (JButton cells : this.playableCells.keySet()) {
      JButton newButton = this.updateCell(cells);
      Coordinate atButton = this.playableCells.get(cells);
      newCopy.put(newButton, atButton);
    }

    this.playableCells.clear();
    List<JButton> keySet = new ArrayList<>(newCopy.keySet());
    for (JButton currentButton : keySet) {
      Coordinate atButton = newCopy.get(currentButton);
      this.playableCells.put(currentButton, atButton);
    }

    super.setVisible(true);
  }

  private JButton updateCell(JButton cell) {
    Coordinate cellXY = this.playableCells.get(cell);
    Optional<Card> cardOptional = this.model.getContentAtCell(cellXY.getX(),
            cellXY.getY());

    if (cardOptional.isPresent()) {
      Card card = cardOptional.get();
      Colors color = this.model.getCardOwner(cellXY.getX(), cellXY.getY());
      int cellX = cellXY.getX();
      int cellY = cellXY.getY();

      JButton cardButton = new CardToJButton(card, color);

      int cellIndex = this.getComponentZOrder(cell);
      this.remove(cell);
      this.add(cardButton, cellIndex); // Add new button
      cardButton.addActionListener(e -> handleCellClick(cellX, cellY));

      return cardButton;
    }
    return cell;
  }

  @Override
  public void setFeatures(Features features) {
    this.features = features;
  }

  /**
   * Gets the playable cells on the grid.
   * @return A map of JButtons that represents the cells on the grid to their
   *         respective coordinates.
   */
  protected Map<JButton, Coordinate> getPlayableCells() {
    return this.playableCells;
  }

  @Override
  public void toggleHints() {
    // intentionally left blank.
  }

  @Override
  public void setHints(Map<Coordinate, Integer> hintFlips) {
    // intentionally left blank
  }
}
