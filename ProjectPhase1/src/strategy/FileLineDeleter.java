package strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * The FileLineDeleter class.
 * This is the class for deleting a line from a given class.
 *
 * @author Make Zhang
 */
public class FileLineDeleter implements FileModifier {

  /**
   * Modify a given file by deleting the given line.
   *
   * @param fileName the given file
   * @param line the line we want to delete
   */
  @Override
  public void modifyFile(String fileName, String line) {
    try {
      File tempFile = new File(fileName.split("\\.")[0] + "-temp.txt");
      if (!tempFile.exists()) {
        tempFile.createNewFile();
      }
      FileWriter fw = new FileWriter(tempFile, true);
      BufferedWriter bw = new BufferedWriter(fw);
      File originalFile = new File(fileName);
      Scanner scanner = new Scanner(new FileInputStream(fileName));
      boolean alreadyDeleteLine = false;
      while (scanner.hasNextLine()) {
        String currentLine = scanner.nextLine();
        if (currentLine.equals(line) && !alreadyDeleteLine) {
          alreadyDeleteLine = true;
        } else {
          bw.write(currentLine);
          bw.write("\n");
        }
      }
      bw.close();
      scanner.close();
      originalFile.delete();
      tempFile.renameTo(originalFile);
    } catch (IOException exception) {
      System.err.println("An IOException was caught");
      exception.printStackTrace();
    }
  }
}
