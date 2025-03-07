package view;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Colors;
import model.Direction;
import strategy.Coordinate;

/**
 * A custom JButton class that represents a card in the game.
 * This class is responsible for rendering card values on a button
 * in the player's hand. Each button corresponds to a specific card
 * and displays its attack values.
 */
public class CardToJButton extends JButton {
  // The card represented by this button
  private final Card card;
  // Stores the attack values and location on the card to place the values.
  private final List<String> strings;
  private final List<Coordinate> coordinates;

  /**
   * Constructor to construct a CardToJButton.
   * Initializes the button with a card and a specified background color.
   * @param card The card to be represented by the button.
   * @param color The color used for the background of the button.
   */
  public CardToJButton(Card card, Colors color) {
    this.card = card;
    this.strings = new ArrayList<>();
    this.coordinates = new ArrayList<>();
    this.setBackground(color.toColor());
    this.setFocusable(false);
  }

  @Override
  protected void paintComponent(Graphics graphic) {
    super.paintComponent(graphic);

    Graphics2D g2d = (Graphics2D) graphic.create();
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 12));

    this.strings.clear();
    this.coordinates.clear();

    this.strings.add(String.valueOf(this.card.valueAt(Direction.NORTH)));
    this.coordinates.add(new Coordinate(this.getWidth() / 2, this.getHeight() / 4));
    this.strings.add(String.valueOf(this.card.valueAt(Direction.SOUTH)));
    this.coordinates.add(new Coordinate(this.getWidth() / 2, this.getHeight() / 4 * 3));
    this.strings.add(String.valueOf(this.card.valueAt(Direction.EAST)));
    this.coordinates.add(new Coordinate(this.getWidth() / 4 * 3, this.getHeight() / 2));
    this.strings.add(String.valueOf(this.card.valueAt(Direction.WEST)));
    this.coordinates.add(new Coordinate(this.getWidth() / 4, this.getHeight() / 2));

    for (int index = 0; index < this.strings.size(); index++) {
      String value = this.strings.get(index);
      Coordinate coord = this.coordinates.get(index);
      graphic.drawString(value, coord.getX(), coord.getY());
    }

    g2d.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(50, 50);
  }
}
