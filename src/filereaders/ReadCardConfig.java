package filereaders;

import java.util.HashSet;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Set;

import model.Numbers;
import model.PlayableCard;

/**
 * Reads the card configuration file to initialize the deck of cards to use.
 */
public class ReadCardConfig  {
  private final Scanner scanner;

  /**
   * Attempts to read the given card configuration file to initialize the deck
   * of cards to be played with.
   *
   * @param fileName The name of the file.
   * @throws IllegalArgumentException If the given file is not found.
   */
  public ReadCardConfig(String fileName) {
    try {
      this.scanner = new Scanner(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Error: File not found.");
    }
  }

  /**
   * Parses the cards from file given in the constructor.
   *
   * @return A list of playable cards to be played with.
   * @throws IllegalArgumentException if two cards have the same name
   * @throws IllegalArgumentException if given card value is < 1 or > 10.
   * @throws IllegalArgumentException if a line is not in the format String int int int int
   * @throws IllegalArgumentException if a line does not contain four int as attack values
   **/
  public List<PlayableCard> parseCards() {
    List<PlayableCard> cards = new ArrayList<>();
    Set<String> cardNames = new HashSet<>();

    while (this.scanner.hasNextLine()) {
      String line = this.scanner.nextLine().trim(); // use trim in case user does "   name ..."
      String[] parts = line.split("\\s+");

      if (parts.length != 5) {
        throw new IllegalArgumentException("Invalid format: Expected: String int int int int");
      }

      String cardName = parts[0];

      try {
        Numbers northVal = numToEnum(Integer.parseInt(parts[1]));
        Numbers southVal = numToEnum(Integer.parseInt(parts[2]));
        Numbers eastVal = numToEnum(Integer.parseInt(parts[3]));
        Numbers westVal = numToEnum(Integer.parseInt(parts[4]));

        if (!cardNames.add(cardName)) {
          throw new IllegalArgumentException("Each card must have a unique name!");
        }

        PlayableCard card = new PlayableCard(cardName, northVal, southVal, eastVal, westVal);
        cards.add(card);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("A line must contain 4 integers");
      }
    }

    return cards;
  }

  /**
   * Turns a valid integer into an enum.
   *
   * @throws IllegalArgumentException if the integer is not valid.
   **/
  private Numbers numToEnum(int num) {
    switch (num) {
      case 1:
        return Numbers.One;
      case 2:
        return Numbers.Two;
      case 3:
        return Numbers.Three;
      case 4:
        return Numbers.Four;
      case 5:
        return Numbers.Five;
      case 6:
        return Numbers.Six;
      case 7:
        return Numbers.Seven;
      case 8:
        return Numbers.Eight;
      case 9:
        return Numbers.Nine;
      case 10:
        return Numbers.A;
      default:
        throw new IllegalArgumentException("Illegal number given.");
    }
  }


}
