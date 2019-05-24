package frontEnd;

import java.io.FileNotFoundException;
import backEnd.Store;
import backEnd.StoreBuilder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * The StoreSimulation class.
 * It is used to simulate a store.
 */
public class StoreSimulation {

  /**
   * File name for serialization .ser file.
   */
  private static final String SER_FILE_NAME = "ProjectPhase1/src/recordForChange/store.ser";

  /**
   * Starts the whole program.
   */
  public static void main(String[] args) throws FileNotFoundException {
    try {
      // resume store
      Store store = storeResume();
      EventOperator operator = new EventOperator(store);
      new LoginFrame(operator);
    } catch (ClassNotFoundException e) {
      System.err.println("A ClassNotFoundException was caught");
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("A IOException was caught");
      e.printStackTrace();
    }
  }

  /**
   * Deserialize .ser file and get the only one store object.
   *
   * @return the singleton store object.
   * @throws IOException If there is any IOException.
   * @throws ClassNotFoundException If the class is not found.
   */
  private static Store storeResume() throws IOException, ClassNotFoundException {
    Store store;
    File file = new File(SER_FILE_NAME);
    if (file.exists()) {
      InputStream inputFile = new FileInputStream(SER_FILE_NAME);
      InputStream buffer = new BufferedInputStream(inputFile);
      ObjectInput input = new ObjectInputStream(buffer);
      store = (Store) input.readObject();
      input.close();
      store.increaseDateByOne();
      store.clearRevenueAndProfit();
    } else {
      // Otherwise, build the store from scratch.
      store = StoreBuilder.buildStore();
    }
    return store;
  }
}
