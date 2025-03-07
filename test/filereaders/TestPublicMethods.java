package filereaders;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import model.Cells;
import model.Direction;
import model.Numbers;
import model.PlayableCard;

/**
 * A test class to test the public methods in the ReadCardConfig and ReadGridConfig
 * classes.
 */
public class TestPublicMethods {

  /*
  parseCards tests
   */

  // test if parseCards creates a list of cards that are same as cards given in file
  @Test
  public void testParseCardsNoDuplicate() {
    ReadCardConfig reader =
            new ReadCardConfig("CardConfig" + File.separator + "ValidNoDuplicateCards");
    List<PlayableCard> cards = reader.parseCards();

    // check first card
    PlayableCard card1 = cards.get(0);
    Assert.assertEquals("Card1", card1.getName());
    Assert.assertEquals(Numbers.Four, card1.valueAt(Direction.NORTH));
    Assert.assertEquals(Numbers.Five, card1.valueAt(Direction.SOUTH));
    Assert.assertEquals(Numbers.Eight, card1.valueAt(Direction.EAST));
    Assert.assertEquals(Numbers.Nine, card1.valueAt(Direction.WEST));

    // check second card
    PlayableCard card2 = cards.get(1);
    Assert.assertEquals("Card2", card2.getName());
    Assert.assertEquals(Numbers.Three, card2.valueAt(Direction.NORTH));
    Assert.assertEquals(Numbers.Two, card2.valueAt(Direction.SOUTH));
    Assert.assertEquals(Numbers.One, card2.valueAt(Direction.EAST));
    Assert.assertEquals(Numbers.A, card2.valueAt(Direction.WEST));
  }

  // test ReadCardConfig's constructor handling files that don't exist
  @Test(expected = IllegalArgumentException.class)
  public void testReadCardConfigConstructorDoesNotExistFile() {
    ReadCardConfig reader = new ReadCardConfig("IDoNotExist");
  }

  // test parseCards finding cards with duplicate names
  @Test(expected = IllegalArgumentException.class)
  public void testParseCardsDuplicateCards() {
    ReadCardConfig reader =
            new ReadCardConfig("CardConfig" + File.separator + "DuplicateCards");
    List<PlayableCard> cards = reader.parseCards();
  }

  // test parseCards on Card with only two direction values
  @Test(expected = IllegalArgumentException.class)
  public void testParseCardsTwoDirection() {
    ReadCardConfig reader = new ReadCardConfig("CardConfig" + File.separator
            + "CardWithOnlyTwoValues");
    List<PlayableCard> cards = reader.parseCards();
  }

  /*
  parseGrid tests
   */

  // test parseGrid on grid file that said 3 rows but only provided 2.
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridLiedAmountOfRowLess() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "LiedNumberRowsTooLittle");
    List<List<Cells>> grid = reader.parseGrid();
  }

  // test parseGrid on grid file that said 3 rows but provided 4.
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridLiedAmountOfRowMore() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "LiedNumberRowsTooMuch");
    List<List<Cells>> grid = reader.parseGrid();
  }

  // test parseGrid on grid file that said 5 rows but provided 4.
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridLiedAmountOfColLess() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "LiedNumberColsTooLittle");
    List<List<Cells>> grid = reader.parseGrid();
  }

  // test parseGrid on grid file that said 5 rows but provided 6.
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridLiedAmountOfColMore() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "LiedNumberColsTooMuch");
    List<List<Cells>> grid = reader.parseGrid();
  }


  // test parseGrid when given Strings other than "X" or "C"
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridUnknownCharacter() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "UnknownCharacters");
    List<List<Cells>> grid = reader.parseGrid();
  }

  // test parseGrid when there is an even amount of card cells
  @Test(expected = IllegalArgumentException.class)
  public void testParseGridEvenAmountOfCardCell() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "HolesButSomeCardsCanNotReachEachOther");
    List<List<Cells>> grid = reader.parseGrid();
  }

  // test parseGrid when given CardCell and HoleCell
  @Test
  public void parseGridCardAndHoleCell() {
    ReadGridConfig reader = new ReadGridConfig("BoardConfig" + File.separator
            + "HoleAndCardCellBoard");
    List<List<Cells>> grid = reader.parseGrid();

    String[] expectedGrid = {"XXCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"};

    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        char cellType = expectedGrid[row].charAt(col);
        Cells cell = grid.get(row).get(col);

        if (cellType == 'C') {
          Assert.assertFalse(cell.isHole());
        } else {
          Assert.assertTrue(cell.isHole());
        }
      }
    }
  }


}
