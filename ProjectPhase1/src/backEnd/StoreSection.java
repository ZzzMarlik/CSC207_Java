package backEnd;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The StoreSection class.  A StoreSection is a tree node in StoreSection
 * tree. It records a parent node superSection and child node list
 * sectionHolder. I also records the products it contains and aisles where
 * its products locate at.
 *
 * @author Haolin Zhang
 */
public class StoreSection implements Serializable {

  /**
   * The name of this StoreSection.
   */
  private String name;

  /**
   * The aisles where products locate at.
   */
  private ArrayList<Aisle> aisleHolder;

  /**
   * The child nodes of this StoreSection.
   */
  private ArrayList<StoreSection> sectionHolder;

  /**
   * The products this StoreSection contains.
   */
  private ArrayList<Product> productHolder;

  /**
   * The parent node of this StoreSection.
   */
  private StoreSection superSection;

  /**
   * Allocates a new StoreSection with name.
   *
   * @param newName StoreSection name.
   */
  public StoreSection(String newName) {
    this.name = newName;
    aisleHolder = new ArrayList<>();
    sectionHolder = new ArrayList<>();
    productHolder = new ArrayList<>();
  }

  /**
   * Returns this car's name.
   *
   * @return this car's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns this car's product list.
   *
   * @return this car's product list.
   */
  public ArrayList<Product> getProductHolder() {
    return productHolder;
  }

  public ArrayList<StoreSection> getSectionHolder() {
    return sectionHolder;
  }

  /**
   * Returns this car's parent node.
   *
   * @return this car's parent node.
   */
  public StoreSection getSuperSection() {
    return superSection;
  }

  /**
   * Adds the newAisle into aisle list and update information into
   * parent node by using recursion.
   *
   * @param newAisle the new aisle.
   */
  void addAisleUp(Aisle newAisle) {
    if (!aisleHolder.contains(newAisle)) {
      aisleHolder.add(newAisle);
      if (superSection != null) {
        superSection.addAisleUp(newAisle);
      }
    }
  }

  /**
   * Deletes the aisleToRemove from aisle list and update information
   * into parent node by using recursion.
   *
   * @param aisleToRemove the new aisle.
   */
  void removeAisleUp(Aisle aisleToRemove) {
    if (aisleHolder.contains(aisleToRemove)) {
      for (Product product : productHolder) {
        if (product.getSuperAisle() == aisleToRemove) {
          return;
        }
      }
      aisleHolder.remove(aisleToRemove);
      if (superSection != null) {
        superSection.removeAisleUp(aisleToRemove);
      }
    }
  }

  /**
   * Adds the newProduct into product list and update information into
   * parent node by using recursion.
   *
   * @param newProduct the new product.
   */
  private void addProductUp(Product newProduct) {
    if (!productHolder.contains(newProduct)) {
      productHolder.add(newProduct);
      if (superSection != null) {
        superSection.addProductUp(newProduct);
      }
    }
  }

  /**
   * Deletes the product from product list and update information
   * into parent node by using recursion.
   *
   * @param product the product to be removed.
   */
  private void removeProductUp(Product product) {
    if (productHolder.contains(product)) {
      productHolder.remove(product);
      if (getSuperSection() != null) {
        superSection.removeProductUp(product);
      }
    }
  }

  /**
   * Deletes the product from product list.
   *
   * @param product the product to be removed.
   */
  public void removeProduct(Product product) {
    if (productHolder.contains(product)) {
      removeProductUp(product);
      removeAisleUp(product.getSuperAisle());
    }
  }

  /**
   * Deletes the subSection from StoreSection list.
   *
   * @param subSection the StoreSection to be removed.
   */
  public void removeSection(StoreSection subSection) {
    if (sectionHolder.contains(subSection)) {
      sectionHolder.remove(subSection);
      for (Product product : subSection.getProductHolder()) {
        removeProduct(product);
      }
    }
  }

  /**
   * Adds the newSection to subSection list.
   *
   * @param newSection the child node to be added.
   */
  public void addSection(StoreSection newSection) {
    if (!sectionHolder.contains(newSection)) {
      sectionHolder.add(newSection);
      for (Product product : newSection.getProductHolder()) {
        addProduct(product);
      }
    }
  }

  /**
   * Adds the newProduct to product list.
   *
   * @param newProduct the product to be added.
   */
  public void addProduct(Product newProduct) {
    addProductUp(newProduct);
    addAisleUp(newProduct.getSuperAisle());
  }

  /**
   * Changes the parent node and update information in both
   * old parent node and new parent node.
   *
   * @param newSuperSection the new parent node.
   */
  void setSuperSection(StoreSection newSuperSection) {
    StoreSection oldSuperSection = superSection;
    this.superSection = newSuperSection;
    if (oldSuperSection != null) {
      oldSuperSection.removeSection(this);
    }
    newSuperSection.addSection(this);
  }

  /**
   * Tests whether this StoreSection is a parent node.
   *
   * @return true if this StoreSection is a parent node, false otherwise.
   */
  private boolean whetherParentSection() {
    return sectionHolder.size() != 0;
  }

  /**
   * Tests whether this StoreSection is a leaf node.
   *
   * @return true if this StoreSection is a leaf node, false otherwise.
   */
  public boolean whetherLeafSection() {
    return !whetherParentSection();
  }

  @Override
  public final String toString() {
    String result = name + ":\n";
    result += "-aisle: ";
    for (Aisle aisle : aisleHolder) {
      result += aisle.getName() + ", ";
    }
    result += "\n-subsection: ";
    for (StoreSection subSection : sectionHolder) {
      result += subSection.getName() + ", ";
    }
    result += "\n-product: ";
    for (Product product : productHolder) {
      result += product.getName() + ", ";
    }
    return result;
  }
}
