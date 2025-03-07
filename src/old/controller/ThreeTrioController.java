package old.controller;

import java.io.File;

import filereaders.ReadCardConfig;
import filereaders.ReadGridConfig;
import model.PlayableCard;
import model.ThreeTrioGameModel;
import view.Features;
import view.GameView;

/**
 * Represents a controller for a ThreeTrioGameModel.
 * Takes in user input for controlling the game. Prints out who clicked a card and
 * the index of the card clicked. Also prints out coordinate of cell clicked.
 */
public class ThreeTrioController implements ThreeTrioGameController<PlayableCard>, Features {

  /**
   * Default constructor for a ThreeTrioController.
   */
  public ThreeTrioController() {
    // empty because we are currently only constructing a controller
    // to take in grid file name and card file name to parse to pass to model.
  }

  @Override
  public void setView(GameView view) {
    view.setFeature(this);
  }

  @Override
  public void startGame(ThreeTrioGameModel<PlayableCard> model, String gridName, String cardName) {
    // hw6 code below
    ReadGridConfig readGridConfig =
            new ReadGridConfig("BoardConfig" + File.separator + gridName);
    ReadCardConfig readCardConfig =
            new ReadCardConfig("CardConfig" + File.separator + cardName);
    model.startGame(readGridConfig.parseGrid(), readCardConfig.parseCards());
  }

  @Override
  public void handleCardClick(int playerIndex, int cardIndex) {
    System.out.println("Player " + (playerIndex + 1) + " clicked on card index " + cardIndex);
  }

  @Override
  public void handleCellClick(int row, int col) {
    System.out.println("Cell clicked at coordinates: (" + row + ", " + col + ")");
  }

  @Override
  public void clearSelectedCard() {
    // does nothing as this method will never be called
  }

  @Override
  public void toggleHint(char letter) {
    // intentionally left blank
  }

}