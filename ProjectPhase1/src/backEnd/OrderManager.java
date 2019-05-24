package backEnd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import strategy.FileLineAppender;

/**
 * The OrderManager class. It receives auto-orders from Product
 * and manual orders from staff user, then it records these
 * information to text files PendingOrder.txt and
 * TotalOrderHistory.txt. At the same time, it also has
 * record about the total amount of all orders of each Product.
 *
 * @author Haolin Zhang
 */
public class OrderManager extends Observable implements Serializable {

  /**
   * Record of each ordered Product's total ordered amount.
   */
  private Map<String, Integer> orderMap;

  /**
   * Constant file address of PendingOrder.txt.
   */
  private static final String PENDING_FILE_NAME = "ProjectPhase1/src/"
      + "recordForChange/PendingOrder.txt";

  /**
   * Constant file address of TotalOrderHistory.txt.
   */
  private static final String ORDER_HIS_FILE_NAME = "ProjectPhase1/src/"
      + "recordForChange/TotalOrderHistory.txt";

  /**
   * Allocates a new OrderManager.
   */
  OrderManager() {
    orderMap = new HashMap<>();
    addObserver(new myObserver.CheckActionObserver(PENDING_FILE_NAME, "order",
        new FileLineAppender()));
    addObserver(new myObserver.CheckActionObserver(ORDER_HIS_FILE_NAME, "order",
        new FileLineAppender()));
  }

  /**
   * Adds the order instruction to those two files and adds this
   * order's amount to the Product's total ordered amount.
   *
   * @param instruction a track to add onto.
   */
  public void addOrder(String instruction) {
    String[] lineList = instruction.split(" , ");
    String productName = lineList[4];
    Integer quantityToOrder = Integer.parseInt(lineList[2]);
    if (orderMap.containsKey(productName)) {
      orderMap.replace(productName, orderMap.get(productName)
          + quantityToOrder);
    } else {
      orderMap.put(productName, quantityToOrder);
    }
    setChanged();
    notifyObservers(instruction);
  }

  /**
   * Returns the corresponding total ordered amount of the Product
   * with productName.
   *
   * @param productName the name of the Product.
   * @return the Product's current total ordered amount.
   */
  public int getProductOrderQuantity(String productName) {
    return orderMap.get(productName);
  }

  /**
   * Tests whether this OrderManager has record about this product.
   *
   * @param productName the name of the Product.
   * @return true if OrderManager has record of this product, false otherwise.
   */
  public boolean hasProduct(String productName) {
    return orderMap.containsKey(productName);
  }

  /**
   * Decreases the current ordered amount of this product
   * if this OrderManager receives its order.
   *
   * @param productName the name of the Product.
   * @param amountDecrement amount on the order
   */
  public void decreaseOrderAmount(String productName, int amountDecrement) {
    if (orderMap.containsKey(productName)) {
      orderMap.replace(productName, orderMap.get(productName)
          - amountDecrement);
    }
  }
}
