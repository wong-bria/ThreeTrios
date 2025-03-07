package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test the public methods of HoleCell class.
 */
public class TestHoleCell {
  private Cells north;
  private Cells south;
  private Cells east;
  private Cells center;

  @Before
  public void setup() {
    this.north = new HoleCell();
    this.south = new HoleCell();
    this.east = new HoleCell();
    this.center = new HoleCell();

    this.center.addNeighbor(north, Direction.NORTH);
    this.center.addNeighbor(south, Direction.SOUTH);
    this.center.addNeighbor(east, Direction.EAST);

    this.north.addNeighbor(center, Direction.SOUTH);
    this.south.addNeighbor(center, Direction.NORTH);
    this.east.addNeighbor(center, Direction.WEST);
  }

  /**
   * Tests behavior of copyOf().
   * Assures the copy does not point to the same hole cell.
   */
  @Test
  public void testCopyOf() {
    Cells copy = this.north.copyOf();
    Assert.assertNotEquals(this.north, copy);
    Assert.assertTrue(copy.isHole());
  }

  /**
   * Tests behavior of putCard().
   * Does nothing and returns false.
   */
  @Test
  public void testPutCard() {
    Card card = new PlayableCard("TestCard", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    Assert.assertFalse(this.north.putCard(card));
    Assert.assertFalse(this.south.putCard(card));
    Assert.assertFalse(this.east.putCard(card));
    Assert.assertFalse(this.center.putCard(card));
  }

  /**
   * Tests isHole(). Always returns true.
   */
  @Test
  public void testIsHole() {
    Assert.assertTrue(this.north.isHole());
    Assert.assertTrue(this.south.isHole());
    Assert.assertTrue(this.east.isHole());
    Assert.assertTrue(this.center.isHole());
  }

  /**
   * Test getCard, which only returns false.
   */
  @Test
  public void testGetCard() {
    Assert.assertNull(this.north.getCard());
    Assert.assertNull(this.south.getCard());
    Assert.assertNull(this.east.getCard());
    Assert.assertNull(this.center.getCard());
  }

  /**
   * Tests getNeighbor(), which returns null, since holes don't track neighbors.
   */
  @Test
  public void testGetNeighbors() {
    Assert.assertNull(this.center.getNeighbors());
    Assert.assertNull(this.north.getNeighbors());
    Assert.assertNull(this.south.getNeighbors());
    Assert.assertNull(this.east.getNeighbors());
  }

  /**
   * Tests hasCard(), which returns false.
   */
  @Test
  public void testHasCard() {
    Assert.assertFalse(this.north.hasCard());
    Assert.assertFalse(this.south.hasCard());
    Assert.assertFalse(this.east.hasCard());
    Assert.assertFalse(this.center.hasCard());
  }

  /**
   * Test addNeighbor(), which shouldn't do anything since
   * the body is empty.
   */
  @Test
  public void testAddNeighbor() {
    this.center = new HoleCell();

    // check neighbors of HoleCell called center before adding neighbors
    Assert.assertEquals(null, center.getNeighbors());

    // call addNeighbor to HoleCell called center which should do nothing
    this.center.addNeighbor(north, Direction.NORTH);
    this.center.addNeighbor(south, Direction.SOUTH);
    this.center.addNeighbor(east, Direction.EAST);

    // check that neighbors are still null for HoleCell called center after calling addNeighbor
    Assert.assertEquals(null, center.getNeighbors());
  }


  /**
   * Test that the method cellColor() gets the Color of the cell calling it.
   * Should be null since Hole cells don't have color
   */
  @Test
  public void testCellColor() {
    Assert.assertNull(this.north.cellColor());
    Assert.assertNull(this.south.cellColor());
    Assert.assertNull(this.east.cellColor());
    Assert.assertNull(this.center.cellColor());
  }

  /**
   * Test that the method changeColor() doesn't change the color of a Hole cell.
   * (it should still be null)
   */
  @Test
  public void testChangeColor() {
    Assert.assertNull(this.north.cellColor());
    this.north.changeColor(Colors.Red);
    Assert.assertNull(this.north.cellColor());
    Assert.assertNull(this.south.cellColor());
    this.south.changeColor(Colors.Blue);
    Assert.assertNull(this.south.cellColor());
  }
}
