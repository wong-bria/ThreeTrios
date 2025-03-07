package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Card;
import model.Colors;
import model.ReadOnlyThreeTrioGameModel;
import strategy.Coordinate;

/**
 * Represents a frame in a Three Trio game that contains the two players' hand, the game
 * grid, and a label to indicate the current player's turn.
 * @param <C> The type of cards used to play a game of Three Trio.
 */
public class JFrameView<C extends Card> extends JFrame implements GameView {
  private final ReadOnlyThreeTrioGameModel<C> model;
  private final List<JComponent> removables;
  private final PlayerPanel<C> player1Hand;
  private final PlayerPanel<C> player2Hand;
  private final GridPanel gridPanel;

  /**
   * Constructor for a JFrameView, which also sets up the layout, initializes its
   * components, and positions the components and the frame itself.
   * @param model The read-only model of the game to retrieve the game and updates.
   */
  public JFrameView(ReadOnlyThreeTrioGameModel<C> model) {
    this.model = model;
    this.removables = new ArrayList<>();
    this.player1Hand = new PlayerHandPanel<>(model, 0);
    this.player2Hand = new PlayerHandPanel<>(model, 1);

    // layout of player1 on left, grid in center, player2 on right, top for current player label
    this.setLayout(new BorderLayout());

    // Player1 hand on the left side
    this.add((JComponent) player1Hand, BorderLayout.WEST);

    // Player2 hand on the right side
    this.add((Component) player2Hand, BorderLayout.EAST);

    // Grid layout is centered
    //this.gridPanel = new GameGridPanel(model);
    this.gridPanel = new HintDecorator(new GameGridPanel(model));
    this.add((Component) gridPanel, BorderLayout.CENTER);

    // default frame settings
    this.setSize(600, 600);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
  }

  @Override
  public void render() {
    super.setVisible(true);

    while (!this.removables.isEmpty()) {
      JComponent removable = this.removables.remove(0);
      this.remove(removable);
    }

    Colors playerColor = model.getPlayerColor(model.getTurn());

    // top label for current player
    // label showing current player turn.
    JLabel currentPlayer = new JLabel("Current player: " + playerColor);
    JPanel topPanel = new JPanel();
    topPanel.add(currentPlayer);
    this.add(topPanel, BorderLayout.NORTH);
    this.removables.add(topPanel);

    this.player1Hand.updatePanel();
    this.player2Hand.updatePanel();
    this.gridPanel.updateAllCells();
  }

  @Override
  public void setFeature(Features feature) {
    player1Hand.setFeatures(feature);
    player2Hand.setFeatures(feature);
    gridPanel.setFeatures(feature);

    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'q' || e.getKeyChar() == 'w') {
          feature.toggleHint(e.getKeyChar());
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // intentionally left blank. (don't need)
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // intentionally left blank. (don't need)
      }
    });

    this.setFocusable(true);
    this.requestFocusInWindow();
  }

  @Override
  public void displayErrorMsg(String errMsg) {
    JOptionPane.showMessageDialog(null, errMsg, errMsg, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayWinMsg(String winMsg) {
    JOptionPane.showMessageDialog(null, winMsg, winMsg, JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showHints() {
    this.gridPanel.toggleHints();
  }

  @Override
  public void setHints(Map<Coordinate, Integer> hintFlips) {
    this.gridPanel.setHints(hintFlips);
  }
}