package filereaders;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileNotFoundException;

import model.CardCell;
import model.Cells;
import model.HoleCell;

/**
 * Reads the grid configuration file to initialize grid.
 */
public class ReadGridConfig {
  private final Scanner scanner;

  /**
   * Attempts to read the grid configuration file.
   * @param fileName The name of the file
   * @throws IllegalArgumentException If the given file is not found.
   */
  public ReadGridConfig(String fileName) {
    try {
      this.scanner = new Scanner(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Error: file not found");
    }
  }

  /**
   * Parses the grid from the given file.
   * @return A grid the user will play on represented as a list of Cells nested in a list.
   * @throws IllegalArgumentException If the given file does not have an odd number of CardCell
   * @throws IllegalArgumentException If specified number of rows doesn't match actual number of
   *                                  given rows.
   * @throws IllegalArgumentException A row doesn't have the same number of columns as the specified
   *                                  number of columns.
   * @throws IllegalArgumentException There is a character other than 'X' or 'C' in the given grid.
   */
  public List<List<Cells>> parseGrid() {
    List<List<Cells>> grid = new ArrayList<List<Cells>>();
    int numRows = this.scanner.nextInt();
    int numCols = this.scanner.nextInt();
    this.scanner.nextLine();

    int numCardCell = 0;

    for (int row = 0; row < numRows; ++row) {
      if (!this.scanner.hasNextLine()) {
        throw new IllegalArgumentException("Error: Specified number of rows doesn't match"
                + "actual number of rows");
      }
      String nextLine = this.scanner.nextLine();

      if (nextLine.length() != numCols) {
        throw new IllegalArgumentException("Error: A row does not have the same number of"
                + " columns as the specified number of columns");
      }

      ArrayList<Cells> cellsToAdd = new ArrayList<>();
      for (int col = 0; col < numCols; ++col) {
        char holeOrCell = nextLine.charAt(0);
        if (holeOrCell == 'X') {
          cellsToAdd.add(new HoleCell());
        }
        else if (holeOrCell == 'C') {
          cellsToAdd.add(new CardCell());
          numCardCell++;
        }
        else {
          throw new IllegalArgumentException("Error: Given value is not a hole or card cell."
          + " Value given: " + holeOrCell);
        }
        nextLine = nextLine.substring(1);
      }
      grid.add(cellsToAdd);
    }
    if (this.scanner.hasNextLine()) {
      throw new IllegalArgumentException("Error: Specified number of rows doesn't match"
              + "actual number of rows");
    }
    if (numCardCell % 2 == 0) {
      throw new IllegalArgumentException("Grid must contain an odd number of card cells");
    }
    return grid;
  }
}
