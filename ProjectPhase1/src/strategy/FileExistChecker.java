package strategy;

import java.io.File;

/**
 * The FileExistChecker class.
 * This is the class for checking a file is exist or not
 *
 * @author Make Zhang
 */
public class FileExistChecker {

  /**
   * The method to check whether the file is exits.
   *
   * @param fileName Given file to check
   * @return boolean
   */
  public boolean fileExistCheck(String fileName) {
    File file = new File(fileName);
    return file.exists();
  }

}
