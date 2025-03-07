package view;

import org.junit.Assert;
import org.junit.Test;

import old.controller.ThreeTrioController;
import old.controller.ThreeTrioGameController;
import model.PlayableCard;
import model.ThreeTrioGameModel;
import model.ThreeTrioModel;

/**
 * A test class to test the public methods of the ThreeTrioGameTextView class.
 */
public class TestPublicView {
  // Builds a valid game using a predefined board and card set.
  private ThreeTrioGameModel<PlayableCard> buildValidGame(String boardName, String cardName) {
    ThreeTrioGameController<PlayableCard> controller = new ThreeTrioController();
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    controller.startGame(model, boardName, cardName);
    return model;
  }

  // test if toString correctly displays the game at the start
  @Test
  public void testToStringAtStartGame() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("NoHolesBoard", "EnoughForAnyBoards");
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    String expected =
            "Player: RED\n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "Hand:\n"
            + "card1 1 2 3 4\n"
            + "card3 1 2 3 4\n"
            + "card5 1 2 3 4\n"
            + "card7 1 2 3 4\n"
            + "card9 1 2 3 4\n"
            + "card11 1 2 3 4\n"
            + "card13 1 2 3 4\n"
            + "card15 1 2 3 4\n"
            + "card17 1 2 3 4\n"
            + "card19 1 2 3 4\n"
            + "card21 1 2 3 4\n"
            + "card23 1 2 3 4\n"
            + "card25 1 2 3 4";


    Assert.assertEquals(expected, view.toString());
  }

  // test toString with board mixed with card cell and hole cell
  @Test
  public void testToStringCardCellAndHoleCell() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    String expected = "Player: RED\n"
            + "__   \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "Hand:\n"
            + "card1 1 2 3 4\n"
            + "card3 1 2 3 4\n"
            + "card5 1 2 3 4\n"
            + "card7 1 2 3 4\n"
            + "card9 1 2 3 4\n"
            + "card11 1 2 3 4\n"
            + "card13 1 2 3 4\n"
            + "card15 1 2 3 4\n"
            + "card17 1 2 3 4\n"
            + "card19 1 2 3 4\n"
            + "card21 1 2 3 4\n"
            + "card23 1 2 3 4";

    Assert.assertEquals(expected, view.toString());
  }

  // test toString when it is blue turn
  @Test
  public void toStringBlueTurn() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("NoHolesBoard", "EnoughForAnyBoards");
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    // red plays a card on board so its blue turn
    model.playCard(0, 0, 0);
    model.battle();

    String expected = "Player: BLUE\n"
            + "R    \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "Hand:\n"
            + "card2 1 2 3 4\n"
            + "card4 1 2 3 4\n"
            + "card6 1 2 3 4\n"
            + "card8 1 2 3 4\n"
            + "card10 1 2 3 4\n"
            + "card12 1 2 3 4\n"
            + "card14 1 2 3 4\n"
            + "card16 1 2 3 4\n"
            + "card18 1 2 3 4\n"
            + "card20 1 2 3 4\n"
            + "card22 1 2 3 4\n"
            + "card24 1 2 3 4\n"
            + "card26 1 2 3 4";

    Assert.assertEquals(expected, view.toString());

  }

  // fix below

  // test toString when there are red and blue cards on board
  @Test
  public void toStringRedAndBlueCardsOnBoard() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    // red plays a card on board, then blue plays card on board
    model.playCard(0, 0, 2);
    model.battle();
    model.playCard(11, 4, 4);
    model.battle();

    String expected = "Player: RED\n"
            + "__R  \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "    B\n"
            + "Hand:\n"
            + "card3 1 2 3 4\n"
            + "card5 1 2 3 4\n"
            + "card7 1 2 3 4\n"
            + "card9 1 2 3 4\n"
            + "card11 1 2 3 4\n"
            + "card13 1 2 3 4\n"
            + "card15 1 2 3 4\n"
            + "card17 1 2 3 4\n"
            + "card19 1 2 3 4\n"
            + "card21 1 2 3 4\n"
            + "card23 1 2 3 4";

    Assert.assertEquals(expected, view.toString());
  }

  // test toString when red switches blue to red card
  @Test
  public void toStringBlueSwitchedToRed() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    // red plays a card on board, then blue plays card on board
    model.playCard(0, 0, 2);
    model.battle();
    model.playCard(11, 0, 3);
    model.battle();

    String expected = "Player: RED\n"
            + "__BB \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "     \n"
            + "Hand:\n"
            + "card3 1 2 3 4\n"
            + "card5 1 2 3 4\n"
            + "card7 1 2 3 4\n"
            + "card9 1 2 3 4\n"
            + "card11 1 2 3 4\n"
            + "card13 1 2 3 4\n"
            + "card15 1 2 3 4\n"
            + "card17 1 2 3 4\n"
            + "card19 1 2 3 4\n"
            + "card21 1 2 3 4\n"
            + "card23 1 2 3 4";

    Assert.assertEquals(expected, view.toString());
  }

  // test view constructor throws IllegalArgumentException if model is null
  @Test (expected = IllegalArgumentException.class)
  public void testViewConstructorNullModel() {
    ThreeTrioGameView view = new ThreeTrioGameTextView(null, new StringBuilder());
  }


  // test view constructor throws IllegalArgumentException if appendable is null
  @Test (expected = IllegalArgumentException.class)
  public void testViewConstructorNullAppendable() {
    ThreeTrioGameView view = new ThreeTrioGameTextView(new ThreeTrioModel(), null);
  }
}
