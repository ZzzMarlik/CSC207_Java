package backEnd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The StoreBuilder class.
 * This is used to build a store by the start.txt file
 * It will build the store with initial Sections, Aisles and Products in start file.
 *
 * @author Make Zhang
 */
public class StoreBuilder {

  /**
   * The name of the file to initialize a store with Products,
   * Aisles and StoreSections etc.
   */
  private static final String START_FILE_NAME = "ProjectPhase1/start.txt";

  /**
   * The marker to distinguish Sections and Aisles in start file.
   */
  private static final String AISLE_MARKER = "***** Aisle *****";

  /**
   * The marker to distinguish Aisles and Products in start file.
   */
  private static final String PRODUCT_MARKER = "***** Product *****";

  /**
   * Return the only one Store object store, which is built by the start.txt file.
   *
   * @return Return the only store object in Store.
   * @throws FileNotFoundException Start file might not exist.
   */
  public static Store buildStore() throws FileNotFoundException {
    Scanner scanner = new Scanner(new FileInputStream(START_FILE_NAME));
    Store store = Store.getInstance();
    buildSection(store, scanner);
    buildAisle(store, scanner);
    buildProduct(store, scanner);
    scanner.close();
    return store;
  }

  // Helper method for buildStore().

  /**
   * Build the store Sections according to start file.
   *
   * @param store the store to be built
   * @param scanner scanner to read file
   */
  private static void buildSection(Store store, Scanner scanner) {
    String line = scanner.nextLine();
    while (!line.equals(AISLE_MARKER)) {
      String[] currentSectionList = line.split(" , ");
      String superSectionName = currentSectionList[0];
      StoreSection superSection = store.getSection(superSectionName);

      for (int i = 1; i < currentSectionList.length; i++) {
        StoreSection currentSection = new StoreSection(currentSectionList[i]);
        store.addSection(currentSection, superSection);
      }
      line = scanner.nextLine();
    }
  }

  // Helper method for buildStore().

  /**
   * Build the store Aisles according to start file.
   *
   * @param store the store to be built
   * @param scanner scanner to read file
   */
  private static void buildAisle(Store store, Scanner scanner) {
    String line = scanner.nextLine();
    while (!line.equals(PRODUCT_MARKER)) {
      store.addAisle(new Aisle(line));
      line = scanner.nextLine();
    }
  }

  // Helper method for buildStore().

  /**
   * Build the store's Products according to start.txt file.
   *
   * @param store the store to be built
   * @param scanner scanner to read file
   */
  private static void buildProduct(Store store, Scanner scanner) {
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] currentProductList = line.split(" , ");
      // Get the information of each Product.
      int quantity = Integer.parseInt(currentProductList[0]);
      String upc = currentProductList[1];
      String name = currentProductList[2];
      double cost = Double.parseDouble(currentProductList[3]);
      double price = Double.parseDouble(currentProductList[4]);
      StoreSection leafSection = store.getSection(currentProductList[5]);
      int threshold = Integer.parseInt(currentProductList[6]);
      Aisle aisle = store.getAisle(currentProductList[7]);
      String distributor = currentProductList[8];
      // Build Product with above information.
      Product currentProduct = new Product(quantity, upc, name, cost,
          store.getOrderManager());
      currentProduct.setThreshold(threshold);
      currentProduct.setPrice(price);
      currentProduct.setSuperSection(leafSection);
      currentProduct.setSuperAisle(aisle);
      currentProduct.setDistributor(distributor);
      currentProduct.createPriceHisFile();
      store.addProduct(currentProduct);
    }
  }
}
