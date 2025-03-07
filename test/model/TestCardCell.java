package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * A test class to test the public methods for the CardCell class.
 */
public class TestCardCell {
  private Cells emptyCardCell;
  private PlayableCard cardRed1234;
  private PlayableCard cardBlue4444;
  private Cells north;
  private Cells south;
  private Cells east;
  private Cells west;
  private Cells center;

  /**
   * Setup method for this class.
   */
  @Before
  public void setup() {
    this.emptyCardCell = new CardCell();
    this.cardRed1234 = new PlayableCard("red1234",
            Numbers.One, Numbers.Two, Numbers.Three, Numbers.Four);
    this.north = new CardCell(cardRed1234);
    this.cardBlue4444 = new PlayableCard("blue4444",
            Numbers.Four, Numbers.Four, Numbers.Four, Numbers.Four);
    this.south = new CardCell(cardBlue4444);
    PlayableCard blue1111 = new PlayableCard("blue1111",
            Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    this.east = new CardCell(blue1111);
    PlayableCard red5555 = new PlayableCard("red5555",
            Numbers.Five, Numbers.Five, Numbers.Five, Numbers.Five);
    this.west = new CardCell(red5555);
    PlayableCard red6666 = new PlayableCard("red6666",
            Numbers.Six, Numbers.Six, Numbers.Six, Numbers.Six);
    this.center = new CardCell(red6666);

    this.center.addNeighbor(north, Direction.NORTH);
    this.center.addNeighbor(south, Direction.SOUTH);
    this.center.addNeighbor(east, Direction.EAST);
    this.center.addNeighbor(west, Direction.WEST);

    this.north.addNeighbor(center, Direction.SOUTH);
    this.south.addNeighbor(center, Direction.NORTH);
    this.east.addNeighbor(center, Direction.WEST);
    this.west.addNeighbor(center, Direction.EAST);
  }

  /**
   * Tests copyOf().
   * Assures that the return value does not have the same reference
   * as the original card. Also assures the cards are near identical.
   */
  @Test
  public void testCopyOf() {
    Cells copyCenter = center.copyOf();
    Assert.assertNotEquals(copyCenter, center);
    Assert.assertEquals(copyCenter.cellColor(), center.cellColor());
    Assert.assertEquals(copyCenter.getCard(), center.getCard());
    Assert.assertEquals(copyCenter.isHole(), center.isHole());
  }

  /**
   * Tests behavior of putCard().
   * Putting a card in an empty card assigns the field to point to the card.
   * Putting a card in a non-empty cell does nothing.
   */
  @Test
  public void testPutCard() {
    Cells cardCell = new CardCell();
    Assert.assertTrue(cardCell.putCard(cardBlue4444));
    Assert.assertEquals(cardCell.getCard(), cardBlue4444);

    Assert.assertFalse(this.north.putCard(cardBlue4444));
    Assert.assertEquals(this.north.getCard(), cardRed1234);
  }

  /**
   * Tests isHole(). Always returns false.
   */
  @Test
  public void testIsHole() {
    Assert.assertFalse(this.emptyCardCell.isHole());
    Assert.assertFalse(this.north.isHole());
    Assert.assertFalse(this.south.isHole());
    Assert.assertFalse(this.east.isHole());
    Assert.assertFalse(this.west.isHole());
    Assert.assertFalse(this.center.isHole());
  }

  /**
   * Test getCard, confirming the output is either null or the card given to the cell.
   */
  @Test
  public void testGetCard() {
    Assert.assertNull(this.emptyCardCell.getCard());
    Assert.assertEquals(this.north.getCard(), cardRed1234);
    Assert.assertEquals(this.south.getCard(), cardBlue4444);
  }

  /**
   * Tests getNeighbor(), which returns a map containing the cards neighboring this one.
   * Also test that modifying list returned by getNeighbor() has no effect.
   */
  @Test
  public void testGetNeighbors() {
    HashMap<Cells, Direction> expected = new HashMap<>();
    expected.put(north, Direction.NORTH);
    expected.put(south, Direction.SOUTH);
    expected.put(east, Direction.EAST);
    expected.put(west, Direction.WEST);
    Assert.assertEquals(expected, this.center.getNeighbors());

    HashMap<Cells, Direction> fromNorth = new HashMap<>();
    fromNorth.put(center, Direction.SOUTH);
    Assert.assertEquals(fromNorth, north.getNeighbors());

    HashMap<Cells, Direction> fromSouth = new HashMap<>();
    fromSouth.put(center, Direction.NORTH);
    Assert.assertEquals(fromSouth, south.getNeighbors());

    HashMap<Cells, Direction> fromEast = new HashMap<>();
    fromEast.put(center, Direction.WEST);
    Assert.assertEquals(fromEast, east.getNeighbors());

    HashMap<Cells, Direction> fromWest = new HashMap<>();
    fromWest.put(center, Direction.EAST);
    Assert.assertEquals(fromWest, west.getNeighbors());

    // attempt to modify list returned by getNeighbor
    this.center.getNeighbors().clear();

    // check that list returned by getNeighbor has not been affected
    Assert.assertEquals(expected, this.center.getNeighbors());
  }

  /**
   * Tests hasCard(), which returns true iff the cardCell has a card.
   */
  @Test
  public void testHasCard() {
    Assert.assertFalse(this.emptyCardCell.hasCard());
    Assert.assertTrue(this.north.hasCard());
    Assert.assertTrue(this.south.hasCard());
    Assert.assertTrue(this.east.hasCard());
    Assert.assertTrue(this.west.hasCard());
    Assert.assertTrue(this.center.hasCard());
  }

  /**
   * Test addNeighbor(), which should add a neighboring cell with its direction into a
   * HashMap.
   */
  @Test
  public void testAddNeighbor() {
    PlayableCard red6666 = new PlayableCard("red6666",
            Numbers.Six, Numbers.Six, Numbers.Six, Numbers.Six);
    this.center = new CardCell(red6666);

    // check neighbors of CardCell called center before adding neighbors
    Assert.assertEquals(new HashMap<>(), center.getNeighbors());

    // add neighbors to CardCell called center
    this.center.addNeighbor(north, Direction.NORTH);
    this.center.addNeighbor(south, Direction.SOUTH);
    this.center.addNeighbor(east, Direction.EAST);
    this.center.addNeighbor(west, Direction.WEST);

    HashMap<Cells, Direction> expected = new HashMap<>();
    expected.put(north, Direction.NORTH);
    expected.put(south, Direction.SOUTH);
    expected.put(east, Direction.EAST);
    expected.put(west, Direction.WEST);

    // check that neighbors were added to CardCell called center after calling addNeighbor
    Assert.assertEquals(expected, center.getNeighbors());
  }

  /**
   * Test that the method cellColor() gets the Color of the cell calling it.
   * Also test that changeColor changes the color of the cell calling it to whatever
   * Color is inputted.
   */
  @Test
  public void testCardColor() {
    Assert.assertNull(this.north.cellColor());
    this.north.changeColor(Colors.Red);
    Assert.assertEquals(this.north.cellColor(), Colors.Red);
    Assert.assertNull(this.south.cellColor());
    this.south.changeColor(Colors.Blue);
    Assert.assertEquals(this.south.cellColor(), Colors.Blue);
  }
}