package player;

import org.junit.Assert;
import org.junit.Test;

import model.PlayableCard;
import model.ReadOnlyThreeTrioGameModel;
import model.ThreeTrioGameModel;
import model.ThreeTrioModel;
import old.controller.ThreeTrioController;
import old.controller.ThreeTrioGameController;
import strategy.CornerStrategy;
import strategy.FailableThreeTrioStrategy;

/**
 * A test class to test the public methods of the HumanPlayer and MachinePlayer classes.
 */
public class TestPlayers {
  // Builds a valid game using a predefined board and card set.
  private ThreeTrioGameModel<PlayableCard> buildValidGame(String boardName, String cardName) {
    ThreeTrioGameController<PlayableCard> controller = new ThreeTrioController();
    ThreeTrioGameModel<PlayableCard> model = new ThreeTrioModel();
    controller.startGame(model, boardName, cardName);
    return model;
  }

  private MachinePlayer<PlayableCard> createMachinePlayer() {
    ReadOnlyThreeTrioGameModel model = buildValidGame("3X3Grid", "EnoughForAnyBoards");
    FailableThreeTrioStrategy strat = new CornerStrategy();
    return new MachinePlayer<>(model, strat);
  }

  // test HumanPlayer's playCard() throws UnsupportedOperationException tries to use the method
  @Test (expected = UnsupportedOperationException.class)
  public void testPlayCardHuman() {
    PlayerActions human = new HumanPlayer();
    human.playCard(0);
  }

  // test that HumanPlayer's isMachine() returns false
  @Test
  public void testIsMachineHuman() {
    PlayerActions human = new HumanPlayer();
    Assert.assertFalse(human.isMachine());
  }

  // test that MachinePlayer's isMachine() returns true
  @Test
  public void testIsMachineMachine() {
    PlayerActions machine = createMachinePlayer();
    Assert.assertTrue(machine.isMachine());
  }

  // test MachinePlayer's playCard()
  @Test
  public void testPlayCardMachine() {
    PlayerActions machine = createMachinePlayer();
    Assert.assertEquals(0, machine.playCard(0).get(0).getKey().getX());
    Assert.assertEquals(2, machine.playCard(0).get(0).getKey().getY());
    Assert.assertEquals(0, machine.playCard(0).get(0).getValue().intValue());
  }
}
