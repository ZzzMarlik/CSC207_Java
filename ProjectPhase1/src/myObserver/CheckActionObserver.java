package myObserver;

import java.util.Observable;
import java.util.Observer;
import strategy.FileModifier;

/**
 * The CheckActionObserver class.  This observer updates new action
 * instruction from user to text file and checks key word to
 * decide whether it is allowed to update.
 *
 * @author Haolin Zhang
 */
public class CheckActionObserver implements Observer {

  /**
   * File name of the file to be updated.
   */
  private String fileName;

  /**
   * The key word to check.
   */
  private String actionWord;

  /**
   * The strategy to modify the file.
   */
  private FileModifier fileModifier;

  /**
   * Allocates a new CheckActionObserver with fileName,
   * actionWord and fileModifier.
   *
   * @param newFileName the file name of the file to be updated.
   * @param newActionWord the key word to check.
   * @param newFileModifier the strategy to modify the file.
   */
  public CheckActionObserver(String newFileName, String newActionWord,
      FileModifier newFileModifier) {
    this.fileName = newFileName;
    this.actionWord = newActionWord;
    this.fileModifier = newFileModifier;
  }

  /**
   * Updates information in text files.
   *
   * @param o the observable object.
   * @param arg the argument to be sent to the text files.
   */
  public void update(Observable o, Object arg) {
    String argument = (String) arg;
    if (argument.split(" , ")[1].equals(actionWord)) {
      fileModifier.modifyFile(fileName, argument);
    }
  }
}
