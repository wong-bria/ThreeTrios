package provider.view.implementations;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import provider.model.inter.Card;
import provider.model.inter.Player;

/**
 * Utility class for drawing cards in both grid and hand panels.
 */
class CardUtil {
  private static final Font CARD_FONT = new Font("Arial", Font.PLAIN, 10);
  private static final Font VALUE_FONT = new Font("Arial", Font.BOLD, 12);

  /**
   * Renders a ThreesTrio card within a player's hand, using color to indicate ownership.
   *
   * @param g2d the graphics used for rendering
   * @param card the card being drawn
   * @param x the x-coordinate of the top-left corner of the card area
   * @param y the y-coordinate of the top-left corner of the card area
   * @param owner the player who owns the card, used to determine the card color
   * @param width the width of the card area
   * @param height the height of the card area
   */
  public static void drawHandCard(Graphics2D g2d, Card card, int x, int y,
                                  Player owner, int width, int height) {
    if (owner.getColor().equals("RED")) {
      g2d.setColor(new Color(255, 200, 200));
    } else {
      g2d.setColor(new Color(200, 200, 255));
    }

    g2d.fillRect(x + 2, y + 2, width - 4, height - 4);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x + 2, y + 2, width - 4, height - 4);

    drawCardDetails(g2d, card, x, y, width, height);
  }

  /**
   * Renders a ThreesTrio card within grid, using color to indicate ownership.
   *
   * @param g2d the graphics used for rendering
   * @param card the card being drawn
   * @param x the x-coordinate of the top-left corner of the card area
   * @param y the y-coordinate of the top-left corner of the card area
   * @param owner the player who owns the card, used to determine the card color
   * @param cellSize the size of the cell in grid
   */
  public static void drawGridCard(Graphics2D g2d, Card card, int x, int y,
                                  Player owner, int cellSize) {
    drawHandCard(g2d, card, x, y, owner, cellSize, cellSize);
  }

  /**
   * Draws the details of a ThreesTrio card on the graphics context.
   *
   * @param g2d the graphics used for rendering
   * @param card the card whose details are being drawn
   * @param x the x-coordinate of the top-left corner of the card area
   * @param y the y-coordinate of the top-left corner of the card area
   * @param width the width of the card area
   * @param height the height of the card area
   */
  private static void drawCardDetails(Graphics2D g2d, Card card,
                                      int x, int y, int width, int height) {
    Font originalFont;
    originalFont = g2d.getFont();

    g2d.setFont(VALUE_FONT);
    FontMetrics metrics = g2d.getFontMetrics();

    String northVal = convertTenString(card.getNorthValue());
    String southVal = convertTenString(card.getSouthValue());
    String eastVal = convertTenString(card.getEastValue());
    String westVal = convertTenString(card.getWestValue());

    // North value
    int northWidth = metrics.stringWidth(northVal);
    g2d.drawString(northVal,
            x + (width - northWidth) / 2,
            y + metrics.getHeight());

    // South value
    int southWidth = metrics.stringWidth(southVal);
    g2d.drawString(southVal,
            x + (width - southWidth) / 2,
            y + height - metrics.getDescent());

    // East value
    int eastWidth = metrics.stringWidth(eastVal);
    g2d.drawString(eastVal,
            x + width - eastWidth - metrics.getHeight() / 2,
            y + height / 2 + metrics.getAscent() / 2);

    // West value
    g2d.drawString(westVal,
            x + metrics.getHeight() / 2,
            y + height / 2 + metrics.getAscent() / 2);

    g2d.setFont(CARD_FONT);
    metrics = g2d.getFontMetrics();
    String name = card.getName();
    int nameWidth = metrics.stringWidth(name);
    g2d.drawString(name,
            x + (width - nameWidth) / 2,
            y + height / 2 + metrics.getAscent() / 2);

    g2d.setFont(originalFont);
  }

  /**
   * Converts a card's integer value to its display string.
   * If the value is 10, it returns "A" as a special representation.
   *
   * @param value the integer value of the card
   * @return "A" if the value is 10; otherwise, the string representation of the value
   */
  private static String convertTenString(int value) {
    if (value == 10) {
      return "A";
    } else {
      return String.valueOf(value);
    }
  }
}