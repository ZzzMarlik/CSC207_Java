package backEnd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * The Store class.  A store class plays a role as a container of
 * inventory back-end model and offers information of all products,
 * locations and category hierarchy.
 *
 * @author Haolin Zhang
 */
public final class Store implements Serializable {

  /**
   * The only Singleton object.
   */
  private static Store instance = new Store();

  /**
   * Product information map.
   */
  private Map<String, Product> productMap;

  /**
   * Aisle information map.
   */
  private Map<String, Aisle> aisleMap;

  /**
   * Section information map.
   */
  private Map<String, StoreSection> sectionMap;

  /**
   * Upc information map.
   */
  private Map<String, Product> upcMap;

  /**
   * Total revenue of this store.
   */
  private double totalRevenue;

  /**
   * Total profit of this store.
   */
  private double totalProfit;

  /**
   * The calendar recording today's date and time.
   */
  private Calendar calendar = Calendar.getInstance();

  /**
   * The orderManager to receive information of orders.
   */
  private OrderManager orderManager;

  /**
   * Map records every user's allowed actions.
   */
  private Map<String, ArrayList<String>> userToAction;

  /**
   * List of legal users;
   */
  private ArrayList<String> userList;

  /**
   * List of legal actions;
   */
  private ArrayList<String> actionList;

  /**
   * Default user action file.
   */
  private static final String USER_ACTION_FILE_NAME = "ProjectPhase1/DefaultUserAction.txt";

  /**
   * Allocates a new Store.
   */
  private Store() {
    productMap = new HashMap<>();
    aisleMap = new HashMap<>();
    sectionMap = new HashMap<>();
    sectionMap.put("Inventory", new StoreSection("Inventory"));
    upcMap = new HashMap<>();
    orderManager = new OrderManager();
    userToAction = new HashMap<>();
    userList = new ArrayList<>();
    actionList = new ArrayList<>();

    try {
      Scanner scanner = new Scanner(new FileInputStream(USER_ACTION_FILE_NAME));
      while (scanner.hasNextLine()) {
        String[] lineList = scanner.nextLine().split(" ");
        String user = lineList[0];
        if (!userList.contains(user)) {
          userList.add(user);
        }
        userToAction.put(user, new ArrayList<>());
        for (int i = 1; i < lineList.length; i++) {
          if (!userToAction.get(user).contains(lineList[i])) {
            userToAction.get(user).add(lineList[i]);
          }
          if (!actionList.contains(lineList[i])) {
            actionList.add(lineList[i]);
          }
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("A FileNotFoundException is caught");
      e.printStackTrace();
    }
  }

  /**
   * Returns the aisle map.
   *
   * @return the aisle map.
   */
  public Map<String, Aisle> getAisleMap() {
    return aisleMap;
  }

  /**
   * Returns the section map.
   *
   * @return the section map.
   */
  public Map<String, StoreSection> getSectionMap() {
    return sectionMap;
  }

  /**
   * Returns the user allowed action map.
   *
   * @return the user allowed action map.
   */
  public Map<String, ArrayList<String>> getUserToAction() {
    return userToAction;
  }

  /**
   * Returns the list of legal users.
   *
   * @return the list of legal users.
   */
  public ArrayList<String> getUserList() {
    return userList;
  }

  /**
   * Returns the list of legal actions.
   *
   * @return the list of legal actions.
   */
  public ArrayList<String> getActionList() {
    return actionList;
  }

  /**
   * Returns the only Singleton store object.
   *
   * @return the only Singleton store object.
   */
  static Store getInstance() {
    return instance;
  }

  /**
   * Returns the total revenue of this store.
   *
   * @return the total revenue of this store.
   */
  public double getTotalRevenue() {
    return totalRevenue;
  }

  /**
   * Returns the total profit of this store.
   *
   * @return the total profit of this store.
   */
  public double getTotalProfit() {
    return totalProfit;
  }

  /**
   * Returns the date of today.
   *
   * @return the date of today.
   */
  public String getTodayDate() {
    return calendarDateTransform();
  }

  /**
   * Returns the orderManager of this store.
   *
   * @return the orderManager of this store.
   */
  public OrderManager getOrderManager() {
    return orderManager;
  }

  /**
   * Returns String representation of calendar in format "year-month-day".
   *
   * @return String representation of calendar in format "year-month-day".
   */
  private String calendarDateTransform() {
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DATE);
    return year + "-" + month + "-" + day;
  }

  /**
   * Increase calendar date by one day.
   */
  public void increaseDateByOne() {
    calendar.add(Calendar.DATE, 1);
  }

  /**
   * Adds a new product to the store and record down all
   * relating information.
   *
   * @param newProduct a new product.
   */
  public void addProduct(Product newProduct) {
    String newProductName = newProduct.getName();
    if (!productMap.containsKey(newProductName)) {
      productMap.put(newProductName, newProduct);
      upcMap.put(newProduct.getUpc(), newProduct);
      newProduct.getSuperAisle().addProduct(newProduct);
      newProduct.getSuperSection().addProduct(newProduct);
    }
  }

  /**
   * Adds a new aisle to the store and record down all
   * relating information.
   *
   * @param newAisle a new aisle.
   */
  void addAisle(Aisle newAisle) {
    String newAisleName = newAisle.getName();
    if (!aisleMap.containsKey(newAisleName)) {
      aisleMap.put(newAisleName, newAisle);
    }
  }

  /**
   * Adds a new section to the store and record down all
   * relating information.
   *
   * @param newSection a new section.
   * @param parentSection the parent section of the new section.
   */
  public void addSection(StoreSection newSection, StoreSection parentSection) {
    String newSectionName = newSection.getName();
    if (!sectionMap.containsKey(newSectionName)) {
      sectionMap.put(newSectionName, newSection);
      parentSection.addSection(newSection);
      newSection.setSuperSection(parentSection);
    }
  }

  /**
   * Tests if this store contains the product with productName.
   *
   * @param productName name of the product.
   * @return true if this store contains the product with productName, false otherwise.
   */
  public boolean hasProduct(String productName) {
    return productMap.containsKey(productName);
  }

  /**
   * Tests if this store contains the product with upc.
   *
   * @param upc upc of the product.
   * @return true if this store contains the product with upc, false otherwise.
   */
  public boolean hasUpc(String upc) {
    return upcMap.containsKey(upc);
  }

  /**
   * Returns the section with sectionName.
   *
   * @param sectionName the name of the section.
   * @return the section with sectionName.
   */
  public StoreSection getSection(String sectionName) {
    return sectionMap.get(sectionName);
  }

  /**
   * Returns the product with productName.
   *
   * @param productName the name of the product.
   * @return the product with productName.
   */
  public Product getProduct(String productName) {
    return productMap.get(productName);
  }

  /**
   * Returns the aisle with aisleName.
   *
   * @param aisleName the name of the aisle.
   * @return the aisle with aisleName.
   */
  public Aisle getAisle(String aisleName) {
    return aisleMap.get(aisleName);
  }

  /**
   * Returns the product with upc.
   *
   * @param upc the upc of the product.
   * @return the product with upc.
   */
  public Product getProductByUpc(String upc) {
    return upcMap.get(upc);
  }

  /**
   * Increases the quantity of the product with upc by increment.
   *
   * @param upc the upc of the product.
   * @param increment the amount to increase.
   */
  public void increaseProductQuantity(String upc, int increment) {
    Product product = upcMap.get(upc);
    product.setQuantity(product.getQuantity() + increment);
  }

  /**
   * Decreases the quantity of the product with upc by decrement.
   *
   * @param upc the upc of the product.
   * @param decrement the amount to decrease.
   */
  public void decreaseProductQuantity(String upc, int decrement) {
    Product product = upcMap.get(upc);
    product.setQuantity(product.getQuantity() - decrement);
  }

  /**
   * Increases the total revenue by revenueIncrement.
   *
   * @param revenueIncrement the amount to increase.
   */
  public void addRevenue(double revenueIncrement) {
    totalRevenue += revenueIncrement;
  }

  /**
   * Increases the total profit by profitIncrement.
   *
   * @param profitIncrement the amount to increase.
   */
  public void addProfit(double profitIncrement) {
    totalProfit += profitIncrement;
  }

  /**
   * Sets both total profit and total revenue to 0.
   */
  public void clearRevenueAndProfit() {
    totalProfit = 0;
    totalRevenue = 0;
  }

  /**
   * Print out the whole inventory structure and details.
   */
  public String toString() {
    String result = "";
    result += "****************** store **************************\n";
    for (Entry<String, Aisle> entry : aisleMap.entrySet()) {
      result += entry.getValue() + "\n" +
          "********************************************" + "\n";
    }
    for (Entry<String, StoreSection> entry : sectionMap.entrySet()) {
      result += entry.getValue() + "\n" +
          "********************************************" + "\n";
    }
    return result;
  }
}
