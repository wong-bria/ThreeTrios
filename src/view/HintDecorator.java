package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JButton;

import strategy.Coordinate;

/**
 * A decorator for a GridPanel. Decorates the grid by adding hints onto the grid:
 * for the selected card, each playable cell on the grid will display the
 * number of flips it can do.
 */
public class HintDecorator extends JPanel implements GridPanel {
  private final GridPanel basePanel;
  private boolean enableHint;
  private final Map<Coordinate, Integer> hints;

  /**
   * The constructor for the HintDecorator.
   * @param panel The grid that will be decorated.
   */
  public HintDecorator(GridPanel panel) {
    this.enableHint = false;
    this.basePanel = panel;
    this.hints = new HashMap<>();

    this.setLayout(new BorderLayout());
    this.add((Component) basePanel, BorderLayout.CENTER);
  }

  @Override
  public void updateAllCells() {
    basePanel.updateAllCells();
    Map<JButton, Coordinate> cells = ((GameGridPanel) basePanel).getPlayableCells();

    for (Map.Entry<JButton, Coordinate> entry : cells.entrySet()) {
      JButton cell = entry.getKey();

      if (enableHint) {
        Coordinate coord = entry.getValue();
        int flip = -1;
        for (Map.Entry<Coordinate, Integer> hintEntry : hints.entrySet()) {
          if (hintEntry.getKey().getX() == coord.getX()
                  && hintEntry.getKey().getY() == coord.getY()) {
            flip = hintEntry.getValue();
          }
        }
        if (flip != -1) {
          cell.setText(String.valueOf(flip));
        } else {
          cell.setText("");
        }
      } else {
        cell.setText("");
      }
    }
  }

  @Override
  public void setFeatures(Features features) {
    basePanel.setFeatures(features);
  }

  @Override
  public void toggleHints() {
    this.enableHint = !enableHint;
    updateAllCells();
  }

  @Override
  public void setHints(Map<Coordinate, Integer> hintFlips) {
    this.hints.clear();
    this.hints.putAll(hintFlips);
    updateAllCells();
  }
}