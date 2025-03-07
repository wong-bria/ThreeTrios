package model.ruled;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import filereaders.ReadCardConfig;
import filereaders.ReadGridConfig;
import model.Card;
import model.CardCell;
import model.Cells;
import model.Colors;
import model.Direction;
import model.ModelFeatures;
import model.Numbers;
import model.PlayableCard;
import model.ThreeTrioGameModel;
import model.UpdatedThreeTrioModel;

/**
 * Test class to test the public methods contained in the ruled package.
 */
public class TestThreeTrioRuled {
  // test FallenAceRule's satisfiesFlip method
  @Test
  public void testFallenAceRuleSatisfiesFlip() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    Card card = new PlayableCard("card1", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("card1", Numbers.One, Numbers.A, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("card1", Numbers.Two, Numbers.Two, Numbers.Two, Numbers.Two);

    //returns false when input or neighbor cell is empty
    Assert.assertFalse(fallenAce.satisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(fallenAce.satisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));

    // tests to check if fallen ace works
    Assert.assertFalse(fallenAce.satisfiesFlip(new CardCell(card),
            new CardCell(card), Direction.NORTH));
    Assert.assertTrue(fallenAce.satisfiesFlip(new CardCell(card3),
            new CardCell(card), Direction.NORTH));
    Assert.assertTrue(fallenAce.satisfiesFlip(new CardCell(card),
            new CardCell(card2), Direction.NORTH));
  }

  // test FallenAceRule's reverseSatisfiesFlip method
  @Test
  public void testFallenAceRuleReverseSatisfiesFlip() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    Card card = new PlayableCard("card1", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("card1", Numbers.One, Numbers.A, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("card1", Numbers.Two,
            Numbers.Two, Numbers.Two, Numbers.Two);

    //returns false when input or neighbor cell is empty
    Assert.assertFalse(fallenAce.reverseSatisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(fallenAce.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));

    // tests to check if reverse fallen ace works
    Assert.assertFalse(fallenAce.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(fallenAce.reverseSatisfiesFlip(new CardCell(card3),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(fallenAce.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(card2), Direction.NORTH));
  }

  // test FallenAceRule's isMutuallyExclusive method
  @Test
  public void testFallenAceRuleIsMutuallyExclusive() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    ThreeTrioRule fallenAce2 = new FallenAceRule();
    Assert.assertFalse(fallenAce.isMutuallyExclusive(fallenAce2));
  }

  // test FallenAceRule's allowCombo method
  @Test
  public void testFallenAceRuleAllowCombo() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    Assert.assertTrue(fallenAce.allowCombo());
  }

  // test ReverseRule's isMutuallyExclusive method
  @Test
  public void testReverseRuleIsMutuallyExclusive() {
    ThreeTrioRule reverseRule = new ReverseRule();
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    ThreeTrioRule plusRule = new PlusRule();
    Assert.assertTrue(reverseRule.isMutuallyExclusive(flipGreater));
    Assert.assertFalse(reverseRule.isMutuallyExclusive(plusRule));
  }

  // test FlipGreaterRule's satisfiesFlip method
  @Test
  public void testFLipGreaterRuleSatisfiesFlip() {
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Card card = new PlayableCard("card1", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("card1", Numbers.One, Numbers.A, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("card1", Numbers.Two,
            Numbers.Two, Numbers.Two, Numbers.Two);

    //returns false when input or neighbor cell is empty
    Assert.assertFalse(flipGreater.satisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(flipGreater.satisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));

    Assert.assertFalse(flipGreater.satisfiesFlip(new CardCell(card),
            new CardCell(card), Direction.NORTH));
    Assert.assertTrue(flipGreater.satisfiesFlip(new CardCell(card3),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(flipGreater.satisfiesFlip(new CardCell(card),
            new CardCell(card2), Direction.NORTH));
  }

  // test FlipGreaterRule's reverseSatisfiesFlip method
  @Test
  public void testFLipGreaterRuleReverseSatisfiesFlip() {
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Card card = new PlayableCard("card1", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("card1", Numbers.One, Numbers.A, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("card1", Numbers.Two, Numbers.Two, Numbers.Two, Numbers.Two);

    //returns false when input or neighbor cell is empty
    Assert.assertFalse(flipGreater.reverseSatisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(flipGreater.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));

    Assert.assertFalse(flipGreater.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(flipGreater.reverseSatisfiesFlip(new CardCell(card3),
            new CardCell(card), Direction.NORTH));
    Assert.assertTrue(flipGreater.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(card2), Direction.NORTH));
  }

  // test FlipGreaterRule's isMutuallyExclusive method
  @Test
  public void testFlipGreaterRuleIsMutuallyExclusive() {
    ThreeTrioRule reverseRule = new ReverseRule();
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    ThreeTrioRule plusRule = new PlusRule();
    Assert.assertFalse(flipGreater.isMutuallyExclusive(plusRule));
    Assert.assertTrue(flipGreater.isMutuallyExclusive(reverseRule));
  }

  // test FlipGreaterRule's allowCombo method
  @Test
  public void testFlipGreaterRuleAllowCombo() {
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Assert.assertTrue(flipGreater.allowCombo());
  }

  // test PlusRule's satisfiesFlip method
  @Test
  public void testPlusRuleSatisfiesFlip() {
    ThreeTrioRule plusRule = new PlusRule();
    Card card = new PlayableCard("B", Numbers.One, Numbers.Five, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("D", Numbers.One, Numbers.Seven, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("C", Numbers.Six, Numbers.Two, Numbers.Two, Numbers.Two);
    Card card4 = new PlayableCard("A", Numbers.Five, Numbers.Four, Numbers.Three, Numbers.A);

    Cells cellB = new CardCell();
    Cells cellD = new CardCell();
    Cells cellC = new CardCell();
    Cells cellA = new CardCell();

    cellB.putCard(card);
    cellD.putCard(card2);
    cellC.putCard(card3);
    cellA.putCard(card4);

    cellA.addNeighbor(cellB, Direction.NORTH);
    cellA.addNeighbor(cellD, Direction.EAST);
    cellA.addNeighbor(cellC, Direction.SOUTH);

    Assert.assertTrue(plusRule.satisfiesFlip(cellA, cellB, Direction.NORTH));
    Assert.assertFalse(plusRule.satisfiesFlip(cellB, cellA, Direction.SOUTH));

    Assert.assertFalse(plusRule.satisfiesFlip(new CardCell(), new CardCell(card), Direction.NORTH));
    Assert.assertFalse(plusRule.satisfiesFlip(new CardCell(card), new CardCell(), Direction.NORTH));
  }


  // test PlusRule's reverseSatisfiesFlip method
  @Test
  public void testPlusRuleReverseSatisfiesFlip() {
    ThreeTrioRule plusRule = new PlusRule();
    Card card = new PlayableCard("B", Numbers.One, Numbers.Five, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("D", Numbers.One, Numbers.Seven, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("C", Numbers.Six, Numbers.Two, Numbers.Two, Numbers.Two);
    Card card4 = new PlayableCard("A", Numbers.Five, Numbers.Four, Numbers.Three, Numbers.A);

    Cells cellB = new CardCell();
    Cells cellD = new CardCell();
    Cells cellC = new CardCell();
    Cells cellA = new CardCell();

    cellB.putCard(card);
    cellD.putCard(card2);
    cellC.putCard(card3);
    cellA.putCard(card4);

    cellA.addNeighbor(cellB, Direction.NORTH);
    cellA.addNeighbor(cellD, Direction.EAST);
    cellA.addNeighbor(cellC, Direction.SOUTH);

    Assert.assertTrue(plusRule.reverseSatisfiesFlip(cellA, cellB, Direction.NORTH));
    Assert.assertFalse(plusRule.reverseSatisfiesFlip(cellB, cellA, Direction.SOUTH));

    Assert.assertFalse(plusRule.reverseSatisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(plusRule.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));
  }

  // test PlusRule's isMutuallyExclusive method
  @Test
  public void testPlusRuleIsMutuallyExclusive() {
    ThreeTrioRule plusRule = new PlusRule();
    ThreeTrioRule sameRule = new SameRule();
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Assert.assertTrue(plusRule.isMutuallyExclusive(sameRule));
    Assert.assertFalse(plusRule.isMutuallyExclusive(flipGreater));
  }

  // test PlusRule's allowCombo method
  @Test
  public void testPlusRuleAllowCombo() {
    ThreeTrioRule plusRule = new PlusRule();
    Assert.assertFalse(plusRule.allowCombo());
  }


  // test SameRule's satisfiesFlip method
  @Test
  public void testSameRuleSatisfiesFlip() {
    ThreeTrioRule sameRule = new SameRule();
    Card card = new PlayableCard("B", Numbers.One, Numbers.Six, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("D", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("C", Numbers.Four, Numbers.Two, Numbers.Two, Numbers.Two);
    Card card4 = new PlayableCard("E", Numbers.Four, Numbers.Two, Numbers.A, Numbers.Two);
    Card card5 = new PlayableCard("A", Numbers.Five, Numbers.Four, Numbers.Three, Numbers.A);

    Cells cellB = new CardCell();
    Cells cellD = new CardCell();
    Cells cellC = new CardCell();
    Cells cellE = new CardCell();
    Cells cellA = new CardCell();

    cellB.putCard(card);
    cellD.putCard(card2);
    cellC.putCard(card3);
    cellE.putCard(card4);
    cellA.putCard(card5);

    cellA.addNeighbor(cellB, Direction.NORTH);
    cellA.addNeighbor(cellD, Direction.EAST);
    cellA.addNeighbor(cellC, Direction.SOUTH);
    cellA.addNeighbor(cellE, Direction.WEST);

    Assert.assertTrue(sameRule.satisfiesFlip(cellA, cellB, Direction.NORTH));
    Assert.assertFalse(sameRule.satisfiesFlip(cellB, cellA, Direction.SOUTH));

    Assert.assertFalse(sameRule.satisfiesFlip(new CardCell(), new CardCell(card), Direction.NORTH));
    Assert.assertFalse(sameRule.satisfiesFlip(new CardCell(card), new CardCell(), Direction.NORTH));
  }


  // test SameRule's reverseSatisfiesFlip method
  @Test
  public void testSameRuleReverseSatisfiesFlip() {
    ThreeTrioRule sameRule = new SameRule();
    Card card = new PlayableCard("B", Numbers.One, Numbers.Six, Numbers.One, Numbers.One);
    Card card2 = new PlayableCard("D", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    Card card3 = new PlayableCard("C", Numbers.Four, Numbers.Two, Numbers.Two, Numbers.Two);
    Card card4 = new PlayableCard("E", Numbers.Four, Numbers.Two, Numbers.A, Numbers.Two);
    Card card5 = new PlayableCard("A", Numbers.Five, Numbers.Four, Numbers.Three, Numbers.A);

    Cells cellB = new CardCell();
    Cells cellD = new CardCell();
    Cells cellC = new CardCell();
    Cells cellE = new CardCell();
    Cells cellA = new CardCell();

    cellB.putCard(card);
    cellD.putCard(card2);
    cellC.putCard(card3);
    cellE.putCard(card4);
    cellA.putCard(card5);

    cellA.addNeighbor(cellB, Direction.NORTH);
    cellA.addNeighbor(cellD, Direction.EAST);
    cellA.addNeighbor(cellC, Direction.SOUTH);
    cellA.addNeighbor(cellE, Direction.WEST);

    Assert.assertTrue(sameRule.reverseSatisfiesFlip(cellA, cellB, Direction.NORTH));
    Assert.assertFalse(sameRule.reverseSatisfiesFlip(cellB, cellA, Direction.SOUTH));

    Assert.assertFalse(sameRule.reverseSatisfiesFlip(new CardCell(),
            new CardCell(card), Direction.NORTH));
    Assert.assertFalse(sameRule.reverseSatisfiesFlip(new CardCell(card),
            new CardCell(), Direction.NORTH));
  }

  // test SameRule's isMutuallyExclusive method
  @Test
  public void testSameRuleIsMutuallyExclusive() {
    ThreeTrioRule plusRule = new PlusRule();
    ThreeTrioRule sameRule = new SameRule();
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Assert.assertTrue(sameRule.isMutuallyExclusive(plusRule));
    Assert.assertFalse(sameRule.isMutuallyExclusive(flipGreater));
  }

  // test SameRule's allowCombo method
  @Test
  public void testSameRuleAllowCombo() {
    ThreeTrioRule sameRule = new SameRule();
    Assert.assertFalse(sameRule.allowCombo());
  }

  // test FallenAceRule's hashCode method
  @Test
  public void testFallenAceRuleHashCode() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    Assert.assertEquals(1, fallenAce.hashCode());
  }

  // test FallenAceRule's equals method
  @Test
  public void testFallenAceRuleEqualsMethod() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    ThreeTrioRule fallenAce2 = new FallenAceRule();
    ThreeTrioRule flipRule = new FlipGreaterRule();
    Assert.assertTrue(fallenAce.equals(fallenAce2));
    Assert.assertFalse(fallenAce.equals(flipRule));
  }

  // test FlipGreaterRule's hashCode method
  @Test
  public void testFlipGreaterRuleHashCode() {
    ThreeTrioRule flipGreater = new FlipGreaterRule();
    Assert.assertEquals(1, flipGreater.hashCode());
  }

  // test FlipGreaterRule's equals method
  @Test
  public void testFlipGreaterRuleEqualsMethod() {
    ThreeTrioRule fallenAce = new FallenAceRule();
    ThreeTrioRule flipRule2 = new FlipGreaterRule();
    ThreeTrioRule flipRule = new FlipGreaterRule();
    Assert.assertTrue(flipRule.equals(flipRule2));
    Assert.assertFalse(flipRule.equals(fallenAce));
  }

  // test PlusRule's hashCode method
  @Test
  public void testPlusRuleHashCode() {
    ThreeTrioRule plusRule = new PlusRule();
    Assert.assertEquals(1, plusRule.hashCode());
  }

  // test PlusRule's equals method
  @Test
  public void testPlusRuleEqualsMethod() {
    ThreeTrioRule plusRule = new PlusRule();
    ThreeTrioRule plusRule2 = new PlusRule();
    ThreeTrioRule flipRule = new FlipGreaterRule();
    Assert.assertTrue(plusRule.equals(plusRule2));
    Assert.assertFalse(plusRule.equals(flipRule));
  }

  // test ReverseRule's equals method
  @Test
  public void testReverseRuleEqualsMethod() {
    ThreeTrioRule reverse = new ReverseRule();
    ThreeTrioRule reverse2 = new ReverseRule();
    ThreeTrioRule flipRule = new FlipGreaterRule();
    Assert.assertTrue(reverse.equals(reverse2));
    Assert.assertFalse(reverse.equals(flipRule));
  }

  // test ReverseRule's hashCode method
  @Test
  public void testReverseRuleHashCode() {
    ThreeTrioRule reverse = new ReverseRule();
    Assert.assertEquals(1, reverse.hashCode());
  }

  // test SameRule's hashCode method
  @Test
  public void testSameRuleHashCode() {
    ThreeTrioRule sameRule = new SameRule();
    Assert.assertEquals(1, sameRule.hashCode());
  }

  // test SameRule's equals method
  @Test
  public void testSameRuleEqualsMethod() {
    ThreeTrioRule same = new SameRule();
    ThreeTrioRule same2 = new SameRule();
    ThreeTrioRule flipRule = new FlipGreaterRule();
    Assert.assertTrue(same.equals(same2));
    Assert.assertFalse(same.equals(flipRule));
  }


  // test RuledThreeTrioModel
  @Test
  public void testRuledThreeTrioModel() {
    Set<ThreeTrioRule> rules = new HashSet<>();
    rules.add(new ReverseRule());
    rules.add(new PlusRule());
    ThreeTrioGameModel<PlayableCard> ruledModel = new RuledThreeTrioModel(rules);
    ModelFeatures<PlayableCard> model = new UpdatedThreeTrioModel(ruledModel);

    ReadGridConfig readGrid = new ReadGridConfig("BoardConfig/HoleAndCardCellBoard");
    ReadCardConfig readCard = new ReadCardConfig("CardConfig/ForPlus");

    model.startGame(readGrid.parseGrid(), readCard.parseCards());

    model.playCard(11, 4, 0);
    model.battle();
    model.playCard(0, 1, 2);
    model.battle();
    model.playCard(10, 4, 4);
    model.battle();
    model.playCard(0, 3, 2);
    model.battle();
    model.playCard(9, 0, 4);
    model.battle();
    model.playCard(0, 2, 3);
    model.battle();
    model.playCard(8, 4, 1);
    model.battle();
    model.playCard(0, 2, 1);
    model.battle();
    model.playCard(0, 2, 2);

    // neighboring cards of center are all blue
    Assert.assertEquals(Colors.Blue, model.getGrid().get(1).get(2).cellColor());
    Assert.assertEquals(Colors.Blue, model.getGrid().get(3).get(2).cellColor());
    Assert.assertEquals(Colors.Blue, model.getGrid().get(2).get(3).cellColor());
    Assert.assertEquals(Colors.Blue, model.getGrid().get(2).get(1).cellColor());

    model.battle();
    // causes surrounding blue cards (all immediate neighbors)
    // to flip/change color
    // this is the effect of the same rule

    Assert.assertEquals(Colors.Red, model.getGrid().get(1).get(2).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(3).get(2).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(3).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(1).cellColor());

    model.playCard(1, 1, 3);
    model.battle();
    // double check neighbor doesn't flip because of reverse rule
    Assert.assertEquals(Colors.Red, model.getGrid().get(1).get(2).cellColor());

    model.playCard(2, 1, 4);
    // check color of card about to flip
    Assert.assertEquals(Colors.Blue, model.getGrid().get(1).get(3).cellColor());
    model.battle();
    // double check that reverse rule causes the card to flip
    Assert.assertEquals(Colors.Red, model.getGrid().get(1).get(3).cellColor());
  }
}
