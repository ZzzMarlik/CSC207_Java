package strategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The FileToStringTransformer class.
 * Transfer a given file to a String that can be print out
 *
 * @author Make Zhang
 */
public class FileToStringTransformer {

  /**
   * The method that can transfer a file to string format.
   *
   * @param fileName given file name
   * @return String
   */
  public String fileToString(String fileName) {
    StringBuilder result = new StringBuilder();
    try {
      Scanner scanner = new Scanner(new FileInputStream(fileName));
      while (scanner.hasNextLine()) {
        result.append(scanner.nextLine()).append("\n");
      }
      scanner.close();
    } catch (IOException exception) {
      System.out.println("catch IOException");
    }
    return result.toString();
  }

}
