package frontEnd;

import backEnd.Product;
import backEnd.StoreSection;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 * This frame is used to receive more detailed information of
 * new scanned-in product.
 *
 * @author Haolin Zhang, Make Zhang
 */
class InsertSectionFrame extends JFrame {

  /**
   * Event controller with today's store.
   */
  private EventOperator operator;

  /**
   * Visible tree structure of store sections.
   */
  private JTree tree;

  /**
   * The root node on the JTree.
   */
  private DefaultMutableTreeNode root;

  /**
   * The root section in the store.
   */
  private StoreSection rootSection;

  /**
   * Input text field for new section name.
   */
  private JTextField onSectionSectionNameTextField;

  /**
   * Input text field for parent section name.
   */
  private JTextField onSectionParentNameTextField;

  /**
   * Input text field for child section names.
   */
  private JTextField onSectionSectionListTextField;

  /**
   * Input text field for new section name.
   */
  private JTextField onProductSectionNameTextField;

  /**
   * Input text field for parent section name.
   */
  private JTextField onProductParentNameTextField;

  /**
   * Input text field for containing product names.
   */
  private JTextField onProductProductListTextField;

  /**
   * The panel on the frame.
   */
  private JPanel panel;

  /**
   * Allocates a new InsertSectionFrame which is used to receive
   * more detailed information of new scanned-in product.
   *
   * @param operator event controller with today's store.
   */
  InsertSectionFrame(EventOperator operator) {
    this.operator = operator;

    this.setSize(1000, 600);
    this.setLocationRelativeTo(null);
    this.setTitle("Insert");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(panel);

    //tree
    rootSection = operator.getRootSection();
    root = new DefaultMutableTreeNode(rootSection.getName());
    tree = new JTree(root);
    tree.setEditable(true);
    tree.setShowsRootHandles(true);
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

    startTree();

    JScrollPane scrollBox = new JScrollPane(tree);
    Dimension d = scrollBox.getPreferredSize();
    d.width = 200;
    d.height = 400;
    scrollBox.setPreferredSize(d);
    gbc.gridx += 2;
    panel.add(scrollBox, gbc);
    generateEmptyLine(gbc);

    //insert-section-on-section
    gbc.gridy += 2;
    gbc.gridx = 0;
    JLabel onSectionLabel = new JLabel("Insert-section-on-section:");
    panel.add(onSectionLabel, gbc);
    onSectionSectionNameTextField = new JTextField("Enter new section name", 15);
    onSectionSectionNameTextField.addFocusListener(new TextFocusListener(
        onSectionSectionNameTextField));
    gbc.gridx++;
    panel.add(onSectionSectionNameTextField, gbc);
    onSectionParentNameTextField = new JTextField("Enter parent section name", 17);
    onSectionParentNameTextField.addFocusListener(new TextFocusListener(
        onSectionParentNameTextField));
    gbc.gridx++;
    panel.add(onSectionParentNameTextField, gbc);
    onSectionSectionListTextField = new JTextField(
        "Enter all child section name, separated by ' , '", 27);
    onSectionSectionListTextField.addFocusListener(new TextFocusListener(
        onSectionSectionListTextField));
    gbc.gridx++;
    panel.add(onSectionSectionListTextField, gbc);
    JButton onSectionButton = new JButton("Insert");
    OnSectionListener onSectionListener = new OnSectionListener();
    onSectionButton.addActionListener(onSectionListener);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(onSectionButton, gbc);
    generateEmptyLine(gbc);

    //insert-section-on-product
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    JLabel onProductLabel = new JLabel("Insert-section-on-product:");
    panel.add(onProductLabel, gbc);
    onProductSectionNameTextField = new JTextField("Enter new section name", 15);
    onProductSectionNameTextField.addFocusListener(new TextFocusListener(
        onProductSectionNameTextField));
    gbc.gridx++;
    panel.add(onProductSectionNameTextField, gbc);
    onProductParentNameTextField = new JTextField("Enter parent section name", 17);
    onProductParentNameTextField.addFocusListener(new TextFocusListener(
        onProductParentNameTextField));
    gbc.gridx++;
    panel.add(onProductParentNameTextField, gbc);
    onProductProductListTextField = new JTextField(
        "Enter all child product name, separated by ' , '", 27);
    onProductProductListTextField.addFocusListener(new TextFocusListener(
        onProductProductListTextField));
    gbc.gridx++;
    panel.add(onProductProductListTextField, gbc);
    JButton onProductButton = new JButton("Insert");
    OnProductListener onProductListener = new OnProductListener();
    onProductButton.addActionListener(onProductListener);
    gbc.gridx++;
    panel.add(onProductButton, gbc);
    generateEmptyLine(gbc);

    //view-tree
    gbc.gridx = 2;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.CENTER;
    JButton viewTreeButton = new JButton("view-tree");
    ViewTreeListener viewTreeListener = new ViewTreeListener();
    viewTreeButton.addActionListener(viewTreeListener);
    panel.add(viewTreeButton, gbc);

    this.setVisible(true);
  }

  /**
   * Show the changed tree on the frame.
   */
  private void showTree() {

    root.removeAllChildren();
    startTree();
    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    model.reload();
  }

  /**
   * Build up the visible tree structure according to current store sections.
   */
  private void startTree() {
    for (StoreSection subSection : rootSection.getSectionHolder()) {
      buildUpTree(root, subSection);
    }
    for (Product product : rootSection.getProductHolder()) {
      DefaultMutableTreeNode productFile = new DefaultMutableTreeNode(product.getName());
      root.add(productFile);
    }
  }

  /**
   * Insert tree nodes on JTree by viewing store section tree in
   * pre-order traversal.
   *
   * @param root the root node on the JTree.
   * @param currentSection the current store section node.
   */
  private void buildUpTree(DefaultMutableTreeNode root, StoreSection currentSection) {
    if (currentSection.whetherLeafSection()) {
      DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(currentSection.getName());
      root.add(newFile);
      if (currentSection.getProductHolder().size() == 0) {
        DefaultMutableTreeNode emptyFile = new DefaultMutableTreeNode("place-holder");
        newFile.add(emptyFile);
      }
      for (Product product : currentSection.getProductHolder()) {
        DefaultMutableTreeNode productFile = new DefaultMutableTreeNode(product.getName());
        newFile.add(productFile);
      }
    } else {
      DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(currentSection.getName());
      root.add(newFile);
      for (StoreSection subSection : currentSection.getSectionHolder()) {
        buildUpTree(newFile, subSection);
      }
      for (Product product : currentSection.getProductHolder()) {
        DefaultMutableTreeNode productFile = new DefaultMutableTreeNode(product.getName());
        newFile.add(productFile);
      }
    }
  }

  /**
   * FocusListener which focuses on text field input.
   *
   * @author Haolin Zhang
   */
  private class TextFocusListener implements FocusListener {

    /**
     * The target text field this FocusListener needs to focus on.
     */
    private JTextField textField;

    /**
     * The default text in the target text field.
     */
    private String defaultText;

    /**
     * Allocates a new TextFocusListener which focuses on text field input.
     *
     * @param textField the target text field this FocusListener needs to focus on.
     */
    TextFocusListener(JTextField textField) {
      this.textField = textField;
      defaultText = textField.getText();
    }

    @Override
    public void focusGained(FocusEvent e) {
      if (textField.getText().equals(defaultText)) {
        textField.setText("");
      }
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (textField.getText().equals("")) {
        textField.setText(defaultText);
      }
    }
  }

  /**
   * ActionListener which listens to insert-section-on-section button.
   *
   * @author Haolin Zhang
   */
  private class OnSectionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String sectionName = onSectionSectionNameTextField.getText();
      String parentName = onSectionParentNameTextField.getText();
      String sectionList = onSectionSectionListTextField.getText();

      if (!(invalidString(sectionName) || invalidString(parentName))) {
        if (!operator.whetherHasSection(parentName)) {
          JOptionPane.showMessageDialog(InsertSectionFrame.this,
              "Invalid parent section");
          onSectionSectionNameTextField.setText("Enter new section name");
          onSectionParentNameTextField.setText("Enter parent section name");
          onSectionSectionListTextField.setText("Enter all child section name, "
              + "separated by ' , '");
          return;
        }
        if (invalidString(sectionList)) {
            operator.insertSectionOnSection(sectionName, parentName, "");
            onSectionSectionNameTextField.setText("Enter new section name");
            onSectionParentNameTextField.setText("Enter parent section name");
            onSectionSectionListTextField.setText("Enter all child section name, "
                    + "separated by ' , '");
            return;
        }
        for (String subSectionName : sectionList.split(" , ")) {
          if (!operator.whetherHasSection(subSectionName)) {
            JOptionPane.showMessageDialog(InsertSectionFrame.this,
                "Invalid child section");
            onSectionSectionNameTextField.setText("Enter new section name");
            onSectionParentNameTextField.setText("Enter parent section name");
            onSectionSectionListTextField.setText("Enter all child section name, "
                + "separated by ' , '");
            return;
          }
        }
        operator.insertSectionOnSection(sectionName, parentName, sectionList);
      }
      onSectionSectionNameTextField.setText("Enter new section name");
      onSectionParentNameTextField.setText("Enter parent section name");
      onSectionSectionListTextField.setText("Enter all child section name, separated by ' , '");
    }
  }

  /**
   * ActionListener which listens to insert-section-on-product button.
   *
   * @author Haolin Zhang
   */
  private class OnProductListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String sectionName = onProductSectionNameTextField.getText();
      String parentName = onProductParentNameTextField.getText();
      String productList = onProductProductListTextField.getText();

      if (!(invalidString(sectionName) || invalidString(parentName))) {
        if (!operator.whetherHasSection(parentName)) {
          JOptionPane.showMessageDialog(InsertSectionFrame.this,
              "Invalid parent section");
          onProductSectionNameTextField.setText("Enter new section name");
          onProductParentNameTextField.setText("Enter parent section name");
          onProductProductListTextField.setText("Enter all child product name, "
              + "separated by ' , '");
          return;
        }
        if (invalidString(productList)) {
            operator.insertSectionOnProduct(sectionName, parentName, "");
            onProductSectionNameTextField.setText("Enter new section name");
            onProductParentNameTextField.setText("Enter parent section name");
            onProductProductListTextField.setText("Enter all child product name, "
                    + "separated by ' , '");
            return;
        }
        for (String productName : productList.split(" , ")) {
          if (!operator.whetherHasProduct(productName)) {
            JOptionPane.showMessageDialog(InsertSectionFrame.this,
                "Invalid product");
            onProductSectionNameTextField.setText("Enter new section name");
            onProductParentNameTextField.setText("Enter parent section name");
            onProductProductListTextField.setText("Enter all child product name, "
                + "separated by ' , '");
            return;
          }
        }
        operator.insertSectionOnProduct(sectionName, parentName, productList);
      }
      onProductSectionNameTextField.setText("Enter new section name");
      onProductParentNameTextField.setText("Enter parent section name");
      onProductProductListTextField.setText("Enter all child product name, separated by ' , '");
    }
  }

  /**
   * ActionListener which listens to view-tree button.
   *
   * @author Haolin Zhang
   */
  private class ViewTreeListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      showTree();
    }
  }

  /**
   * Adds an empty line to create more space on the layout of panel.
   *
   * @param gbc grid on the panel.
   */
  private void generateEmptyLine(GridBagConstraints gbc) {
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("            "), gbc);
  }

  /**
   * Determine whether the string is invalid:
   * 1. equals to ""
   * 2. start with "Enter"
   *
   * @param input String to determine
   * @return whether it is invalid.
   */
  private boolean invalidString(String input) {
    return (input.equals("") || input.startsWith("Enter"));
  }
}
