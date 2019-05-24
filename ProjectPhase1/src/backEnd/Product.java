package backEnd;

import myObserver.CheckActionObserver;
import java.io.Serializable;
import java.util.Observable;
import strategy.FileLineAppender;

/**
 * Product in the store.
 * Each product has name, upc, cost, price, discount, quantity, threshold,
 * which StoreSection and Aisle it belongs to, and startDate and endDate of Sale.
 * All Products can generate an order automatically, if their quantity is under threshold.
 * Each time an automatic order or manual order is sent, the orderHistory file of this
 * Product will record.
 *
 * @author Ziyue Xu
 */
public class Product extends Observable implements Serializable {

  /**
   * The name of this Product.
   */
  private String name;

  /**
   * The upc of this Product.
   */
  private String upc;

  /**
   * The cost of this Product.
   */
  private double cost;

  /**
   * The price of this Product.
   */
  private double price;

  /**
   * The discount of this Product.
   */
  private double discount;

  /**
   * The StoreSection this product belongs to.
   */
  private StoreSection superSection;

  /**
   * The quantity of this Product in store.
   */
  private int quantity;

  /**
   * The threshold of this Product, under which means order is necessary.
   */
  private int threshold;

  /**
   * The Aisle this Product belongs to.
   */
  private Aisle superAisle;

  /**
   * The start date of sale, if this product has sale date.
   */
  private String startDate;

  /**
   * The end date of sale, if this product has sale date.
   */
  private String endDate;

  /**
   * OrderManager, used to manage orders generated when quantity is under threshold.
   */
  private OrderManager orderManager;

  /**
   * The multiple when generating auto order.
   */
  private static final int AUTO_ORDER_MULTIPLE = 3;

  /**
   * The distributor who provide this Product.
   */
  private String distributor;

  /**
   * Create a new Product with quantity, upc, name, cost and orderManager.
   *
   * @param quantity the quantity of this Product when it initialized.
   * @param upc the upc of this Product when it initialized.
   * @param name the name of this Product when it initialized.
   * @param cost the cost of this Product when it initialized.
   * @param orderManager the orderManger to store all orders.
   */
  public Product(int quantity, String upc, String name, double cost, OrderManager orderManager) {
    this.name = name;
    this.upc = upc;
    this.cost = cost;
    this.quantity = quantity;
    // When a Product is created, its discount is set to be 1.0.
    this.discount = 1.0;
    this.orderManager = orderManager;
    addObserver(new CheckActionObserver("ProjectPhase1/src/recordForChange/" + name +
        "OrderHistory.txt", "order", new FileLineAppender()));
    addObserver(new CheckActionObserver("ProjectPhase1/src/recordForChange/" + name +
        "PriceHistory.txt", "change-price", new FileLineAppender()));
  }

  // Getter method for field name.

  /**
   * Return the field name of this Product.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  // Getter method of field upc.

  /**
   * Return the upc of this Product.
   *
   * @return upc
   */
  public String getUpc() {
    return upc;
  }

  // Getter method for field cost.

  /**
   * Return the cost of this Product.
   *
   * @return cost
   */
  public double getCost() {
    return cost;
  }

  // Getter method for field price.

  /**
   * Return the price of this Product.
   *
   * @return price
   */
  public double getPrice() {
    return price;
  }

  // Setter method for field price.

  /**
   * Set field price of this Product to be newPrice.
   *
   * @param newPrice new price to set
   */
  public void setPrice(double newPrice) {
    this.price = newPrice;
  }

  // Getter method for field distributor.

  /**
   * Return the distributor of this Product.
   *
   * @return distributor
   */
  public String getDistributor() {
    return distributor;
  }

  // Setter method for field distributor.

  /**
   * Set distributor to this Product.
   *
   * @param distributor distributor to set
   */
  public void setDistributor(String distributor) {
    this.distributor = distributor;
  }

  // Getter method for field quantity.

  /**
   * Return the quantity of this Product.
   *
   * @return quantity
   */
  public int getQuantity() {
    return quantity;
  }

  // Setter method for field quantity.

  /**
   * Set field quantity of this Product to be quantity.
   * If the new quantity id under threshold, it will call autoOrder automatically.
   *
   * @param quantity new quantity to set.
   */
  void setQuantity(int quantity) {
    this.quantity = quantity;
    if (needAutoOrder()) {
      autoOrder();
    }
  }

  // Getter method for field discount.

  /**
   * Return the discount of this Product.
   *
   * @return discount
   */
  public double getDiscount() {
    return discount;
  }

  // Getter method for field threshold.

  /**
   * Return the threshold of this Product.
   *
   * @return threshold
   */
  public int getThreshold() {
    return threshold;
  }

  /**
   * Set new threshold.
   * if the current quantity is under the new threshold,
   * it will automatically call autoOrder,
   *
   * @param threshold threshold
   */
  public void setThreshold(int threshold) {
    this.threshold = threshold;
    if (needAutoOrder()) {
      autoOrder();
    }
  }

  // Setter method for field superAisle.

  /**
   * Set the superAisle to this Product.
   *
   * @param superAisle new Aisle
   */
  public void setSuperAisle(Aisle superAisle) {
    this.superAisle = superAisle;
  }

  // Getter method for field superAisle.

  /**
   * Return the field Aisle of this Product.
   *
   * @return superAisle
   */
  Aisle getSuperAisle() {
    return superAisle;
  }

  // Getter method for field superSection.

  /**
   * Return the superSection of this Product.
   *
   * @return super Section
   */
  public StoreSection getSuperSection() {
    return superSection;
  }

  /**
   * Set field superSection of this Product to be superSection.
   *
   * @param superSection new StoreSection to set
   */
  public void setSuperSection(StoreSection superSection) {
    this.superSection = superSection;
  }

  /**
   * Set discount to this Product and notify this with the name of user.
   *
   * @param discount discount to be set, a double.
   * @param user the name of user who did this.
   */
  public void setDiscount(double discount, String user) {
    this.discount = discount;
    setChanged();
    notifyObservers(String.format("%s , change-price , without-sale price : %s , "
        + "sale price : %s", user, price, getOnSalePrice()));
  }

  // Getter method for startDate.

  /**
   * Return the startDate of this Product.
   *
   * @return the start date of sale.
   */
  public String getStartDate() {
    return startDate;
  }

  // Getter method for endDate.

  /**
   * Return the endDate of this Product.
   *
   * @return the end date of sale.
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * Set startDate and endDate two fields.
   *
   * @param startDate the start date of sale.
   * @param endDate the end date of sale.
   */
  public void setSaleDate(String startDate, String endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Return the sale price of this Product.
   *
   * @return discount multiples price
   */
  public double getOnSalePrice() {
    return discount * price;
  }

  // Overload

  /**
   * User set the field price to be newPrice.
   *
   * @param newPrice newPrice to set
   * @param user the name of user do this action
   */
  public void setPrice(double newPrice, String user) {
    price = newPrice;
    setChanged();
    notifyObservers(String.format("%s , change-price , without-sale price : %s , "
        + "sale price : %s", user, price, getOnSalePrice()));
  }

  /**
   * Change the location of this Product to superAisle.
   *
   * @param superAisle new Aisle
   */
  public void changeLocation(Aisle superAisle) {
    Aisle oldAisle = this.superAisle;
    this.superAisle = superAisle;
    oldAisle.removeProduct(this);
    superAisle.addProduct(this);
  }

  /**
   * Return true if and only if todayDate is within the saleDate.
   *
   * @param todayDate today's date
   * @return boolean
   */
  public boolean whetherOnSale(String todayDate) {
    if (startDate == null) {
      return false;
    } else {
      String[] startDateList = startDate.split("-");
      String[] endDateList = endDate.split("-");
      String[] todayDateList = todayDate.split("-");

      for (int i = 0; i < todayDateList.length; i++) {
        int startDateNum = Integer.parseInt(startDateList[i]);
        int todayDateNum = Integer.parseInt(todayDateList[i]);
        int endDateNum = Integer.parseInt(endDateList[i]);
        if (!((startDateNum <= todayDateNum) && (todayDateNum <= endDateNum))) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Send autoOrder notification to observer and record it in orderManager.
   */
  private void autoOrder() {
    String instruction = String.format("automation , order , %s , %s , %s , Distributor: %s",
        threshold * AUTO_ORDER_MULTIPLE, upc, name, distributor);
    setChanged();
    notifyObservers(instruction);
    orderManager.addOrder(instruction);
  }

  /**
   * Create a PriceHistory File for this Product.
   */
  public void createPriceHisFile() {
    FileLineAppender appender = new FileLineAppender();
    appender.modifyFile("ProjectPhase1/src/recordForChange/" + name +
        "PriceHistory.txt", name + " initial price: " + price);
  }

  /**
   * Return the name of this Product's Aisle.
   *
   * @return name of Aisle
   */
  public String getLocation() {
    return superAisle.getName();
  }

  /**
   * Check whether the total quantity of this Product under threshold.
   * Return true if and only if the total quantity is under threshold.
   * Total quantity includes current quantity plus quantity already been ordered.
   *
   * @return whether need to order automatically
   */
  private boolean needAutoOrder() {
    if (orderManager.hasProduct(this.name)) {
      return (quantity + orderManager.getProductOrderQuantity(
          this.name)) < threshold;
    } else {
      return quantity < threshold;
    }
  }

  /**
   * Notify Observer with instruction.
   *
   * @param instruction instruction to notify
   */
  public void manualOrderNotify(String instruction) {
    setChanged();
    notifyObservers(instruction);
  }
}
