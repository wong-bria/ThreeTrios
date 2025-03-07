package provider.view.implementations;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

import provider.view.inter.PlayerActionFeatures;
import provider.model.inter.CellEnum;
import provider.model.inter.ReadOnlyThreesTrioModel;
import provider.model.inter.Cell;

/**
 * A JPanel that displays the game grid for the ThreesTrio game. It handles rendering the grid
 * cells, drawing cards on the grid, and responding to mouse interactions.
 */
class GridPanel extends JPanel {
  private final ReadOnlyThreesTrioModel model;
  private static final int minCellSize = 60;
  private PlayerActionFeatures features;

  /**
   * Constructs a new GridPanel to display the ThreesTrio game grid.
   *
   * @param model provides the game state
   */
  public GridPanel(ReadOnlyThreesTrioModel model) {
    int cellSize = getCellSize();
    this.model = model;
    this.setPreferredSize(new Dimension(model.getCols() * cellSize,
            model.getRows() * cellSize));
    addMouseListener(new GridMouseListener(this, model));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        drawCell(g2d, row, col);
      }
    }
  }

  /**
   * Draws an individual grid cell at the specified position. This includes drawing the background
   * color and any card placed in the cell.
   *
   * @param g2d the graphics used for drawing
   * @param row the row index of the cell
   * @param col the column index of the cell
   */
  private void drawCell(Graphics2D g2d, int row, int col) {
    int cellSize = getCellSize();
    int x = col * cellSize;
    int y = row * cellSize;
    Cell cell = model.getCell(row, col);

    if (cell.getType() == CellEnum.HOLE) {
      g2d.setColor(Color.GRAY);
      g2d.fillRect(x, y, cellSize, cellSize);
    } else {
      g2d.setColor(Color.YELLOW);
      g2d.fillRect(x, y, cellSize, cellSize);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, cellSize, cellSize);

      if (cell.getCard() != null) {
        CardUtil.drawGridCard(g2d, cell.getCard(), x, y,
                cell.getOwner(), cellSize);
      }
    }
  }

  @Override
  public Dimension getPreferredSize() {
    int width = getParent() != null ? getParent().getWidth() : minCellSize * model.getCols();
    int height = getParent() != null ? getParent().getHeight() : minCellSize * model.getRows();

    int cellWidth = width / model.getCols();
    int cellHeight = height / model.getRows();
    int cellSize = Math.min(cellWidth, cellHeight);

    return new Dimension(cellSize * model.getCols(), cellSize * model.getRows());
  }

  /**
   * Returns the size of a single cell based on the current dimensions of the panel.
   *
   * @return the size of a single cell
   */
  public int getCellSize() {
    if (getWidth() <= 0 || getHeight() <= 0) {
      return minCellSize;
    }
    return Math.min(getWidth() / model.getCols(),
            getHeight() / model.getRows());
  }

  /**
   * Retrieves the features associated with the player's action.
   *
   * @return the PlayerActionFeatures object representing the player's action features
   */
  public PlayerActionFeatures getFeatures() {
    return features;
  }

  /**
   * Assigns the specified features to the player's action.
   *
   * @param features the PlayerActionFeatures object containing the features to be assigned
   */
  public void addFeatures(PlayerActionFeatures features) {
    this.features = features;
  }
}