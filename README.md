# Three Trios Game
A Java based GUI application that allows for a card game played through player vs. player, AI vs. AI,
or a combination of both.

## Features ✨
- Developed using the model-view-controller pattern using Java 📱
- Utilized abstraction and object-oriented design patterns 🖥️
  - Adapter pattern, Decorator pattern, Observer pattern, and Strategy pattern
- Validated methods work as specified using mocks and JUnit tests 🧪

## Game Instructions
XYZ

### Special Rules
XYZ

### Board Configuration File
XYZ

### Card Configuration File

## Example
XYZ

## How to Run 🚀
- Ensure you have JDK 11 or later installed.
- Make sure JUnit 4 is available for testing

1. git clone https://github.com/wong-bria/ThreeTrios.git
2. Open the project in the IDE of your choice.
3. Edit configurations and arguments. (Steps detailed in Arguments section)
4. Click run with configurations.

### Arguments
XYZ

Overview: This codebase is trying to solve the problem of implementing a custom card game called Three
Trios with custom game rules and mechanics, turn-based gameplay, and game state management.
Some high-level assumptions made in the codebase are that the user is familiar with grid-based
systems and basic list operations. More specifically, the grid a player plays on
is 0 index based where the origin starts at (0,0) in the top left, as row increases it moves
downward on the grid, and as column increases it moves right on the grid. Also, hands are a list
where when cards slide down left when used. Both these high-level assumptions are require
background knowledge but are documented in comments as reminders.

Quick start:
@Test
  public void QuickStartToUseCodebase() {
    // initialize a model
    ThreeTrioModel model = new ThreeTrioModel();
    // start the game with a board configuration file to be the grid played on
    // alongside a card configuration file to be the cards used in the game.
    model.startGame("BoardConfig" + File.separator + "HoleAndCardCellBoard",
            "CardConfig" + File.separator + "EnoughForAnyBoards");
    // initialize a view, taking in a model and Appendable, to display the game
    ThreeTrioGameView view = new ThreeTrioGameTextView(model, new StringBuilder());

    // allow player1 to play a card from their hand onto a (row,col) on the board
    model.playCard(0, 0, 2);
    // have the card battle neighboring card to the north, south, east, west
    model.battle();
    // allow player2 to play a card
    model.playCard(11, 0, 3);
    model.battle();

    // how a working game can look
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



Key components:
Some key components are the file readers, model, and view, and controller.

The controller isn't implemented yet but is the component that will drive the control-flow of our
system. It will handle interactions between the model, view, and user input/output. It also
controls the flow of the game by processing commands, updating game states, and creating outputs
to be displayed.

The file readers are driven by the model because they are called in the model's
startGame to create a list of cards and a grid of cells to be used in the game respectively.
The model is driven by the controller since the controller responds to user inputs and makes
requests to the model to either update game states or retrieve data. The model also drives
because when a game state changes, the model notifies the view to update the display.
The view is driven by the model because when the model changes, the view will update itself to
reflect the changes and match the game state in the model.

Key subcomponents:
In file readers, key subcomponents: ReadCardConfig and ReadGridConfig.
    - ReadCardConfig: the main noun is card. It exists to read
      a card configuration file given by the user. It is used to create a list of cards to be played in
      the game.
    - ReadGridConfig: the main noun is grid. It exists to read a grid configuration file given by the user.
      It is used to create a list of list of cells.

In view, key subcomponent: ThreeTrioGameView.
    - ThreeTrioGameView: the main noun is game view. It exists and is used to display the current
      game state of a game of Three Trios.

In model, key subcomponent: Player, Cells, and Card.
    - Player: the main nouns are hand and color. It exists to represent a player playing a game of
      Three Trios. It is used to hold a list of cards and a color to represent which cards
      on a board belongs to them.
    - Cells: the main nouns are card and neighbors. It exists to represent either a card cell or hole cell.
      Cells are used to build a grid that the user will play on where if a cell is a hole cell, a user can't
      play a card on it, but if it's a card cell, then a user can play a card on it.
    - Card: the main nouns are name, color, and attack value. It exists to represent a card that holds
      information such as its name, color, and attack value in its north, south, east, and west directions.
      It is used to be played on a grid for a user to try and win a game of Three Trios.


Source organization:
    - The file readers component can be found by opening the hw5 folder, then the src folder, and
      finally the filereaders folder.
    - The model component can be found by opening the hw5 folder, then the src folder, and
      finally the model folder.
    - The view component can be found by opening the hw5 folder, then the src folder, and finally
      the view folder.

Each of these components will also contain their key subcomponents inside of their respective
folders.

We decided to put ReadCardConfig and ReadGridConfig in a filereaders package because they
are focused on configuration and not directly modifying or maintaining game states,
handling user inputs, or displaying a view, thus it wouldn't be part of the model, controller,
 or view package but its own package instead.

 A card is in the format: String int int int int , or Name northValue southValue eastValue westValue

Invariant inside of ThreeTrioModel:
    - Invariant: this.turn is always within the index bounds of the number of players. (0 or 1).

 *** Changes for part 2 ***

 In our model, we changed "this.grid.addAll(new ReadGridConfig(gridFile).parseGrid());"
 and "this.deck.addAll(new ReadCardConfig(cardFile).parseCards());" to
 "this.grid = new ArrayList<>(new ReadGridConfig(gridFile).parseGrid());" and
 "this.deck = new ArrayList<>(new ReadCardConfig(cardFile).parseCards());" because it makes it more
 clear that we are creating a copy of the given grid and cards.

We also added the observations: getScore, getFlipCount, checkLegal, getCardOwner, getPlayerHand,
getContentAtCell, getGridLength, and getGridWidth.
    - getScore: This ability to observe a player's score in a game was missing because we didn't
    think anyone needed to see the score except for the model to update its game state after
    a battle to determine which player won. Thus, we had a private helper named determineWinner
    which similar to getScore except it calculates the score of both players and uses the
    results to determine a winner instead of only getting a score for a given player.
    We chose to add getScore to add this missing functionality by taking in an index,
    0 index based, to represent the player we are finding the score for, then
    having a local variable that is set to the amount of cards in the interested player
    which is also incremented for every card on the board that has the same color as
    the player we are interested in.
    - getFlipCount: This ability to check how many cards a player can flip by playing a card at
     a given coordinate was missing because we think of this ability. We added this missing
     functionality by getting the cell the player getting a copy of grid, putting
     the specified card from the specified player onto the copy of the grid, getting a cell
     based on given coordinate, and returning the result of a private helper that takes in that
     cell. The private helper has a local variable to keep count of flips that is initialized to 0.
     It then looks at all its neighbors then flips the neighboring cards
     that are the opposite color with a lower attack value and increments the variable flips by one,
     and continues at the card that was just flipped.
    - checkLegal: This ability to observe if it was legal for the current player to play at a
     given coordinate was missing because we made the decision to throw an exception if
     the current player tries to play at a coordinate that is not legal and to just put
     the card onto a the grid at the given coordinate if it is legal, thus the player
     wouldn't need to see if their move is legal or not. We decided to add this missing
     functionality by checking if the given coordinate is in the grid, is not going to be
     on a hole cell, and is not going to be on a card cell that already has a card on it.
    - getCardOwner: This ability to check which player owns a card in a cell at a given
    coordinate was missing because we never needed to check the owner a card at a random
    location. When we did check the owner of a card on the grid, we would check every single
    cell on the grid with nested for each loops and then check the color of the card
    at a cell to determine the owner. We added this missing functionality by checking if
    the given cell has a card. If it does, then get the card's color to determine the owner.
    - getPlayerHand: This ability to check what the contents of a player’s hand was missing
    because we thought it was only necessary to check the contents of the current player's hand.
    We added this missing functionality by returning the hand of the specified player based
    on given player index.
    - getContentAtCell: This ability to check what the contents of a cell at a given coordinate
    was missing because we forgot we could abstract this ability to a method. We implemented this
    missing functionality by returning Optional.empty() if a cell has nothing in it. Otherwise,
    return Optional.of(X) where X is a card in the cell at a given coordinate.
    - getGridLength: This ability to check how big the grid is in terms of length was missing
    because we didn't think of much situations that required this ability. We added this missing
    functionality by checking the size of the grid.
    - getGridWidth: This ability to check how big the grid is in terms of width was missing
    because we didn't think of much situations that required this ability. We added this missing
    functionality by checking the size of the first row in the grid.

    In our card interface, instead of using four separate methods to return the four different
    attack values, we replaced them with a method that takes in a direction and returns the
    attack value at that direction.

    For the cell interface, we also added copyOf method to create a copy of a cell. We did this
    because in ThreeTrioModel, we changed our getGrid to use for each loops, which required
    creating a copy of a cell, instead of just passing in the model's grid into a new array.

*** New Classes for Hw6 ***
    - ThreeTrios: Can be found inside the src folder. This class is currently used as a palceholder
    to run our view so we are able to see a visual representation of a Three Trio game.

    - Inside of strategy package:
        - Coordinate: The main noun is coordinate. It exists to represents a coordinate which
         contains a row and col in a grid. It is used to represent a location on a grid
         that a strategy should return.
        - CornerStrategy: A strategy that someone can use that prioritizes the placement of
        cards on corners. If no corners are available, place card from hand at index 0 at
        the uppermost, leftmost available cell.
        - FlipMostCardsStrategy: A strategy that someone can use that prioritizes the placement
        of cards on cells that will flip the most cards. If no flips are possible
        (a flip count of 0), place card from hand at index 0 at the uppermost, leftmost
        available cell.
        - LeastExposedStrategy: A strategy that someone can use that prioritizes the placement of
        cards on cells that expose the least amount directions of a card with the card that
        is hardest to flip. Best moves are returned in a list.
        - MinMaxStrategy: A strategy that someone can use that prioritizes the placement of
        cards on cells that minimizes the maximum flips an opponent can get the next turn.
        Best moves are returned in a list.
        - Tuple: Used to represent a tuple data type, which stores keys with corresponding
        values.

    - Inside of view package:
        - CardToJButton: The main noun is button. It exists to represent a card as a button.
        It is used to represent a card in a player's hand as as button which will allow
        users to interact with the cards in a hand.
        - GameGridPanel: The main noun is grid. It exists to represent a grid. It is used to
        represent a grid which users can interact with.
        - JFrameView: The main noun is frame. It exists to represent a frame containing
        two player hand panels and a grid. It is used to represent a frame containing
        all the information a player would see when playing a Three Trio game.
        - PlayerHandPanel: The main noun is hand. It exists to represent a panel
        which will be a player's hand. It is used to represent a player's hand containing
        cards.

    - Inside of controller package:
        - ThreeTrioController: The controller takes in the file name for a board and deck
        of card to be parsed and passed to the model.

*** Changes for Part 3 ***
    - Added a new ModelFeatures interface and implementation
        - This interface serves as an expansion of ThreeTrioModel, where it pings
          listeners (controllers) of updates as they occur.
        - The implementation is an extension of the original model, since this is
          an enhanced ThreeTrioModel that more easily supports controllers.
    - Changed access modifier of GameState field and GameState enum in ThreeTrioModel
        - Since our new interfaces needs to know the state of the game when it's over
          (figure out who won) we changed the state from private to protected.
    - Changed return type of getTurn() from Colors to int in ReadOnlyThreeTrio...
        - Makes it significantly easier to determine which controller's turn it is and
          another method (getPlayerColor) accommodates the loss of the original return type.
    - Refactored our previous Player interface and HumanPlayer implementation to ModelPlayer
        - Our interface and implementation represented the state a player is in rather than
          an actual player. By this, we mean that a player strictly represented a color and
          a deck. We figured this would be more adjacent to a "Model representation" of a
          player, and have renamed it as such.
    - Added a new PlayerActions interface
        - This interface represents an action a player can take (i.e. select a card and tile).
          It includes a method that identifies whether or not the player partaking in the action
          is a Human or Machine. Although this method can be removed, it serves to simplify the
          logic in the controller by excluding instanceOf.
        - Added a new class HumanPlayer (implements PlayerActions)
            - Currently, since a HumanPlayer relies on the view to interact with the controller,
              a HumanPlayer object will throw an UnsupportedOperationException on calls to
              playCard.
        - Added a new class MachinePlayer (implements PlayerActions)
            - Calls to playCard cause the machine player to cook up a position and card to play
              on the board. Since a machine player doesn't rely on a view, it relies on conveying
              the information of card selection and placement via this interface.
    - Added a new ModelNotificationListener interface and implementation
        - Purpose is to notify the controller of events that occur in the model:
            - notifyGameStateUpdated() notifies the controllers of when to update the view
            - notifyPlayerTurn() notifies the controllers of whose turn it is
            - notifyInvalidMove() notifies the controllers to attempt another play when selecting
              an invalid position.
            - notifyGameOver() notifies the controllers to stop implementing inputs and outputs
              that would modify the game state.
    - Added a new Controller interface and class (ThreeTrioGameController(Impl))
        - Controller class derives methods from ModelNotificationListener and Features, as it is
          a model notification listener and a view listener.
        - Controller class alters the model's game state by taking in Human input via the model
          and machine input via the output of a strategy.
        - Controller class relies on a Model, PlayerAction, and View.
    - Moved old controller to old.controller
        - Primarily used for model instantiation without having to deal with
          ReadGridConfig/ReadCardConfig
    - Added displayErrMsg() and displayWinMsg() to the ThreeTrioGameView interface
        - Decouples the view from the controller, where our controller no longer
          creates a new JOptionPlane for displaying errors or win messages. Instead, delegates
          the display functions to the view.
        - Purpose behind having two methods is the difference in display, where our JFrameView
          creates an ERROR_MESSAGE JOptionPlane for errors and a PLAIN_MESSAGE JOptionPlane for
          win messages.
    - Overhauled the constructors and method calls in JFrameView, GridPanel, and PlayerHandPanel
        - JFrameViews can now be instantiated without error when a model has not already started
        - JFrameView, GridPanel, and PlayerHandPanel update renders upon request
            - JFrameView does this via the render() method
            - GridPanel does this via the updateCells() method
            - PlayerHandPanel does this via the updatePanel() method.
        - Removed toggleHighlight() from JFrameView
            - This was predominantly a PlayerHandPanel feature, but was added for testing prior.
        - Parameterized JFrameView and PlayerHandPanel to support all classes extending model.Card
    - Added clearSelectedCard() in Features
        - Serves its purpose by telling the listener (controller) to clear the card its
          currently holding when the card is de-selected.

*** Assignment 8 ***
    - Make sure to clearly document your new command-line options in your README,
      so graders know how to run your game.
      Our command-line options are: X Y
      where X and Y can be human, strategy1, strategy2, strategy3, or strategy4

    - Which features of the provided views you were able to get working,
     and which ones you were not:
     The only features of the provided view that we had to get working were in the provider's
     PlayerActionFeatures interface. In that interface, it had three methods and we were
     able to get all three working properly.

*** Assignment 9 ***
    - WHAT FEATURES WE ADDED AND WHAT FILES WERE AFFECTED?
        - We added all the extra credit features
    - For level 0, we added a new feature that decorated the grid and allowed a user to toggle
      between having hints turned on or off. The files that were affected include
      ThreeTrioPlayerControllerImpl, JFrameView, and GameGridPanel. For hints to be toggled between
      on and off, player one would click 'q' and player two would click 'w'. For the hints to appear,
      the user must toggle hints on, then click on a card in their hand.
    - Added new rules to the game under model.ruled
        - The rule implementations follow the ThreeTrioRule interface, which has the following methods:
            - SatisfiesFlip: Returns true if the Cell, Neighboring Cell, and Direction to Neighbor satisfy
              the rule's flip condition.
            - ReverseSatisfiesFlip: Returns true if the intended inverse effect is reached. Some rules like
              FallenAce would accept nearly all plays as flips if we simply take the inverse of the
              output of SatisfiesFlip, so this method serves to differentiate the intended functions.
            - IsMutuallyExclusive: Returns true if the given rule and the current rule are mutually exclusive.
            - allowCombo: Returns true if this rule can be applied after combos (i.e. to battle after placed card).
        - FallenAceRule implements the Regular rule and Fallen Ace, where newly placed cards
          with greater opposing values overtake cards AND 1 overtakes Ace. The reverse is also
          true, where newly placed cards with smaller opposing values overtake cards AND Ace
          overtakes 1.
        - FlipGreaterRule implements the default rules of the game. Newly placed cards with
          greater values overtake old cards and vice versa if the game is reversed.
        - PlusRule implements a rule where cards are flipped if at least two opposing pairs of
          adjacent cards have the same sum with the current card in their respective opposing
          directions. Reversing this rule does nothing, since there is nothing to reverse (it
          would allow ALL cards to flip except those with an opposing sum of 10, which would
          ruin the flow of the game)
        - ReverseRule implements the reverse of the original rules. It acts functionally
          identically to FlipGreater, but instanceof is used to notify the model that
          the reverseSatisfiesFlip method is being used.
        - SameRule implements a new rule where cards with the same opposing values as
          the card played are flip IFF at least 2 cards satisfy this condition. The reverse
          acts functionally the same as the non-reversed version.
    - Added new RuledThreeTrioModel, which works nearly identically to ThreeTrioModel.
        - Rather than update and possibly break our existing ThreeTrioModel, we extended our existing
          implementation and added the new rule functionality to the implementation. This allows for the
          models to co-exist without having to reimplement all the methods.
    - Changed UpdatedThreeTrioModel to become a wrapper.
        - Rather than add a whole new model for controller support (to support our new ruled model), we made
          our UpdatedThreeTrioModel a wrapper. It now uses composition, taking in an existing model and
          delegating all functions to that model. We kept both constructors to allow for previous
          implementations to co-exist with current implementations.

    - removed tests in package called old, strategy because we have over 125 files