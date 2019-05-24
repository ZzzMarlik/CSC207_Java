package strategy;

/**
 * The interface fileModifier that has the following methods.
 *
 * @author Make Zhang
 */
public interface FileModifier {

  /**
   * The modifyFile method.
   *
   * @param fileName given file name
   * @param line given line
   */
  void modifyFile(String fileName, String line);

}
