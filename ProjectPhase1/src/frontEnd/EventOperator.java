package frontEnd;

import backEnd.StoreSection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import backEnd.Aisle;
import backEnd.Product;
import backEnd.Store;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import strategy.FileExistChecker;
import strategy.FileLineDeleter;
import strategy.FileToStringTransformer;

/**
 * The EventOperator class. A event controller which identifies instruction
 * from users, calls corresponding methods and records information in
 * the text file log.txt.
 *
 * @author Haolin Zhang, Ziyue Xu
 */
public class EventOperator extends Observable {

  /**
   * Constant file name of file recording all pending orders.
   */
  private static final String PENDING_ORDER_FILE_NAME = "ProjectPhase1/src/"
      + "recordForChange/PendingOrder.txt";

  /**
   * Strategy to read a whole text file as a String.
   */
  private FileToStringTransformer fileToStringTransformer;

  /**
   * Strategy to check whether a file exits.
   */
  private FileExistChecker fileExistChecker;

  /**
   * Strategy to a line in a text file.
   */
  private FileLineDeleter fileLineDeleter;

  /**
   * Today's store.
   */
  private Store store;

  /**
   * Creates a new EventOperator and load valid user actions from
   * file with allowed actions for each user.
   *
   * @param store today's store.
   */
  EventOperator(Store store) {
    this.store = store;
    fileToStringTransformer = new FileToStringTransformer();
    fileExistChecker = new FileExistChecker();
    fileLineDeleter = new FileLineDeleter();
  }

  /**
   * Returns today's store.
   *
   * @return today's store.
   */
  public Store getStore() {
    return store;
  }

  /**
   * Adds the new allowed action to user.
   *
   * @param action the new allowed action.
   * @param user a staff in the store.
   */
  void addActionToUser(String action, String user) {
    ArrayList<String> actionList = store.getUserToAction().get(user);
    if (!actionList.contains(action)) {
      actionList.add(action);
    }
  }

  /**
   * Deletes the allowed action from user.
   *
   * @param action an allowed action.
   * @param user a staff in the store.
   */
  void deleteActionFromUser(String action, String user) {
    ArrayList<String> actionList = store.getUserToAction().get(user);
    if (actionList.contains(action)) {
      actionList.remove(action);
    }
  }

  /**
   * Tests if the user if a legal staff in the store.
   *
   * @param user a staff in the store.
   * @return true if the user if a legal staff in the store, false otherwise.
   */
  boolean whetherLegalUser(String user) {
    return store.getUserList().contains(user);
  }

  /**
   * Returns the allowed action list of user.
   *
   * @param user a staff in the store.
   * @return the allowed action list of user.
   */
  ArrayList<String> getLegalActions(String user) {
    return store.getUserToAction().get(user);
  }

  /**
   * Returns the list of all allowed actions in store.
   *
   * @return the list of all allowed actions in store.
   */
  ArrayList<String> getAllLegalActions() {
    return store.getActionList();
  }

  /**
   * Returns the list of all legal users in store.
   *
   * @return the list of all legal users in store.
   */
  ArrayList<String> getAllLegalUsers() {
    return store.getUserList();
  }

  /**
   * Tests if product exists in the store.
   *
   * @param productName the name of product.
   * @return true if product exists in the store, false otherwise.
   */
  boolean whetherHasProduct(String productName) {
    return store.hasProduct(productName);
  }

  /**
   * Tests if section exists in the store.
   *
   * @param sectionName the name of section.
   * @return true if section exists in the store, false otherwise.
   */
  boolean whetherHasSection(String sectionName) {
    return store.getSectionMap().containsKey(sectionName);
  }

  /**
   * Tests if upc is a upc of an existed product.
   *
   * @param upc the upc of product.
   * @return true if upc is a upc of an existed product, false otherwise.
   */
  boolean whetherHasUpc(String upc) {
    return store.hasUpc(upc);
  }

  /**
   * Returns the leaf section list.
   *
   * @return the leaf section list.
   */
  ArrayList<String> getLeafSectionNameList() {
    ArrayList<String> nameList = new ArrayList<>();
    for (StoreSection section : store.getSectionMap().values()) {
      if (section.whetherLeafSection()) {
        nameList.add(section.getName());
      }
    }
    return nameList;
  }

  /**
   * Returns the existed aisle list.
   *
   * @return the existed aisle list.
   */
  ArrayList<String> getAisleNameList() {
    ArrayList<String> nameList = new ArrayList<>();
    for (Aisle aisle : store.getAisleMap().values()) {
      nameList.add(aisle.getName());
    }
    return nameList;
  }

  /**
   * Returns today's date.
   *
   * @return today's date.
   */
  String getTodayDate() {
    return store.getTodayDate();
  }

  /**
   * Returns upc of product with productName.
   *
   * @param productName the name of product.
   * @return upc of product with productName.
   */
  String getUpc(String productName) {
    return store.getProduct(productName).getUpc();
  }

  /**
   * Returns the root section in the store.
   *
   * @return the root section in the store.
   */
  StoreSection getRootSection() {
    return store.getSection("Inventory");
  }

  /**
   * Scan-in Products.
   * Returns action result information.
   * If the Product is not new, it will increase quantity.
   * Both will check the order history to see whether an order is received.
   *
   * @param upc the upc of product.
   * @param quantity the quantity of product.
   * @return action result information.
   */
  String scanInOld(String upc, int quantity) {
    String result = "";
    String productName = store.getProductByUpc(upc).getName();
    if (receiveCheck(productName, upc, quantity, PENDING_ORDER_FILE_NAME)) {
      result += String.format("order of %s with quantity %s has arrived",
          upc, quantity) + "\n";
    }
    // Inventory already has this product.
    Product product = store.getProductByUpc(upc);
    int oldQuantity = product.getQuantity();
    store.increaseProductQuantity(upc, quantity);
    int newQuantity = product.getQuantity();
    result += String.format("%s quantity is changed from %s to %s",
        upc, oldQuantity, newQuantity) + "\n";
    return result;
  }

  /**
   * Scan-in Products.
   * Returns action result information. If the Product is new to the store,
   * it will create this new Product and set a series of attributes.
   * Both will check the order history to see whether an order is received.
   *
   * @param quantity the quantity of new product.
   * @param upc the upc of new product.
   * @param name the name of new product.
   * @param cost the cost of new product.
   * @param price the price of new product.
   * @param superSectionName the name of parent section of new product.
   * @param threshold the threshold amount of new product.
   * @param aisleName the name of aisle the new product will be at.
   * @param distributor the name of distributor of new product.
   * @return action result information.
   */
  String scanInNew(int quantity, String upc, String name,
      double cost, double price, String superSectionName, int threshold,
      String aisleName, String distributor) {
    String result = "";
    if (receiveCheck(name, upc, quantity, PENDING_ORDER_FILE_NAME)) {
      result += String.format("order of %s with quantity %s has arrived",
          upc, quantity) + "\n";
    }
    // This kind of product is new.
    // Get information first.
    StoreSection leafSection = store.getSection(superSectionName);
    Aisle aisle = store.getAisle(aisleName);

    // Create new product and set necessary fields.
    Product newProduct = new Product(quantity, upc, name,
        cost, store.getOrderManager());
    newProduct.setPrice(price);
    newProduct.setSuperSection(leafSection);
    newProduct.setThreshold(threshold);
    newProduct.setSuperAisle(aisle);
    newProduct.setDistributor(distributor);
    store.addProduct(newProduct);
    // Create price history.
    newProduct.createPriceHisFile();
    result += String.format("new product %s with initial quantity %s "
        + "is created", newProduct.getName(), newProduct.getQuantity()) + "\n";
    return result;
  }

  /**
   * Scan-out quantity number Product with upc.
   * Returns action result information. It will check whether the
   * Product scanned-out is on sale and use the sale price if it is.
   * Add revenue and profit to daily revenue and profit.
   * This might stimulate auto order.
   *
   * @param quantity the quantity of this Product to scan-out
   * @param upc the upc of this Product to scan-out
   * @return action result information.
   */
  String scanOut(int quantity, String upc) {
    if (!store.hasUpc(upc)) {
      return "no such product in store";
    } else {
      Product product = store.getProductByUpc(upc);
      String name = product.getName();
      int oldQuantity = product.getQuantity();
      if (quantity > oldQuantity) {
        return "The inventory amount of this product is insufficient\n" +
            String.format("%s %s has no change on quantity", upc, name);
      } else {
        String result = "";
        store.decreaseProductQuantity(upc, quantity);
        int newQuantity = product.getQuantity();
        result += String.format("%s %s quantity is changed from %s to %s",
            upc, name, oldQuantity, newQuantity) + "\n";
        // Daily revenue and profit will increase correspondingly.
        double todayPrice;
        if (product.whetherOnSale(store.getTodayDate())) {
          todayPrice = product.getOnSalePrice();
          result += String.format("%s is on sale today with discount %s",
              product.getName(), product.getDiscount()) + "\n";
        } else {
          result += String.format("no discount for %s today",
              product.getName()) + "\n";
          todayPrice = product.getPrice();
        }
        double revenue = todayPrice * quantity;
        double profit = (todayPrice - product.getCost()) * quantity;
        store.addRevenue(revenue);
        store.addProfit(profit);
        result += String.format("Today's revenue add %s and profit add %s",
            revenue, profit) + "\n";
        return result;
      }
    }
  }

  /**
   * Returns action result information about the total inventory quantity of
   * the product with productName.
   *
   * @param productName the name of the product.
   * @return Returns action result information.
   */
  String viewQuantity(String productName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    int result = store.getProduct(productName).getQuantity();
    return String.format("%s's current quantity is %s", productName, result);
  }

  /**
   * Returns action result information about the aisle location of
   * the product with productName.
   *
   * @param productName the name of the product.
   * @return action result information.
   */
  String viewLocation(String productName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    return store.getProduct(productName).getLocation();
  }

  /**
   * Returns action result information about the cost of this Product.
   *
   * @param productName name of this Product.
   * @return action result information.
   */
  String viewCost(String productName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    double result = store.getProduct(productName).getCost();
    return productName + "'s cost is " + result;
  }

  /**
   * Returns action result information about the price history
   * of this Product.
   *
   * @param productName name of this Product.
   * @return action result information.
   */
  String viewPriceHistory(String productName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    String priceHisFileName = "ProjectPhase1/src/recordForChange/" +
        productName + "PriceHistory.txt";
    String result = fileToStringTransformer.fileToString(priceHisFileName);
    return "This is " + productName + "'s price history:\n" + result;
  }

  /**
   * Returns action result information about the current price of this Product.
   * If it is on sale, use sale price, otherwise, use regular price.
   *
   * @param productName the name of this Product.
   * @return action result information.
   */
  String viewCurrentPrice(String productName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    Product product = store.getProduct(productName);
    double price;
    if (product.whetherOnSale(store.getTodayDate())) {
      price = product.getOnSalePrice();
    } else {
      price = product.getPrice();
    }
    return productName + "'s price is " + price;
  }

  /**
   * Returns action result information about the discount sale date of
   * the product with productName.
   *
   * @param productName the name of the product.
   * @return action result information.
   */
  String viewSaleDate(String productName) {
    if (!store.hasProduct(productName)) {
      return "no such product in store";
    }
    String result;
    Product product = store.getProduct(productName);
    if (product.getStartDate() == null && product.getEndDate() == null) {
      result = "This product is not on sale";
    } else {
      result = String.format("The sale date of %s is %s to %s", productName,
          product.getStartDate(), product.getEndDate());
    }
    return result;
  }

  /**
   * Returns action result information about the manual order from the user.
   *
   * @param quantity the quantity to order.
   * @param name the name of the product.
   * @param user the user who commits this action.
   * @param distributor the distributor of product.
   * @return action result information.
   */
  public String order(int quantity, String name, String upc, String user,
      String distributor) {

    //case of new product
    if (!store.hasProduct(name)) {
      String newInstruction = user + " , order , " + quantity + " , " +
          upc + " , " + name + " , " +
          "Distributor: " + distributor;
      store.getOrderManager().addOrder(newInstruction);
      File newOrderHis = new File("ProjectPhase1/src/recordForChange/" +
          name + "OrderHistory.txt");
      try {
        newOrderHis.createNewFile();
        BufferedWriter br = new BufferedWriter(new FileWriter(newOrderHis));
        br.write(newInstruction);
        br.write("\n");
        br.close();
      } catch (IOException exception) {
        System.err.println("An IOException is caught");
        exception.printStackTrace();
      }
      return "this new product's order is successful";
    }

    //case of existed product
    String result;
    if (manualOrderCheck(quantity, name)) {
      Product product = store.getProduct(name);
      String newInstruction = user + " , order , " + quantity + " , " +
          upc + " , " + name + " , " +
          "Distributor: " + distributor;
      store.getOrderManager().addOrder(newInstruction);
      product.manualOrderNotify(newInstruction);
      result = "manual order is successful";
    } else {
      result = "sorry, manual order amount is not enough, manual order fails";
    }
    return result;
  }

  /**
   * Returns action result information. Changes the aisle location of the product
   * with productName to the aisle with newAisleName, then notify the observers.
   *
   * @param productName the name of the product.
   * @param newAisleName the name of the destination aisle.
   * @return action result information.
   */
  String changeLocation(String productName, String newAisleName) {
    if ((!store.hasProduct(productName))) {
      return "no such product in store";
    }
    String oldLocation = store.getProduct(productName).getLocation();
    store.getProduct(productName).changeLocation(store.getAisle(newAisleName));
    String newLocation = store.getProduct(productName).getLocation();
    return productName + "'s location is changed from" +
        oldLocation + " to " + newLocation;
  }

  /**
   * Returns action result information about the order history of the product
   * with productName.
   *
   * @param productName the name of the product.
   * @return action result information.
   */
  String viewOrderHistory(String productName) {
    if (!store.hasProduct(productName)) {
      return "no such product in store";
    }
    String orderHisFileName = "ProjectPhase1/src/recordForChange/" +
        productName + "OrderHistory.txt";
    String result;
    if (fileExistChecker.fileExistCheck(orderHisFileName)) {
      result = "This is " + productName + "'s order history:\n" +
          fileToStringTransformer.fileToString(orderHisFileName);
    } else {
      result = "No order for " + productName + " available";
    }
    return result;
  }

  /**
   * Returns action result information about the information in TotalOrderHistory.txt.
   *
   * @return action result information.
   */
  String viewTotalOrderHistory() {
    String totalOrderHisFileName = "ProjectPhase1/src/recordForChange/" +
        "TotalOrderHistory.txt";
    String result;
    if (fileExistChecker.fileExistCheck(totalOrderHisFileName)) {
      result = "This is total order history:\n" +
          fileToStringTransformer.fileToString(totalOrderHisFileName);
    } else {
      result = "No order in this store available";
    }
    return result;
  }

  /**
   * Returns action result information about pending orders.
   *
   * @return action result information.
   */
  String viewPendingOrder() {
    String pendingOrderFileName = "ProjectPhase1/src/recordForChange/PendingOrder.txt";
    String result;
    if (fileExistChecker.fileExistCheck(pendingOrderFileName)) {
      result = "This is pending order list:\n" +
          fileToStringTransformer.fileToString(pendingOrderFileName);
    } else {
      result = "No pending order available";
    }
    return result;
  }

  /**
   * Returns action result information about today store revenue.
   *
   * @return action result information.
   */
  String viewRevenue() {
    double result = store.getTotalRevenue();
    return "The total revenue of store is " + result;
  }

  /**
   * Returns action result information about today store profit.
   *
   * @return action result information.
   */
  String viewProfit() {
    double result = store.getTotalProfit();
    return "The total profit of store is " + result;
  }

  /**
   * Returns action result information about product's distributor.
   *
   * @param productName the name of product.
   * @return action result information.
   */
  String viewDistributor(String productName) {
    if (!store.hasProduct(productName)) {
      return "no such product in store";
    }
    return store.getProduct(productName).getDistributor();
  }

  /**
   * Returns action result information. Changes the original price of
   * the product with productName to newPrice, then notify the observers.
   *
   * @param productName the name of the product.
   * @param newPrice the new original price of the product.
   * @param user the identity of the user.
   * @return action result information.
   */
  String changePrice(String productName, double newPrice, String user) {
    if (!store.hasProduct(productName)) {
      return "no such product in store";
    }
    Product product = store.getProduct(productName);
    double oldOriginalPrice = product.getPrice();
    product.setPrice(newPrice, user);
    double newOriginalPrice = product.getPrice();
    return String.format("%s's original price is changed from "
        + "%s to %s", productName, oldOriginalPrice, newOriginalPrice);
  }

  /**
   * Returns action result information. Set discount and sale dates for
   * one Product in the store by user.
   *
   * @param productName name of the Product to set discount.
   * @param newDiscount new discount to set.
   * @param startDate set the start date of sale.
   * @param endDate set the end date of sale.
   * @param user the name of user who do this action.
   * @return action result information.
   */
  String setDiscount(String productName, double newDiscount,
      String startDate, String endDate, String user) {
    if (!store.hasProduct(productName)) {
      return "no such product in store";
    }
    Product product = store.getProduct(productName);
    double oldDis = product.getDiscount();
    product.setDiscount(newDiscount, user);
    product.setSaleDate(startDate, endDate);
    double newDis = product.getDiscount();
    return String.format("%s's discount is changed from "
            + "%s to %s (from %s to %s)", productName,
        oldDis, newDis, startDate, endDate);
  }

  /**
   * Tests if the manual-ordered quantity of product with name name
   * is enough or not.
   *
   * @param quantityToOrder the manual-ordered quantity of the product.
   * @param name the name of the product.
   * @return true if the manual-ordered quantity of product with name name is enough, false
   * otherwise.
   */
  private boolean manualOrderCheck(int quantityToOrder, String name) {
    Product product = store.getProduct(name);
    if (store.getOrderManager().hasProduct(name)) {
      return (product.getQuantity() + quantityToOrder +
          store.getOrderManager().getProductOrderQuantity(name))
          >= product.getThreshold();
    } else {
      return (product.getQuantity() + quantityToOrder)
          >= product.getThreshold();
    }
  }

  /**
   * Check whether the pending order is received.
   * If a pending is received, delete this line of pending order in file with fileName.
   *
   * @param productName the name of product.
   * @param upc the upc of product.
   * @param quantity the quantity of product.
   * @param fileName the file which needs to be updated.
   */
  private boolean receiveCheck(String productName, String upc,
      int quantity, String fileName) {
    try {
      File file = new File(fileName);
      if (file.exists()) {
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        String lineToDelete = null;
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          String[] orderInfo = line.split(" , ");
          if (Integer.parseInt(orderInfo[2]) == quantity &&
              orderInfo[3].equals(upc)) {
            lineToDelete = line;
            break;
          }
        }
        scanner.close();
        if (lineToDelete != null) {
          fileLineDeleter.modifyFile(fileName, lineToDelete);
          store.getOrderManager().decreaseOrderAmount(productName, quantity);
          return true;
        }
        return false;
      }
      return false;
    } catch (IOException e) {
      System.err.println("A IOException is caught");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Inserts a new section into the section tree. It is a new internal
   * node in the section tree which has a parent node and also
   * contains some child nodes in the section list.
   *
   * @param sectionName the name of the new section.
   * @param parentSectionName the name of parent section of new section.
   * @param childrenNameList the names of child sections of new section.
   */
  void insertSectionOnSection(String sectionName, String parentSectionName,
      String childrenNameList) {
    StoreSection newSection = new StoreSection(sectionName);
    StoreSection parentSection = store.getSection(parentSectionName);
    ArrayList<Object> children = new ArrayList<>();
    String[] lineList = childrenNameList.split(" , ");
    for (String aLineList : lineList) {
      children.add(store.getSection(aLineList));
    }
    insertSection(newSection, parentSection, children);
  }

  /**
   * Inserts a new section into the section tree. It is a new leaf
   * node in the section tree which has a parent node and also
   * contains some products in the product list.
   *
   * @param sectionName the name of the new section.
   * @param parentSectionName the name of parent section of new section.
   * @param childrenNameList the names of products in new section.
   */
  void insertSectionOnProduct(String sectionName, String parentSectionName,
      String childrenNameList) {
    StoreSection newSection = new StoreSection(sectionName);
    StoreSection parentSection = store.getSection(parentSectionName);
    ArrayList<Object> children = new ArrayList<>();
    String[] lineList = childrenNameList.split(" , ");
    for (String aLineList : lineList) {
      children.add(store.getProduct(aLineList));
    }
    insertSection(newSection, parentSection, children);
  }

  /**
   * Inserts a new section into the section tree which has a parent node
   * parentSection and a list of child nodes children.
   *
   * @param section a new section to be inserted.
   * @param parentSection the parent node of the new section.
   * @param children the child nodes of the new section.
   */
  private void insertSection(StoreSection section,
      StoreSection parentSection, ArrayList<Object> children) {
    for (Object child : children) {
      if (child instanceof StoreSection) {
        StoreSection subSection = (StoreSection) child;
        subSection.getSuperSection().removeSection(subSection);
        section.addSection(subSection);
      } else if (child instanceof Product) {
        Product product = (Product) child;
        section.addProduct(product);
        product.getSuperSection().removeProduct(product);
        product.setSuperSection(section);
      }
    }
    store.addSection(section, parentSection);
  }

  /**
   * Return a String representation of the singleton object store.
   *
   * @return String representation of store
   */
  String viewStore() {
    return store.toString();
  }
}

