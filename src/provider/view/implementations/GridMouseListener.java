package provider.view.implementations;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import provider.model.inter.ReadOnlyThreesTrioModel;

/**
 * Mouse listener for grid panel interactions.
 */
class GridMouseListener extends MouseAdapter {
  private final GridPanel gridPanel;
  private final ReadOnlyThreesTrioModel model;

  /**
   * Constructs a GridMouseListener with the specified grid panel and game model.
   *
   * @param gridPanel the grid that the player interacts with
   * @param model representing the game state
   */
  public GridMouseListener(GridPanel gridPanel, ReadOnlyThreesTrioModel model) {
    this.gridPanel = gridPanel;
    this.model = model;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int cellSize = gridPanel.getCellSize();
    int row = e.getY() / cellSize;
    int col = e.getX() / cellSize;
    if (model.isValidPosition(row, col)) {
      System.out.println("Clicked grid position: " + row + "," + col);
      if (gridPanel.getFeatures() != null) {
        gridPanel.getFeatures().positionSelected(row, col);
      }
    }
  }
}