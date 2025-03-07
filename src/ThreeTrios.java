import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.Colors;
import model.OurReadToProviderReadADT;
import model.ThreeTrioGameModel;
import model.ruled.FallenAceRule;
import model.ruled.FlipGreaterRule;
import model.ruled.PlusRule;
import model.ruled.ReverseRule;
import model.ruled.RuledThreeTrioModel;
import model.ruled.SameRule;
import model.ruled.ThreeTrioRule;
import player.HumanPlayer;
import player.MachinePlayer;
import player.PlayerADT;
import player.PlayerActions;
import controller.ThreeTrioPlayerControllerImpl;
import filereaders.ReadCardConfig;
import filereaders.ReadGridConfig;
import model.ModelFeatures;
import model.UpdatedThreeTrioModel;
import model.PlayableCard;
import provider.model.inter.Player;
import provider.model.inter.ReadOnlyThreesTrioModel;
import provider.view.implementations.ThreesTrioFrame;
import provider.view.inter.ThreesTrioView;
import strategy.CornerStrategy;
import strategy.FailableThreeTrioStrategy;
import strategy.FlipMostCardsStrategy;
import strategy.InfailableThreeTrioStrategy;
import strategy.LeastExposedStrategy;
import strategy.MinMaxStrategy;
import view.JFrameView;
import view.ThreeTrioGameView;

/**
 * The main class which is currently a placeholder to run our view.
 * Can simulate a few game moves to be at a non-trivial intermediate point of the game
 * and then displays the game board in a JFrame.
 */
public final class ThreeTrios {

  /**
   * The main method to run a game of Three Trio.
   *
   * @param args Arguments to be passed to main.
   */
  public static void main(String[] args) {
    // HW9 calls


    String[] ruleArguments = Arrays.copyOfRange(args, 2, args.length);
    Set<ThreeTrioRule> rules = ThreeTrios.parseRules(ruleArguments);

    ThreeTrioGameModel<PlayableCard> ruledModel = new RuledThreeTrioModel(rules);
    ModelFeatures<PlayableCard> model = new UpdatedThreeTrioModel(ruledModel);
    ReadGridConfig readGrid = new ReadGridConfig("BoardConfig/HoleAndCardCellBoard");
    //    ReadCardConfig readCard = new ReadCardConfig("CardConfig/EnoughForAnyBoardsAce");
    ReadCardConfig readCard = new ReadCardConfig("CardConfig/ForPlus");

    PlayerActions playerOne = ThreeTrios.parseInput(model, args[0]);
    PlayerActions playerTwo = ThreeTrios.parseInput(model, args[1]);


    // We need to start the game before creating the second view because their impl calls methods
    // that only work post-start game.
    model.startGame(readGrid.parseGrid(), readCard.parseCards());

    ThreeTrioGameView view1 = new JFrameView<>(model);
    ThreeTrioPlayerControllerImpl<PlayableCard> controller1 =
            new ThreeTrioPlayerControllerImpl<>(model, playerOne, view1);

    ThreeTrioGameView view2 = new JFrameView<>(model);
    ThreeTrioPlayerControllerImpl<PlayableCard> controller2 =
            new ThreeTrioPlayerControllerImpl<>(model, playerTwo, view2);


    //    ThreeTrioGameView view2 = new ProviderViewAdapter(makeView(model,
    //    playerOne, playerTwo), 1);
    //    ThreeTrioPlayerControllerImpl<PlayableCard> controller2 =
    //            new ThreeTrioPlayerControllerImpl<>(model, playerTwo, view2);

    // Informs listeners the game has started.
    model.informStartListener();
  }

  private static ThreesTrioView makeView(ModelFeatures<PlayableCard> model, PlayerActions playerOne,
                                         PlayerActions playerTwo) {
    ReadOnlyThreesTrioModel adaptedModel = new OurReadToProviderReadADT(model);
    Player adaptedPlayerOne = new PlayerADT(playerOne, adaptedModel, Colors.Red);
    Player adaptedPlayerTwo = new PlayerADT(playerTwo, adaptedModel, Colors.Blue);
    adaptedModel = new OurReadToProviderReadADT(model, adaptedPlayerOne, adaptedPlayerTwo);
    return new ThreesTrioFrame(adaptedModel, adaptedPlayerTwo);
  }

  // Creates a PlayerAction depending on the input parameters.
  private static PlayerActions parseInput(ModelFeatures<PlayableCard> model,
                                          String assignmentValue) {
    switch (assignmentValue) {
      case "human":
        return new HumanPlayer();
      case "strategy1":
        FailableThreeTrioStrategy<PlayableCard> corner = new CornerStrategy<>();
        return new MachinePlayer<>(model, corner);
      case "strategy2":
        FailableThreeTrioStrategy<PlayableCard> flip = new FlipMostCardsStrategy<>();
        return new MachinePlayer<>(model, flip);
      case "strategy3":
        InfailableThreeTrioStrategy<PlayableCard> leastExposed = new LeastExposedStrategy<>();
        return new MachinePlayer<>(model, leastExposed);
      case "strategy4":
        InfailableThreeTrioStrategy<PlayableCard> minMax = new MinMaxStrategy<>();
        return new MachinePlayer<>(model, minMax);
      default:
        throw new IllegalArgumentException("Error: Invalid input. Valid inputs include: 'human'"
                + "'strategy1', 'strategy2', 'strategy3', 'strategy4'");
    }
  }

  // Create rules based on the remaining inputs.
  private static Set<ThreeTrioRule> parseRules(String[] args) {
    if (args.length == 0) {
      return Set.of(new FlipGreaterRule()); // Returns default upon no input
    } else {
      Set<ThreeTrioRule> rulesToAdd = new HashSet<>();
      for (String arg : args) {
        rulesToAdd.add(parseRule(arg));
      }
      if (!(rulesToAdd.contains(new FlipGreaterRule())
              || rulesToAdd.contains(new ReverseRule())
              || rulesToAdd.contains(new FallenAceRule()))) {
        rulesToAdd.add(new FlipGreaterRule());
      }
      return rulesToAdd;
    }
  }

  private static ThreeTrioRule parseRule(String input) {
    switch (input) {
      case "default":
        return new FlipGreaterRule();
      case "reverse":
        return new ReverseRule();
      case "fallen-ace":
        return new FallenAceRule();
      case "same":
        return new SameRule();
      case "plus":
        return new PlusRule();
      default:
        throw new IllegalArgumentException("Error: Invalid rule. Rules include: " +
                "'default', 'reverse', 'fallen-ace', 'same', 'plus'");
    }
  }
}