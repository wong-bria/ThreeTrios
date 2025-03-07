package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * A test class to test the public methods of the PlayableCard class.
 */
public class TestPlayableCard {
  PlayableCard red1234;
  PlayableCard blue5678;
  PlayableCard uncolored1111;

  @Before
  public void setup() {
    this.red1234 = new PlayableCard("red1234",
            Numbers.One, Numbers.Two, Numbers.Three, Numbers.Four);
    this.blue5678 = new PlayableCard("blue5678",
            Numbers.Five, Numbers.Six, Numbers.Seven, Numbers.Eight);
    this.uncolored1111 = new PlayableCard("uncolored1111",
            Numbers.One, Numbers.One, Numbers.One, Numbers.One);
  }

  /**
   * Test that the method getName() gets the name of the card calling it.
   */
  @Test
  public void testGetName() {
    Assert.assertEquals("red1234", red1234.getName());
    Assert.assertEquals("blue5678", blue5678.getName());
    Assert.assertEquals("uncolored1111", uncolored1111.getName());
  }

  /**
   * Test that the method northValue returns the north attack value
   * of the card calling it.
   */
  @Test
  public void testNorth() {
    Assert.assertEquals(this.red1234.valueAt(Direction.NORTH), Numbers.One);
    Assert.assertEquals(this.blue5678.valueAt(Direction.NORTH), Numbers.Five);
    Assert.assertEquals(this.uncolored1111.valueAt(Direction.NORTH), Numbers.One);
  }

  /**
   * Test that the method southValue returns the south attack value
   * of the card calling it.
   */
  @Test
  public void testSouth() {
    Assert.assertEquals(this.red1234.valueAt(Direction.SOUTH), Numbers.Two);
    Assert.assertEquals(this.blue5678.valueAt(Direction.SOUTH), Numbers.Six);
    Assert.assertEquals(this.uncolored1111.valueAt(Direction.SOUTH), Numbers.One);
  }

  /**
   * Test that the method eastValue returns the east attack value
   * of the card calling it.
   */
  @Test
  public void testEast() {
    Assert.assertEquals(this.red1234.valueAt(Direction.EAST), Numbers.Three);
    Assert.assertEquals(this.blue5678.valueAt(Direction.EAST), Numbers.Seven);
    Assert.assertEquals(this.uncolored1111.valueAt(Direction.EAST), Numbers.One);
  }

  /**
   * Test that the method westValue returns the west attack value
   * of the card calling it.
   */
  @Test
  public void testWest() {
    Assert.assertEquals(this.red1234.valueAt(Direction.WEST), Numbers.Four);
    Assert.assertEquals(this.blue5678.valueAt(Direction.WEST), Numbers.Eight);
    Assert.assertEquals(this.uncolored1111.valueAt(Direction.WEST), Numbers.One);
  }
}
