package controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.PlayableCard;
import player.HumanPlayer;
import player.PlayerActions;
import strategy.Coordinate;
import view.ThreeTrioGameView;

/**
 * A testing class for a ThreeTrioController.
 * Uses mocks to confirm input/output is being passed properly.
 */
public class TestControllerMock {
  private MockThreeTrioModel<PlayableCard> mockModel;
  private MockThreeTrioView<PlayableCard> mockView;
  private Appendable modelAppendable;
  private Appendable viewAppendable;

  private void setup(PlayerActions type) {
    this.modelAppendable = new StringBuilder();
    this.mockModel = new MockThreeTrioModel<>(modelAppendable);
    this.viewAppendable = new StringBuilder();
    this.mockView = new MockThreeTrioView<>(viewAppendable);
    ThreeTrioGameController<PlayableCard> controller =
            new ThreeTrioPlayerControllerImpl<>(mockModel, type, mockView);
  }

  // Tests the controller actually calls the render method of the view
  // upon updating game state.
  @Test
  public void testRenderCalled() {
    this.setup(new HumanPlayer());
    this.mockModel.notifyGameStateUpdated();
    Assert.assertEquals(this.viewAppendable.toString(), "Render");
  }

  // Uses the model and view to force an invalid turn scenario.
  // Ensures the error message passed is the same.
  @Test
  public void testInvalidMove() {
    this.setup(new HumanPlayer());
    String message = "Bad move made!";
    // ensures calling the wrong player index does not trigger a message
    this.mockModel.notifyInvalidMove(message, 1);
    Assert.assertEquals(this.viewAppendable.toString(), "");

    this.mockModel.notifyInvalidMove(message, 0);
    Assert.assertEquals(this.viewAppendable.toString(), message);
  }


  // Uses the model and view to force a game over scenario.
  // Ensures the proper message is conveyed to the view.
  @Test
  public void testGameOver() {
    this.setup(new HumanPlayer());
    this.mockModel.notifyWin(0, 10);
    String appendableContent = this.viewAppendable.toString();
    Assert.assertEquals(appendableContent, "Game is over! Player R won with a score of: 10");
  }

  // test notifyPlayerTurn() using a machine player mock.
  // ensures the coordinate and hand index passed to the model is the same coordinate
  // and index instantiated for the player.
  @Test
  public void testMachinePlayerTurn() {
    Appendable modelAppendable = new StringBuilder();
    this.mockModel = new MockThreeTrioModel<>(modelAppendable);
    this.viewAppendable = new StringBuilder();
    ThreeTrioGameView mockView = new MockThreeTrioView<>(viewAppendable);
    PlayerActions machine = new MockMachinePlayer<>(new Coordinate(-1, -1), -1);
    ThreeTrioGameController<PlayableCard> controller =
            new ThreeTrioPlayerControllerImpl<>(mockModel, machine, mockView);
    this.mockModel.notifyPlayerTurn(0);
    String appendableContent = modelAppendable.toString();
    Assert.assertEquals("Play card: -1 at -1,-1Battle", appendableContent);

    PlayerActions machine2 = new MockMachinePlayer<>(new Coordinate(5, 4), 12);
    ThreeTrioGameController<PlayableCard> controller2 =
            new ThreeTrioPlayerControllerImpl<>(mockModel, machine2, mockView);
    this.mockModel.notifyPlayerTurn(1);
    String addedAppendableContent = modelAppendable.toString();
    Assert.assertEquals(appendableContent + "Play card: 12 at 5,4Battle", addedAppendableContent);

    PlayerActions machine3 = new MockMachinePlayer<>(new Coordinate(7, 1), 2);
    ThreeTrioGameController<PlayableCard> controller3 =
            new ThreeTrioPlayerControllerImpl<>(mockModel, machine3, mockView);
    this.mockModel.notifyPlayerTurn(2);
    Assert.assertEquals(addedAppendableContent + "Play card: 2 at 7,1Battle",
            modelAppendable.toString());
  }

  // test notifyPlayerTurn() using a human player
  // simulates a player clicking on the view to select a card then cell after 1 second
  // ensures the coordinate and hand index passed to the model is the same coordinate
  // and index instantiated for the player.
  @Test
  public void testHumanPlayerTurn() {
    this.setup(new HumanPlayer());
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // do these actions after 1 seconds to simulate player actions
    scheduler.schedule(() -> {
      this.mockView.handCardClick(0, 0);
      this.mockView.handCellClick(1, 4);
    }, 1, TimeUnit.SECONDS);

    this.mockModel.notifyPlayerTurn(0);
    Assert.assertEquals("Play card: 0 at 1,4Battle", this.modelAppendable.toString());
  }

  // test clearSelectedCard() clears the selected card
  @Test
  public void testClearSelectedCard() {
    this.modelAppendable = new StringBuilder();
    this.mockModel = new MockThreeTrioModel<>(modelAppendable);
    this.viewAppendable = new StringBuilder();
    this.mockView = new MockThreeTrioView<>(viewAppendable);
    ThreeTrioGameController<PlayableCard> controller =
            new ThreeTrioPlayerControllerImpl<>(mockModel, new HumanPlayer(), mockView);

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // do these actions after 1 seconds to simulate player actions
    scheduler.schedule(() -> {
      this.mockView.handCardClick(0, 0);
      controller.clearSelectedCard();
      this.mockView.handCellClick(1, 4);
      this.mockView.handCardClick(0, 0);
      this.mockView.handCellClick(1, 4);
    }, 1, TimeUnit.SECONDS);

    this.mockModel.notifyPlayerTurn(0);
    Assert.assertEquals("Please select a card first.", this.viewAppendable.toString());
    Assert.assertEquals("Play card: 0 at 1,4Battle", this.modelAppendable.toString());
  }
}
