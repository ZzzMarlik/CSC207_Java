package strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The FileLineAppender class.
 * This is the class for appending a new line for a given file
 *
 * @author Make Zhang
 */
public class FileLineAppender implements FileModifier {

  /**
   * modfiy a file with appending the given new line.
   *
   * @param fileName the given file
   * @param line the new line
   */
  @Override
  public void modifyFile(String fileName, String line) {
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
      bw.write(line);
      bw.write("\n");
      bw.close();
    } catch (IOException exception) {
      System.out.println("catch IOException");
    }
  }
}
