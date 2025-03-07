package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A test class to test the public methods in the HumanPlayer class.
 */
public class TestHumanPlayer {
  ModelPlayer<PlayableCard> redPlayer;
  ModelPlayer<PlayableCard> bluePlayer;

  /**
   * Set up method for constructing new Human Players.
   */
  @Before
  public void setup() {
    this.redPlayer = new ModelPlayerImpl(Colors.Red);
    this.bluePlayer = new ModelPlayerImpl(Colors.Blue);
  }

  /**
   * Tests the addCardToHand() method.
   * Confirms that the card is in the HumanPlayer's hand.
   */
  @Test
  public void testAddCard() {
    PlayableCard uncolored1 = new PlayableCard("card1", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    this.redPlayer.addCardToHand(uncolored1);
    Assert.assertTrue(this.redPlayer.getHand().contains(uncolored1));

    PlayableCard uncolored2 = new PlayableCard("card2", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    this.bluePlayer.addCardToHand(uncolored2);
    Assert.assertTrue(this.bluePlayer.getHand().contains(uncolored2));
  }

  /**
   * Tests the removeCardFromHand() method.
   * Confirms that the card is removed from the player's hand.
   */
  @Test
  public void testRemoveCardFromHand() {
    PlayableCard uncolored3 = new PlayableCard("card3", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    this.redPlayer.addCardToHand(uncolored3);
    Assert.assertTrue(this.redPlayer.getHand().contains(uncolored3));
    this.redPlayer.removeCardFromHand(uncolored3);
    Assert.assertFalse(this.redPlayer.getHand().contains(uncolored3));

    PlayableCard uncolored4 = new PlayableCard("card4", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    this.bluePlayer.addCardToHand(uncolored4);
    Assert.assertTrue(this.bluePlayer.getHand().contains(uncolored4));
    this.bluePlayer.removeCardFromHand(uncolored4);
    Assert.assertFalse(this.bluePlayer.getHand().contains(uncolored4));
  }

  // test that removeCardFromHand throws an error when trying to remove card not in hand
  @Test (expected = IllegalArgumentException.class)
  public void testRemoveCardFromHandWhereCardNotInHand() {
    PlayableCard uncolored5 = new PlayableCard("card5", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    this.bluePlayer.removeCardFromHand(uncolored5);
  }

  /**
   * Tests the getHand() method.
   * Confirms that passing a non-empty list in the constructor copies the contents.
   * Confirms modifying the initial list does nothing to the object.
   * Confirms modifying the list given by getHand() does nothing to the object.
   */
  @Test
  public void testGetHand() {
    PlayableCard uncolored5 = new PlayableCard("card5", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    PlayableCard uncolored6 = new PlayableCard("card6", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    List<PlayableCard> cards = new ArrayList<>();
    cards.add(uncolored5);
    cards.add(uncolored6);

    ModelPlayer<PlayableCard> human = new ModelPlayerImpl(Colors.Red);
    for (PlayableCard card : cards) {
      human.addCardToHand(card);
    }
    Assert.assertEquals(human.getHand(), cards);

    PlayableCard testMutateCard = new PlayableCard("card7", Numbers.One, Numbers.One,
            Numbers.One, Numbers.One);
    cards.add(testMutateCard);
    Assert.assertEquals(human.getHand().size(), 2);

    List<PlayableCard> fromGetHand = human.getHand();
    fromGetHand.add(testMutateCard);
    Assert.assertEquals(human.getHand().size(), 2);
  }

  /**
   * Tests the getColor() method.
   * Confirms return value matches with constructor value.
   */
  @Test
  public void testGetColor() {
    Assert.assertEquals(this.redPlayer.getColor(), Colors.Red);
    Assert.assertEquals(this.bluePlayer.getColor(), Colors.Blue);
  }
}
