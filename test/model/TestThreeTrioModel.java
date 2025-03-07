package model;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import controller.ModelNotificationListener;
import controller.ThreeTrioPlayerControllerImpl;
import old.controller.ThreeTrioController;
import old.controller.ThreeTrioGameController;
import filereaders.ReadCardConfig;
import filereaders.ReadGridConfig;
import player.HumanPlayer;
import view.JFrameView;

/**
 * Test class to test the public methods of the ThreeTrioModel class.
 */
public class TestThreeTrioModel {
  // Builds a valid game using a predefined board and card set.
  private ThreeTrioGameModel<PlayableCard> buildValidGame(String boardName, String cardName) {
    ThreeTrioGameController<PlayableCard> controller = new ThreeTrioController();
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    controller.startGame(model, boardName, cardName);
    return model;
  }

  // Builds a valid game using a predefined board and card set.
  private ThreeTrioGameModel<PlayableCard> easySwitchColorGame() {
    return this.buildValidGame("HoleAndCardCellBoard", "AllOneOrAllFour");
  }

  /**
   * Tests startGame().
   * Confirms an IllegalArgumentException is thrown when there are not enough cards
   * for the given board.
   */
  @Test
  public void testNotEnoughStartGame() {
    Assert.assertThrows(IllegalArgumentException.class, () -> this.buildValidGame(
            "NoHolesBoard", "EnoughForSomeBoards"));
  }

  /**
   * Tests startGame().
   * Confirms an IllegalStateException is thrown if the game has already started.
   */
  @Test
  public void testStateStartGame() {
    ReadGridConfig gridConfig = new ReadGridConfig("BoardConfig" + File.separator + "NoHolesBoard");
    ReadCardConfig cardConfig = new ReadCardConfig("CardConfig" + File.separator
            + "EnoughForAnyBoards");
    ThreeTrioGameModel<PlayableCard> model = this.buildValidGame("NoHolesBoard",
            "EnoughForAnyBoards");
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame(
            gridConfig.parseGrid(), cardConfig.parseCards()));
  }

  // test that startGame() throws an exception when called after game is over
  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenOver() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle(); // Game is over!
    Assert.assertTrue(model.isGameOver());


    ReadGridConfig gridConfig = new ReadGridConfig("BoardConfig"
            + File.separator + "ThreeByOneForTesting");
    ReadCardConfig cardConfig = new ReadCardConfig("CardConfig" + File.separator
            + "EnoughForAnyBoards");
    model.startGame(gridConfig.parseGrid(), cardConfig.parseCards());
  }

  /**
   * Tests playCard().
   * Confirms an IllegalArgumentException is thrown if an invalid hand index, row/col is given.
   */
  @Test
  public void testBadInputPlayCard() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(-1,
            1, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(1,
            -1, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(1,
            1, -1));

    // For the created board, the number of cards distributed is 24.
    // Cards in hand = (23+1)/2 = 12
    // indices valid from 0 - 11
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(12,
            1, 1));
  }

  /**
   * Tests playCard().
   * Confirms an IllegalArgumentException is thrown if the cell is a hole or has a card.
   */
  @Test
  public void testBadPlayCard() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    // Hole:
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(0,
            0, 0));

    // Already exists card
    model.playCard(0, 2, 2);
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playCard(0,
            2, 2));
  }

  /**
   * Tests playCard().
   * Confirms an IllegalStateException is thrown when the game has not started yet.
   */
  @Test
  public void testStatePlayCard() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.playCard(0, 0, 0));
  }

  // test battle() throws exception when game not started
  @Test(expected = IllegalStateException.class)
  public void testBattleGameNotStart() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.battle();
  }

  // test battle() throws exception when game is over
  @Test(expected = IllegalStateException.class)
  public void testBattleGameOver() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    model.battle();
  }

  // test battle() doesn't change north neighbor
  @Test
  public void testBattleNoChangeNorthNeighbor() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(0).get(0).cellColor());
  }

  // test battle() changes neighbor card color to the south
  @Test
  public void testBattleChangeSouthNeighbor() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 0, 0);
    model.battle();
    Assert.assertEquals(Colors.Blue, model.getGrid().get(1).get(0).cellColor());
  }

  // test battle() changes neighbor card color to the west
  @Test
  public void testBattleChangeWestNeighbor() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    model.playCard(0, 0, 2);
    model.battle();
    model.playCard(0, 0, 3);
    model.battle();
    Assert.assertEquals(Colors.Blue, model.getGrid().get(0).get(2).cellColor());
  }

  // test battle() changes neighbor card color to the east
  @Test
  public void testBattleChangeEastNeighbor() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(0, 0, 3);
    model.battle();
    model.playCard(1, 0, 2);
    model.battle();
    Assert.assertEquals(Colors.Blue, model.getGrid().get(0).get(3).cellColor());
  }

  // test battle() changes neighbor card color to the north
  @Test
  public void testBattleChangeNorthNeighbor() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(0, 0, 2);
    model.battle();
    model.playCard(1, 1, 2);
    model.battle();
    Assert.assertEquals(Colors.Blue, model.getGrid().get(0).get(2).cellColor());
  }

  // test battle() doesn't change neighbor card color to the south
  @Test
  public void testBattleNoChangeSouthNeighbor() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(1, 1, 2);
    model.battle();
    model.playCard(0, 0, 2);
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(1).get(2).cellColor());
  }

  // test battle() doesn't change neighbor card color to the east
  @Test
  public void testBattleNoChangeEastNeighbor() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(1, 0, 3);
    model.battle();
    model.playCard(0, 0, 2);
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(0).get(3).cellColor());
  }

  // test battle() doesn't change neighbor card color to the west
  @Test
  public void testBattleNoChangeWestNeighbor() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(1, 0, 2);
    model.battle();
    model.playCard(0, 0, 3);
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(0).get(2).cellColor());
  }

  // test battle does combo where multiple cards are flipped
  @Test
  public void testBattleMultipleFlips() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(0, 2, 0); // red 1111
    model.battle();
    model.playCard(0, 2, 3); // blue 1111
    model.battle();
    model.playCard(1, 3, 0); // red 1111
    model.battle();
    model.playCard(3, 2, 2); // blue 3333
    model.battle();
    model.playCard(0, 2, 1); // red 4444
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(2).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(3).cellColor());
  }

  // test battle does combo where multiple cards are flipped in multiple directions
  @Test
  public void testBattleMultipleFlipsTwoDirection() {
    ThreeTrioGameModel<PlayableCard> model = this.easySwitchColorGame();
    model.playCard(0, 1, 0); // red 1111
    model.battle();
    model.playCard(0, 2, 2); // blue 1111
    model.battle();
    model.playCard(1, 3, 0); // red 1111
    model.battle();
    model.playCard(3, 2, 0); // blue 3333 -> changes (3,0) and (1,0)to blue
    model.battle();
    Assert.assertEquals(Colors.Blue, model.getGrid().get(3).get(0).cellColor());
    Assert.assertEquals(Colors.Blue, model.getGrid().get(1).get(0).cellColor());
    model.playCard(0, 2, 1); // red 4444
    model.battle();
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(2).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(2).get(0).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(3).get(0).cellColor());
    Assert.assertEquals(Colors.Red, model.getGrid().get(1).get(0).cellColor());
  }

  // test getPlayerColor() correctly gets the color player1
  @Test
  public void testGetPlayerColorCorrect() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getPlayerColor(0);
    Assert.assertEquals(Colors.Red, model.getPlayerColor(0));
  }

  // test getPlayerColor() correctly gets the color player2
  @Test
  public void testGetPlayerColorCorrectBlue() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getPlayerColor(1);
    Assert.assertEquals(Colors.Blue, model.getPlayerColor(1));
  }

  // test getPlayerColor() throws exception when given invalid player index
  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayerColorBadIndexNeg() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getPlayerColor(-1);
  }

  // test getPlayerColor() throws exception when given invalid player index
  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayerColorBadIndex() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getPlayerColor(100);
  }

  // Observations
  /**
   * Tests isGameOver().
   * Confirms IllegalStateException is thrown if the game has not started yet.
   */
  @Test
  public void testIsGameOverNotStarted() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.isGameOver());
  }

  // test isGameOver() returns true when game is over
  @Test
  public void testGameOverTrue() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    Assert.assertEquals(2, model.getScore(0));
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    Assert.assertTrue(model.isGameOver());
  }

  // test isGameOver() returns false when game is not over
  @Test
  public void testIsGameOverFalse() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertFalse(model.isGameOver());
  }

  // test didPlayerOneWin throws an exception when game has not started
  @Test(expected = IllegalStateException.class)
  public void testDidPlayerOneWinGameNotStarted() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.didPlayerOneWin();
  }

  // test didPlayerOneWin() throws an exception when game is not over
  @Test(expected = IllegalStateException.class)
  public void testDidPlayerOneWinGameNotOver() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.didPlayerOneWin();
  }

  // test didPlayerOneWin() returns true when player 1 wins
  @Test
  public void testDidPlayerOneWinTrue() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertTrue(model.didPlayerOneWin());
  }

  // test didPlayerOneWin() returns false when player 1 does not win
  @Test
  public void testDidPlayerOneWinFalse() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    Assert.assertFalse(model.didPlayerOneWin());
  }

  // test didPlayerTwoWin throws an exception when game has not started
  @Test(expected = IllegalStateException.class)
  public void testDidPlayerTwoWinGameNotStarted() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.didPlayerTwoWin();
  }

  // test didPlayerTwoWin() throws an exception when game is not over
  @Test(expected = IllegalStateException.class)
  public void testDidPlayerTwoWinGameNotOver() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.didPlayerTwoWin();
  }

  // test didPlayerTwoWin() returns true when player 2 does not win
  @Test
  public void testDidPlayerTwoWinTrue() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    Assert.assertTrue(model.didPlayerTwoWin());
  }

  // test didPlayerTwoWin() returns false when player 2 does not win
  @Test
  public void testDidPlayerTwoWinFalse() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    model.battle();
    model.playCard(0, 1, 0);
    model.battle();
    model.playCard(0, 2, 0);
    model.battle();
    Assert.assertFalse(model.didPlayerTwoWin());
  }

  // test getCurrentPlayerHand() throws exception when game not started
  @Test(expected = IllegalStateException.class)
  public void testGetCurrentPlayerHandNotStart() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.getCurrentPlayerHand();
  }

  // test modifying list returned from getCurrentPlayerHand() has no effect on game
  @Test
  public void testGetCurrentPlayerHandReturnSameOrder() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    // test to see current length before modifying list
    Assert.assertEquals(2, model.getCurrentPlayerHand().size());
    // attempt to modify list returned by getCurrentPlayerHand
    model.getCurrentPlayerHand().remove(0);
    // check that the player's hand was not modified
    Assert.assertEquals(2, model.getCurrentPlayerHand().size());
  }

  // tests getCurrentPlayerHand() changes to the next player's hand after a turn is over.
  @Test
  public void testGetCurrentPlayerHandDifferentPlayer() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.playCard(0, 0, 0);
    List<PlayableCard> cards = model.getCurrentPlayerHand();
    model.battle(); // iterating turn
    List<PlayableCard> newCards = model.getCurrentPlayerHand();
    for (int index = 0; index < cards.size(); index += 1) {
      Assert.assertNotEquals(cards.get(index), newCards.get(index));
    }
  }

  // test getGrid() throws an exception when game not started
  @Test(expected = IllegalStateException.class)
  public void testGetGridNotStart() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.getGrid();
  }

  // test modifying list returned from getGrid() has no effect on game
  @Test
  public void testModifyGetGridNoEffect() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    // test to see current length before modifying list
    Assert.assertEquals(3, model.getGrid().size());
    // attempt to modify list returned by getGrid
    model.getGrid().remove(0);
    // check that the grid was not modified
    Assert.assertEquals(3, model.getGrid().size());
  }

  // tests getGrid returns the expected grid after building and after playing a card.
  @Test
  public void testGetGrid() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    List<List<Cells>> expected =
            new ReadGridConfig("BoardConfig" + File.separator + "ThreeByOneForTesting").parseGrid();
    List<List<Cells>> actual = model.getGrid();
    for (int row = 0; row < model.getGridLength(); row += 1) {
      for (int col = 0; col < model.getGridWidth(); col += 1) {
        Cells atExpected = expected.get(row).get(col);
        Cells atActual = actual.get(row).get(col);
        Assert.assertEquals(atExpected.getCard(), atActual.getCard());
      }
    }
    List<PlayableCard> copyHand = model.getPlayerHand(0);
    model.playCard(0, 0, 0);
    expected.get(0).get(0).putCard(copyHand.get(0));
    actual = model.getGrid();
    for (int row = 0; row < model.getGridLength(); row += 1) {
      for (int col = 0; col < model.getGridWidth(); col += 1) {
        Cells atExpected = expected.get(row).get(col);
        Cells atActual = actual.get(row).get(col);
        Assert.assertEquals(atExpected.getCard(), atActual.getCard());
      }
    }
  }

  // test getTurn() throws exception when game not started
  @Test(expected = IllegalStateException.class)
  public void testGetTurnNotStart() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    model.getTurn();
  }

  // test getTurn() returns the current player
  @Test
  public void testGetTurnReturnsCurrentPlayer() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    Assert.assertEquals(0, model.getTurn());
    model.playCard(0, 0, 0);
    model.battle();
    Assert.assertEquals(1, model.getTurn());
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertEquals(0, model.getTurn());
  }

  // test getScore() at start of game
  @Test
  public void testGetScoreAtStart() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    Assert.assertEquals(2, model.getScore(0));
  }

  // test getScore() at start of game
  @Test (expected = IllegalArgumentException.class)
  public void testGetScoreBadIndex() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getScore(100);
  }

  // test getScore() at start of game
  @Test (expected = IllegalArgumentException.class)
  public void testGetScoreBadIndexNeg() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    model.getScore(-1);
  }

  // tests getScore() while playing a match where cards are constantly being flipped.
  @Test
  public void testGetScoreOngoing() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "OnlyIncreasing");
    model.playCard(0, 0, 0);
    model.battle();
    // No cards flipped, 2-2
    Assert.assertEquals(model.getScore(0), 2);
    Assert.assertEquals(model.getScore(1), 2);
    model.playCard(0, 1, 0);
    model.battle();
    // Red's card at (0,0) flipped to blue, 1-3
    Assert.assertEquals(model.getScore(0), 1);
    Assert.assertEquals(model.getScore(1), 3);
    model.playCard(0, 2, 0);
    model.battle();
    // Blue's card at (1,1) AND (0,0) flipped to red. 3-1
    Assert.assertEquals(model.getScore(0), 3);
    Assert.assertEquals(model.getScore(1), 1);
    Assert.assertTrue(model.isGameOver());
    Assert.assertTrue(model.didPlayerOneWin());
  }

  // tests for an IllegalArgumentException when an invalid player index is given.
  @Test
  public void testGetFlipCountIAEPlayerIdx() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "OnlyIncreasing");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(-1, 0, 0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(2, 0, 0, 1));
  }

  // tests for an IllegalArgumentException when an invalid player index is given.
  @Test
  public void testGetFlipCountIAEHandIdx() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("ThreeByOneForTesting", "OnlyIncreasing");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, -1, 0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 10, 0, 1));
  }

  // tests for an IllegalArgumentException when an invalid coordinate is given.
  @Test
  public void testGetFlipCountIAECoordinate() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    // Hole Cells
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 0, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 2, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 2, 2));
    model.playCard(0, 1, 1);
    model.playCard(0, 2, 1);
    // Card cell already taken
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 1, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 2, 1));
    // Coordinate does not exist
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, -1, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 1, -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 5, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getFlipCount(0, 0, 1, 5));
  }

  // tests getFlipCount in a valid, ongoing game
  @Test
  public void testGetFlipOngoing() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    // No flip count because there are no cards to flip.
    Assert.assertEquals(model.getFlipCount(0, 0, 0, 1), 0);
    model.playCard(0,0, 1);
    model.battle();
    // X R X
    // C C C < -- Playing to center C
    // X C X
    // Card count is 1 because card above is flippable for blue.
    Assert.assertEquals(model.getFlipCount(1, 0, 1, 1), 1);
    model.playCard(0, 1, 1);
    model.battle();
    // X B X
    // C B C
    // X C X < -- Playing to bottom center C
    // Card count is 2 for red because both cards will be flipped to red
    Assert.assertEquals(model.getFlipCount(0, 0, 2, 1), 2);
    model.playCard(0, 2, 1);
    model.battle();
    // Card is large enough to flip the center card. Center card can flip card above it
    // but not below.
    Assert.assertEquals(model.getFlipCount(1, 0, 1, 0), 2);
    model.playCard(0, 1, 0);
    model.battle();
    // Card can flip center and upper card, but nothing else.
    Assert.assertEquals(model.getFlipCount(0, 0, 1, 2), 2);
  }

  // tests checkLegal() throws an IllegalStateException when game has not started
  @Test
  public void testCheckLegalBadState() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.checkLegal(0, 0));
  }

  // tests checkLegal() with bad inputs (out of bounds / holes)
  @Test
  public void testCheckLegalBadInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertFalse(model.checkLegal(-1, 0));
    Assert.assertFalse(model.checkLegal(0, -1));
    Assert.assertFalse(model.checkLegal(10, 0));
    Assert.assertFalse(model.checkLegal(0, 0)); // hole
    Assert.assertFalse(model.checkLegal(0, 2)); // hole
    Assert.assertFalse(model.checkLegal(2, 0)); // hole
    Assert.assertFalse(model.checkLegal(2, 2)); // hole
  }

  // tests checkLegal() with valid inputs
  @Test
  public void testCheckLegalValid() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertTrue(model.checkLegal(0, 1));
    Assert.assertTrue(model.checkLegal(1, 0));
    Assert.assertTrue(model.checkLegal(1, 1));
    Assert.assertTrue(model.checkLegal(1, 2));
    Assert.assertTrue(model.checkLegal(2, 1));
  }

  // tests checkLegal() updates appropriately after placing cards
  @Test
  public void testCheckLegalUpdate() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertTrue(model.checkLegal(1, 0));
    model.playCard(0, 1, 0);
    Assert.assertFalse(model.checkLegal(1, 0)); // card cell now has card
  }

  // tests getCardOwner() with invalid inputs (out of bounds, holes, card cells w/o cards)
  @Test
  public void testGetCardOwnerBadInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    // No cells have colors at the start, regardless of type.
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(0, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(0, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(1, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(1, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(1, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(2, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(2, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(2, 2));
    // Out of bounds
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(-1, 3));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(1, -3));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(13, 1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardOwner(1, 13));
  }

  // tests getCardOwner() throws an IllegalStateException when game has not started
  @Test
  public void testGetCardOwnerNotStarted() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.getCardOwner(0, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.getCardOwner(0, 1));
    Assert.assertThrows(IllegalStateException.class, () -> model.getCardOwner(1, 0));
  }

  // tests getCardOwner() with valid inputs
  @Test
  public void testGetCardOwnerValidInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    // Red @ (0, 1)
    model.playCard(0, 0, 1);
    model.battle();
    Assert.assertEquals(model.getCardOwner(0, 1), Colors.Red);
    // Blue @ (0, 1), (1, 1)
    model.playCard(0, 1, 1);
    model.battle();
    Assert.assertEquals(model.getCardOwner(0, 1), Colors.Blue);
    Assert.assertEquals(model.getCardOwner(1, 1), Colors.Blue);
    // Red @ (0, 1), (1, 1), (2, 1)
    model.playCard(0, 2, 1);
    model.battle();
    Assert.assertEquals(model.getCardOwner(0, 1), Colors.Red);
    Assert.assertEquals(model.getCardOwner(1, 1), Colors.Red);
    Assert.assertEquals(model.getCardOwner(2, 1), Colors.Red);
    // Blue @ (1, 0), (1, 1), (0, 1)
    // Red @ (2, 1)
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertEquals(model.getCardOwner(1, 0), Colors.Blue);
    Assert.assertEquals(model.getCardOwner(1, 1), Colors.Blue);
    Assert.assertEquals(model.getCardOwner(0, 1), Colors.Blue);
    Assert.assertEquals(model.getCardOwner(2, 1), Colors.Red);
    // Blue @ (1, 0)
    // Red @ (2, 1), (1, 2), (1, 1), (0, 1)
    model.playCard(0, 1, 2);
    model.battle();
    Assert.assertEquals(model.getCardOwner(1, 0), Colors.Blue);
    Assert.assertEquals(model.getCardOwner(2, 1), Colors.Red);
    Assert.assertEquals(model.getCardOwner(1, 2), Colors.Red);
    Assert.assertEquals(model.getCardOwner(1, 1), Colors.Red);
    Assert.assertEquals(model.getCardOwner(0, 1), Colors.Red);
  }

  // tests getPlayerColor() with bad input (out of bounds)
  @Test
  public void testGetPlayerColorBadInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getPlayerColor(-1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getPlayerColor(2));
  }

  // tests getPlayerColor with proper input
  @Test
  public void testGetPlayerColor() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertEquals(model.getPlayerColor(0), Colors.Red);
    Assert.assertEquals(model.getPlayerColor(1), Colors.Blue);
  }

  // tests for IllegalArgumentException when getPlayerHand is given bad inputs
  @Test
  public void tesGetPlayerHandBadInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getPlayerHand(-1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getPlayerHand(3));
  }

  // tests for IllegalStateException when game has not started yet
  @Test
  public void testGetPlayerHandBadState() {
    ThreeTrioGameModel<PlayableCard> notStarted = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> notStarted.getPlayerHand(0));
    Assert.assertThrows(IllegalStateException.class, () -> notStarted.getPlayerHand(1));
  }

  // tests mutating the outputted list will not affect the real list
  @Test
  public void testGetPlayerHandMutate() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    List<PlayableCard> playerOneHand = model.getPlayerHand(0);
    PlayableCard toAdd = new PlayableCard("Hi", Numbers.One, Numbers.One, Numbers.One, Numbers.One);
    playerOneHand.add(toAdd);
    playerOneHand.remove(0);
    playerOneHand.remove(0);
    Assert.assertNotEquals(model.getPlayerHand(0), playerOneHand);
    Assert.assertFalse(model.getPlayerHand(0).contains(toAdd));
    Assert.assertNotEquals(model.getPlayerHand(0).size(), playerOneHand.size());
  }

  // tests getPlayerOneHand is updated properly when cards are played.
  @Test
  public void testGetPlayerOneHandUpdate() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    List<PlayableCard> playerOneHand = model.getPlayerHand(0);
    PlayableCard toPlay = playerOneHand.get(0);
    model.playCard(0, 1, 1);
    model.battle();
    Assert.assertNotEquals(playerOneHand, model.getPlayerHand(0));
    Assert.assertEquals(playerOneHand.size() - 1, model.getPlayerHand(0).size());
    Assert.assertFalse(model.getPlayerHand(0).contains(toPlay));
    playerOneHand.remove(0);
    Assert.assertEquals(playerOneHand, model.getPlayerHand(0));
  }

  // tests getContentAtCell() throws an IllegalArgumentException for bad inputs
  @Test
  public void testGetContentAtCellBadInput() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getContentAtCell(-1, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getContentAtCell(1, -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getContentAtCell(10, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getContentAtCell(1, 10));
  }

  // tests getContentAtCell throws an IllegalStateException when game has not started
  @Test
  public void testGetContentAtCellNotStarted() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.getContentAtCell(0, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.getContentAtCell(0, 1));
    Assert.assertThrows(IllegalStateException.class, () -> model.getContentAtCell(1, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.getContentAtCell(1, 1));
  }

  // tests getContentAtCell works and updates properly when cards are placed.
  @Test
  public void testGetContentAtCellUpdates() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    // All cells are either empty or have are a hole
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(0, 0));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(0, 1));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(0, 2));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(1, 0));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(1, 1));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(1, 2));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(2, 0));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(2, 1));
    Assert.assertEquals(Optional.empty(), model.getContentAtCell(2, 2));
    // Cells now have cards
    List<PlayableCard> playerOneHand = model.getPlayerHand(0);
    PlayableCard firstPlayed = playerOneHand.get(0);
    model.playCard(0, 0, 1);
    model.battle();
    Assert.assertTrue(model.getContentAtCell(0, 1).isPresent());
    Assert.assertEquals(firstPlayed, model.getContentAtCell(0, 1).get());
    List<PlayableCard> playerTwoHand = model.getPlayerHand(1);
    PlayableCard secondPlayed = playerTwoHand.get(0);
    model.playCard(0, 1, 1);
    Assert.assertTrue(model.getContentAtCell(1, 1).isPresent());
    Assert.assertEquals(secondPlayed, model.getContentAtCell(1, 1).get());
  }

  // tests getGridLength() throws an IllegalStateException when the game has not started.
  @Test
  public void testGetGridLengthBadState() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.getGridLength());
  }

  // tests getGridLength() for various grids.
  @Test
  public void testGetGridLength() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertEquals(model.getGridLength(), 3);
    model = this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridLength(), 3);
    model = this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridLength(), 5);
    model = this.buildValidGame("SmallBoard", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridLength(), 2);
  }

  // tests getGridWidth() throws an IllegalStateException when the game has not started.
  @Test
  public void testGetGridWidthBadState() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.getGridWidth());
  }

  // tests getGridWidth() for various grids.
  @Test
  public void testGetGridWidth() {
    ThreeTrioGameModel<PlayableCard> model =
            this.buildValidGame("Plus", "OnlyIncreasing");
    Assert.assertEquals(model.getGridWidth(), 3);
    model = this.buildValidGame("ThreeByOneForTesting", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridWidth(), 1);
    model = this.buildValidGame("HoleAndCardCellBoard", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridWidth(), 5);
    model = this.buildValidGame("SmallBoard", "EnoughForAnyBoards");
    Assert.assertEquals(model.getGridWidth(), 3);
  }

  // tests lastPlayedRow() throws an IllegalStateException when game has not started
  @Test
  public void testLastPlayedRowBadState() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.lastPlayedRow());
  }

  // tests lastPlayedRow() updates as cards are being played
  @Test
  public void testLastPlayedRow() {
    ThreeTrioGameModel<PlayableCard> model
            = this.buildValidGame("Plus", "OnlyIncreasing");
    model.playCard(0, 1, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedRow(), 1);
    model.playCard(0, 0, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedRow(), 0);
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertEquals(model.lastPlayedRow(), 1);
    model.playCard(0, 1, 2);
    model.battle();
    Assert.assertEquals(model.lastPlayedRow(), 1);
    model.playCard(0, 2, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedRow(), 2);
  }

  // tests lastPlayedCol() throws an IllegalStateException when game has not started
  @Test
  public void testLastPlayedColBadState() {
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    Assert.assertThrows(IllegalStateException.class, () -> model.lastPlayedCol());
  }
  
  // tests lastPlayedCol() updates as cards are being played
  @Test
  public void testLastPlayedCol() {
    ThreeTrioGameModel<PlayableCard> model
            = this.buildValidGame("Plus", "OnlyIncreasing");
    model.playCard(0, 1, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedCol(), 1);
    model.playCard(0, 0, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedCol(), 1);
    model.playCard(0, 1, 0);
    model.battle();
    Assert.assertEquals(model.lastPlayedCol(), 0);
    model.playCard(0, 1, 2);
    model.battle();
    Assert.assertEquals(model.lastPlayedCol(), 2);
    model.playCard(0, 2, 1);
    model.battle();
    Assert.assertEquals(model.lastPlayedCol(), 1);
  }

  // test the addModelNotificationListener() method adds a listener to the model's
  // list of listeners. So initially 0 listeners + 1 listener = 1 listener.
  @Test
  public void testAddModelNotificationListener() {
    ModelFeatures<PlayableCard> model = new UpdatedThreeTrioModel();
    ModelNotificationListener listener = new ThreeTrioPlayerControllerImpl<PlayableCard>(model,
            new HumanPlayer(), new JFrameView<PlayableCard>(new ThreeTrioModel()));
    Assert.assertEquals(1, model.addModelNotificationListener(listener));
  }
}

