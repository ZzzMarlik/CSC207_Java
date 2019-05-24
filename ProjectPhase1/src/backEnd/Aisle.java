package backEnd;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Aisle is used to store products.
 * Each Aisle can also hold many StoreSections in this Store,
 * but each StoreSection need to be a leafStore,
 * which means it does not have any subSection.
 *
 * @author Ziyue Xu
 */
public class Aisle implements Serializable {

  /**
   * The name of this Aisle.
   */
  private String name;

  /**
   * The sections this Aisle holds
   */
  private ArrayList<StoreSection> leafSectionList;

  /**
   * All products this Aisle holds.
   */
  private ArrayList<Product> productList;

  /**
   * Create a new Aisle with name
   *
   * @param name The name of this Aisle. The format is Aisle X, X is an integer.
   */
  public Aisle(String name) {
    this.name = name;
    leafSectionList = new ArrayList<>();
    productList = new ArrayList<>();
  }

  /**
   * Return the name of this Aisle.
   *
   * @return The name of this Aisle.
   */
  public String getName() {
    return name;
  }

  /**
   * Add a new StoreSection newSection to this Aisle.
   *
   * @param newSection new StoreSection to be added to this Aisle. This StoreSection need to be a
   * leafSection, which means it does not have subSection.
   */
  private void addSection(StoreSection newSection) {
    if (!leafSectionList.contains(newSection)) {
      leafSectionList.add(newSection);
    }
    newSection.addAisleUp(this);
  }

  /**
   * Add a new Product newProduct to this Aisle.
   *
   * @param newProduct a new Product to be added to this Aisle.
   */
  void addProduct(Product newProduct) {
    productList.add(newProduct);
    addSection(newProduct.getSuperSection());
  }

  /**
   * Remove productToRemove from this Aisle.
   *
   * @param productToRemove The Product to be removed from this Aisle.
   */
  void removeProduct(Product productToRemove) {
    productList.remove(productToRemove);
    StoreSection targetSection = productToRemove.getSuperSection();
    boolean stillContain = false;
    for (Product product : productList) {
      if (product.getSuperSection() == targetSection) {
        stillContain = true;
        break;
      }
    }
    if (!stillContain) {
      leafSectionList.remove(targetSection);
      targetSection.removeAisleUp(this);
    }
  }

  /**
   * Return a String representation of this Aisle.
   *
   * @return String representation.
   */
  @Override
  public String toString() {
    String result = name + ":\n";
    result += "-section: ";
    for (StoreSection section : leafSectionList) {
      result += section.getName() + ", ";
    }
    result += "\n-product: ";
    for (Product product : productList) {
      result += product.getName() + ", ";
    }
    return result;
  }
}

