package frontEnd;

import backEnd.Store;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;

/**
 * This frame is the main frame for user to use our program.
 *
 * @author Haolin Zhang, Ziyue Xu, Make Zhang
 */
class EventFrame extends JFrame {

  /**
   * Event controller with today's store.
   */
  private EventOperator operator;

  /**
   * Selection list for categories of view.
   */
  private JComboBox<String> viewComboBox;

  /**
   * Input text field for product to view.
   */
  private JTextField viewTextField;

  /**
   * Input text field for ordered product name.
   */
  private JTextField orderNameTextField;

  /**
   * Input text field for ordered product quantity.
   */
  private JTextField orderQuantityTextField;

  /**
   * Input text field for ordered product upc.
   */
  private JTextField orderUpcTextField;

  /**
   * Input text field for ordered product distributor.
   */
  private JTextField orderDistributorTextField;

  /**
   * Input text field for scanned-out product upc.
   */
  private JTextField scanOutUpcTextField;

  /**
   * Input text field for scanned-out product quantity.
   */
  private JTextField scanOutQuantityTextField;

  /**
   * Input text field for scanned-in product upc.
   */
  private JTextField scanInUpcTextField;

  /**
   * Input text field for scanned-in product quantity.
   */
  private JTextField scanInQuantityTextField;

  /**
   * Input text field for the name of product to change location.
   */
  private JTextField changeLocationNameTextField;

  /**
   * Input text field for the new aisle location.
   */
  private JComboBox<String> newLocationComboBox;

  /**
   * Input text field for the name of product to change price.
   */
  private JTextField changePriceNameTextField;

  /**
   * Input text field for the new price.
   */
  private JTextField newPriceTextField;

  /**
   * Input text field for the name of product to set discount.
   */
  private JTextField setDisNameTextField;

  /**
   * Input text field for the discount number.
   */
  private JTextField setDiscountTextField;

  /**
   * Input text field for the start date of discount.
   */
  private JTextField setStartDateTextField;

  /**
   * Input text field for the end date of discount.
   */
  private JTextField setEndDateTextField;

  /**
   * Selection list of all legal actions in the store.
   */
  private JComboBox<String> addActionActionComboBox;

  /**
   * Selection list of all legal users in the store.
   */
  private JComboBox<String> addActionUserComboBox;

  /**
   * Selection list of all legal actions in the store.
   */
  private JComboBox<String> deleteActionActionComboBox;

  /**
   * Selection list of all legal users in the store.
   */
  private JComboBox<String> deleteActionUserComboBox;

  /**
   * Text area to show all action output results.
   */
  private JTextArea outputTextArea;

  /**
   * Current logged-in user.
   */
  private String currentUser;

  /**
   * Constant folder to contain all files to be updated.
   */
  private static final String DIR_NAME = "ProjectPhase1/src/recordForChange";

  /**
   * Constant destination file to serialize the store to.
   */
  private static final String SER_FILE_NAME = "ProjectPhase1/src/recordForChange/store.ser";

  /**
   * Allocates a new EventFrame which is the main frame for user to use our program.
   *
   * @param operator event controller with today's store.
   * @param user current logged-in user.
   */
  EventFrame(EventOperator operator, String user) {
    this.operator = operator;
    this.currentUser = user;
    this.setSize(1200, 700);
    this.setLocationRelativeTo(null);
    this.setTitle(operator.getTodayDate() + " ---- " + "Login as: " + currentUser);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(panel);

    //output
    gbc.gridwidth += 5;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    outputTextArea = new JTextArea(15, 20);
    outputTextArea.setWrapStyleWord(true);
    JScrollPane scrollPaneOutput = new JScrollPane(outputTextArea,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panel.add(scrollPaneOutput, gbc);
    gbc.gridwidth -= 5;

    //enable buttons according to current user's allowed actions.
    enableButtons(user, gbc, panel);

    this.setVisible(true);
  }

  /**
   * Adds all components of view part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void viewOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridx = x;
    grid.gridy = y;
    grid.anchor = GridBagConstraints.LINE_END;
    JLabel viewLabel = new JLabel("View:");
    panel.add(viewLabel, grid);
    grid.gridx++;
    viewComboBox = new JComboBox<>();
    panel.add(viewComboBox, grid);
    viewTextField = new JTextField("Enter product name", 13);
    viewTextField.addFocusListener(new TextFocusListener(viewTextField));
    grid.gridx++;
    panel.add(viewTextField, grid);
    JButton viewButton = new JButton("View");
    grid.gridx += 3;
    panel.add(viewButton, grid);
    ViewListener viewListener = new ViewListener();
    viewButton.addActionListener(viewListener);
  }

  /**
   * Adds all components of order part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void orderOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel orderLabel = new JLabel("Order:");
    panel.add(orderLabel, grid);
    grid.gridx++;
    orderNameTextField = new JTextField("Enter product name", 10);
    orderNameTextField.addFocusListener(new TextFocusListener(orderNameTextField));
    panel.add(orderNameTextField, grid);
    orderQuantityTextField = new JTextField("Enter quantity", 10);
    orderQuantityTextField.addFocusListener(new TextFocusListener(orderQuantityTextField));
    grid.gridx++;
    panel.add(orderQuantityTextField, grid);
    orderUpcTextField = new JTextField("Enter upc", 10);
    orderUpcTextField.addFocusListener(new TextFocusListener(orderUpcTextField));
    grid.gridx++;
    panel.add(orderUpcTextField, grid);
    orderDistributorTextField = new JTextField("Enter distributor", 10);
    orderDistributorTextField.addFocusListener(new TextFocusListener(orderDistributorTextField));
    grid.gridx++;
    panel.add(orderDistributorTextField, grid);
    JButton orderButton = new JButton("Order");
    grid.gridx++;
    panel.add(orderButton, grid);
    OrderListener orderListener = new OrderListener();
    orderButton.addActionListener(orderListener);
  }

  /**
   * Adds all components of scan-out part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void scanOutOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel scanOutLabel = new JLabel("Scan-out:");
    panel.add(scanOutLabel, grid);
    scanOutUpcTextField = new JTextField("Enter upc", 10);
    scanOutUpcTextField.addFocusListener(new TextFocusListener(scanOutUpcTextField));
    grid.gridx++;
    panel.add(scanOutUpcTextField, grid);
    scanOutQuantityTextField = new JTextField("Enter quantity", 10);
    scanOutQuantityTextField.addFocusListener(new TextFocusListener(scanOutQuantityTextField));
    grid.gridx++;
    panel.add(scanOutQuantityTextField, grid);
    JButton scanOutButton = new JButton("Scan-out");
    grid.gridx += 3;
    panel.add(scanOutButton, grid);
    ScanOutListener scanOutListener = new ScanOutListener();
    scanOutButton.addActionListener(scanOutListener);
  }

  /**
   * Adds all components of scan-in part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void scanInOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel scanInLabel = new JLabel("Scan-in:");
    panel.add(scanInLabel, grid);
    scanInUpcTextField = new JTextField("Enter upc", 10);
    scanInUpcTextField.addFocusListener(new TextFocusListener(scanInUpcTextField));
    grid.gridx++;
    panel.add(scanInUpcTextField, grid);
    scanInQuantityTextField = new JTextField("Enter quantity", 10);
    scanInQuantityTextField.addFocusListener(new TextFocusListener(scanInQuantityTextField));
    grid.gridx++;
    panel.add(scanInQuantityTextField, grid);
    JButton scanInButton = new JButton("Scan-in");
    grid.gridx += 3;
    panel.add(scanInButton, grid);
    ScanInListener scanInListener = new ScanInListener(this);
    scanInButton.addActionListener(scanInListener);
  }

  /**
   * Adds all components of change-location part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void changeLocationOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel changeLocationLabel = new JLabel("Change Location: ");
    panel.add(changeLocationLabel, grid);

    grid.gridx++;
    changeLocationNameTextField = new JTextField("Enter product name", 10);
    changeLocationNameTextField
        .addFocusListener(new TextFocusListener(changeLocationNameTextField));
    panel.add(changeLocationNameTextField, grid);

    grid.gridx++;
    newLocationComboBox = new JComboBox<>();
    ArrayList<String> aisleNameList = operator.getAisleNameList();
    for (String aisleName : aisleNameList) {
      newLocationComboBox.addItem(aisleName);
    }
    panel.add(newLocationComboBox, grid);

    grid.gridx += 3;
    JButton changeLocationButton = new JButton("Change");
    changeLocationButton.addActionListener(new ChangeLocationListener());
    panel.add(changeLocationButton, grid);
  }

  /**
   * Adds all components of change-price part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void changePriceOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel changePriceLabel = new JLabel("Change Price: ");
    panel.add(changePriceLabel, grid);

    grid.gridx++;
    changePriceNameTextField = new JTextField("Enter product name", 10);
    changePriceNameTextField.addFocusListener(new TextFocusListener(changePriceNameTextField));
    panel.add(changePriceNameTextField, grid);

    grid.gridx++;
    newPriceTextField = new JTextField("Enter new price", 10);
    newPriceTextField.addFocusListener(new TextFocusListener(newPriceTextField));
    panel.add(newPriceTextField, grid);

    grid.gridx += 3;
    JButton changePriceButton = new JButton("Change");
    changePriceButton.addActionListener(new ChangePriceListener());
    panel.add(changePriceButton, grid);
  }

  /**
   * Adds all components of set-discount part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void setDiscountOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    JLabel setDiscountLabel = new JLabel("Set Discount: ");
    panel.add(setDiscountLabel, grid);

    grid.gridx++;
    setDisNameTextField = new JTextField("Enter product name", 10);
    setDisNameTextField.addFocusListener(new TextFocusListener(setDisNameTextField));
    panel.add(setDisNameTextField, grid);

    grid.gridx++;
    setDiscountTextField = new JTextField("Enter discount", 10);
    setDiscountTextField.addFocusListener(new TextFocusListener(setDiscountTextField));
    panel.add(setDiscountTextField, grid);

    grid.gridx++;
    setStartDateTextField = new JTextField("Enter start date", 10);
    setStartDateTextField.addFocusListener(new TextFocusListener(setStartDateTextField));
    panel.add(setStartDateTextField, grid);

    grid.gridx++;
    setEndDateTextField = new JTextField("Enter end date", 10);
    setEndDateTextField.addFocusListener(new TextFocusListener(setEndDateTextField));
    panel.add(setEndDateTextField, grid);

    grid.gridx++;
    JButton setDiscountButton = new JButton("Set");
    setDiscountButton.addActionListener(new SetDiscountListener());
    panel.add(setDiscountButton, grid);
  }

  /**
   * Adds all components of insert-section part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void insertSectionOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    panel.add(new JLabel("Insert-Section: "), grid);
    grid.gridx += 5;
    JButton insertSectionButton = new JButton("Insert-Section");
    InsertSectionListener insertSectionListener = new InsertSectionListener();
    insertSectionButton.addActionListener(insertSectionListener);
    panel.add(insertSectionButton, grid);
    grid.gridx -= 5;
  }

  /**
   * Adds all components of restart part on this frame.
   *
   * @param panel current panel on the frame.
   * @param grid the grid location.
   * @param x x-coordinate on the panel.
   * @param y y-coordinate on the panel.
   */
  private void restartOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridy = y;
    grid.gridx = x;
    grid.anchor = GridBagConstraints.LINE_START;
    String command = "Restart as new";
    JButton restartButton = new JButton(command);
    restartButton.setForeground(Color.RED);
    panel.add(restartButton, grid);
    ThreeOptionsListener restartListener = new ThreeOptionsListener(this,
        command);
    restartButton.addActionListener(restartListener);
  }


  /**
   * Add all things needed for exit option on the panel.
   *
   * @param panel panel on frame.
   * @param grid the grid location
   * @param x x coordinate on the panel
   * @param y y coordinate on the panel
   */
  private void exitOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridx = x;
    grid.gridy = y;
    grid.anchor = GridBagConstraints.CENTER;
    String command = "Exit";
    JButton exitButton = new JButton(command);
    panel.add(exitButton, grid);
    ThreeOptionsListener exitListener = new ThreeOptionsListener(this,
        command);
    exitButton.addActionListener(exitListener);
  }

  /**
   * Add all things needed for log out option on the panel.
   *
   * @param panel panel on frame.
   * @param grid the grid location
   * @param x x coordinate on the panel
   * @param y y coordinate on the panel
   */
  private void logOutOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridx = x;
    grid.gridy = y;
    grid.anchor = GridBagConstraints.LINE_END;
    String command = "Logout";
    JButton logoutButton = new JButton("Logout");
    panel.add(logoutButton, grid);
    ThreeOptionsListener logoutListener = new ThreeOptionsListener(this,
        command);
    logoutButton.addActionListener(logoutListener);
  }

  /**
   * Add all things needed for add-action-to-user option on the panel.
   *
   * @param panel panel on frame.
   * @param grid the grid location
   * @param x x coordinate on the panel
   * @param y y coordinate on the panel
   */
  private void addActionOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridx = x;
    grid.gridy = y;
    JLabel addActionLabel = new JLabel("Add-action-to-user:");
    panel.add(addActionLabel, grid);
    grid.gridx++;
    addActionActionComboBox = new JComboBox<>();
    for (String action : operator.getAllLegalActions()) {
      addActionActionComboBox.addItem(action);
    }
    panel.add(addActionActionComboBox, grid);
    grid.gridx++;
    addActionUserComboBox = new JComboBox<>();
    for (String user : operator.getAllLegalUsers()) {
      addActionUserComboBox.addItem(user);
    }
    panel.add(addActionUserComboBox, grid);
    grid.gridx += 3;
    JButton addActionButton = new JButton("Add-action");
    panel.add(addActionButton, grid);
    AddActionToUserListener addActionToUserListener = new AddActionToUserListener();
    addActionButton.addActionListener(addActionToUserListener);
  }

  /**
   * Add all things needed for delete-action-to-user option on the panel.
   *
   * @param panel panel on frame.
   * @param grid the grid location
   * @param x x coordinate on the panel
   * @param y y coordinate on the panel
   */
  private void deleteActionOption(JPanel panel, GridBagConstraints grid, int x, int y) {
    grid.gridx = x;
    grid.gridy = y;
    JLabel deleteActionLabel = new JLabel("Delete-action-from-user:");
    panel.add(deleteActionLabel, grid);
    grid.gridx++;
    deleteActionActionComboBox = new JComboBox<>();
    for (String action : operator.getAllLegalActions()) {
      deleteActionActionComboBox.addItem(action);
    }
    panel.add(deleteActionActionComboBox, grid);
    grid.gridx++;
    deleteActionUserComboBox = new JComboBox<>();
    for (String user : operator.getAllLegalUsers()) {
      deleteActionUserComboBox.addItem(user);
    }
    panel.add(deleteActionUserComboBox, grid);
    grid.gridx += 3;
    JButton deleteActionButton = new JButton("Delete-action");
    panel.add(deleteActionButton, grid);
    DeleteActionFromUserListener deleteActionFromUserListener = new DeleteActionFromUserListener();
    deleteActionButton.addActionListener(deleteActionFromUserListener);
  }

  /**
   * Return the output JTextArea on the current panel.
   *
   * @return the output JTextArea
   */
  JTextArea getOutputTextArea() {
    return outputTextArea;
  }

  /**
   * Add different above options to the current
   * panel according to user type.
   *
   * @param user the current user
   * @param grid the grid location
   * @param panel panel on frame.
   */
  private void enableButtons(String user, GridBagConstraints grid, JPanel panel) {
    ArrayList<String> legalActions = operator.getLegalActions(user);
    boolean alreadyHasViewPart = false;
    boolean alreadyHasInsertPart = false;
    int xCoordinate = 0;
    int yCoordinate = 1;
    grid.insets = new Insets(10, 3, 10, 3);
    for (String action : legalActions) {
      String[] actionSplit = action.split("-");
      if (actionSplit[0].equals("view")) {
        if (alreadyHasViewPart) {
          viewComboBox.addItem(action.substring(5));
        } else {
          viewOption(panel, grid, xCoordinate, yCoordinate);
          alreadyHasViewPart = true;
          yCoordinate++;
          viewComboBox.addItem(action.substring(5));
        }
      } else if (action.equals("scan-in")) {
        scanInOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("order")) {
        orderOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("scan-out")) {
        scanOutOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("change-location")) {
        changeLocationOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (actionSplit[0].equals("insert")) {
        if (!alreadyHasInsertPart) {
          insertSectionOption(panel, grid, xCoordinate, yCoordinate);
          yCoordinate++;
          alreadyHasInsertPart = true;
        }
      } else if (action.equals("change-price")) {
        changePriceOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("set-discount")) {
        setDiscountOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("add-action-to-user")) {
        addActionOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      } else if (action.equals("delete-action-from-user")) {
        deleteActionOption(panel, grid, xCoordinate, yCoordinate);
        yCoordinate++;
      }
    }
    xCoordinate++;
    grid.insets = new Insets(30, 0, 0, 50);
    restartOption(panel, grid, xCoordinate, yCoordinate);
    xCoordinate++;
    exitOption(panel, grid, xCoordinate, yCoordinate);
    xCoordinate++;
    logOutOption(panel, grid, xCoordinate, yCoordinate);
  }

  /**
   * ViewListener is used to listen to all actions of view.
   */
  private class ViewListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String result = "";
      String warning = "Please enter the name of the product"
          + " that you want to view %s of";
      String productName = viewTextField.getText();
      if (viewComboBox.getSelectedItem() == null) {
        return;
      }
      switch ((String) viewComboBox.getSelectedItem()) {
        case "location":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "location");
          } else {
            result = operator.viewLocation(productName);
          }
          break;
        case "cost":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "cost");
          } else {
            result = operator.viewCost(productName);
          }
          break;
        case "price-history":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "price history");
          } else {
            result = operator.viewPriceHistory(productName);
          }
          break;
        case "current-price":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "current price");
          } else {
            result = operator.viewCurrentPrice(productName);
          }
          break;
        case "sale-date":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "sale date");
          } else {
            result = operator.viewSaleDate(productName);
          }
          break;
        case "order-history":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "order history");
          } else {
            result = operator.viewOrderHistory(productName);
          }
          break;
        case "quantity":
          if (productName.startsWith("Enter")) {
            result = String.format(warning, "quantity");
          } else {
            result = operator.viewQuantity(productName);
          }
          break;
        case "total-order-history":
          result = operator.viewTotalOrderHistory();
          break;
        case "pending-order":
          result = operator.viewPendingOrder();
          break;
        case "revenue":
          result = operator.viewRevenue();
          break;
        case "profit":
          result = operator.viewProfit();
          break;
        case "store":
          result = operator.viewStore();
          break;
        default:
          break;
      }
      outputTextArea.setText(result);
      viewTextField.setText("Enter product name");
    }
  }

  /**
   * TextFocusListener is used whenever a component gains or
   * loses the keyboard focus.
   */
  private class TextFocusListener implements FocusListener {

    /**
     * textField to focus.
     */
    private JTextField textField;

    /**
     * the default test of each textField.
     */
    private String defaultText;

    /**
     * Create a new TextFocusListener with textField needed to focus on.
     *
     * @param textField the textField needed to focus on.
     */
    private TextFocusListener(JTextField textField) {
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
   * ViewListener is used to listen to actions of order.
   */
  private class OrderListener implements ActionListener {

    /**
     * The warning message to notice if user miss input required information.
     */
    private static final String WARNING = "If the product exists in store:\n"
        + "Enter name and quantity.\n"
        + "If it is new:\n"
        + "Enter name, quantity, upc and distributor.";

    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        String productName = orderNameTextField.getText();
        String quantity = orderQuantityTextField.getText();
        String upc = orderUpcTextField.getText();
        String distributor = orderDistributorTextField.getText();

        // If we already has this kind of product in store.
        if (operator.whetherHasProduct(productName)) {
          if (inValidString(productName) || inValidString(quantity)) {
            outputTextArea.setText(WARNING);
          } else {
            outputTextArea.setText(operator.order(Integer.parseInt(quantity),
                productName, operator.getUpc(productName), currentUser,
                operator.viewDistributor(productName)));
          }
        } else {
          if (inValidString(productName) || inValidString(quantity) ||
              inValidString(upc) || inValidString(distributor)) {
            outputTextArea.setText(WARNING);
          } else {
            outputTextArea.setText(operator.order(Integer.parseInt(quantity),
                productName, upc, currentUser, distributor));
          }
        }
      } catch (Exception ee) {
        outputTextArea.setText("Quantity must be an integer");
      }
      orderNameTextField.setText("Enter product name");
      orderQuantityTextField.setText("Enter quantity");
      orderUpcTextField.setText("Enter upc");
      orderDistributorTextField.setText("Enter distributor");
    }
  }

  /**
   * ScanOutListener is used to listen to action of scanning out products.
   */
  private class ScanOutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      if (inValidString(scanOutQuantityTextField.getText())
          || inValidString(scanOutUpcTextField.getText())) {
        outputTextArea.setText("Please enter both upc and quantity");
      } else {
        try {
          outputTextArea.setText(operator.scanOut(
              Integer.parseInt(scanOutQuantityTextField.getText()),
              scanOutUpcTextField.getText()));
        } catch (Exception notInt) {
          outputTextArea.setText("Quantity should be an integer");
        }
      }
      scanOutQuantityTextField.setText("Enter quantity");
      scanOutUpcTextField.setText("Enter upc");
    }
  }

  /**
   * ScanOutListener is used to listen to action of scanning in products.
   * There are two conditions: the product previously existed in the store,
   * or it is a new kind product for this store.
   */
  private class ScanInListener implements ActionListener {

    /**
     * This frame.
     */
    private EventFrame currentFrame;

    /**
     * Create a new ScanInListener with EventFrame currentFrame
     *
     * @param currentFrame the EventFrame is used when a new ScanInNewFrame is needed.
     */
    ScanInListener(EventFrame currentFrame) {
      this.currentFrame = currentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (inValidString(scanInUpcTextField.getText())
          || inValidString(scanInQuantityTextField.getText())) {
        outputTextArea.setText("Please enter both upc and quantity");
      } else {
        try {
          String upc = scanInUpcTextField.getText();
          int quantity = Integer.parseInt(scanInQuantityTextField.getText());
          // Old product is scanned in.
          if (operator.whetherHasUpc(upc)) {
            outputTextArea.setText(operator.scanInOld(upc, quantity));
          } else {
            // New product is scanned in.
            new ScanInNewFrame(upc, quantity, currentFrame, operator);
          }
        } catch (Exception notInt) {
          outputTextArea.setText("Quantity should be an integer");
        }
      }
      scanInUpcTextField.setText("Enter upc");
      scanInQuantityTextField.setText("Enter quantity");
    }
  }

  /**
   * ChangeLocationListener is used to listen to action of
   * changing location of a products.
   */
  private class ChangeLocationListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      if (inValidString(changeLocationNameTextField.getText())) {
        outputTextArea.setText("Please enter the name of the "
            + "product you want to change location of");
      } else {
        String productName = changeLocationNameTextField.getText();
        String newLocation = (String) newLocationComboBox.getSelectedItem();
        outputTextArea.setText(operator.changeLocation(productName, newLocation));
        changeLocationNameTextField.setText("Enter product name");
      }
    }
  }

  /**
   * ChangePriceListener is used to listen to action of
   * changing price of a products.
   */
  private class ChangePriceListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String productName = changePriceNameTextField.getText();
      String price = newPriceTextField.getText();
      if (inValidString(productName) || inValidString(price)) {
        outputTextArea.setText("Please enter both name and new price");
      } else {
        try {
          double newPrice = Double.parseDouble(price);
          outputTextArea.setText(operator.changePrice(productName, newPrice, currentUser));
        } catch (Exception notDouble) {
          outputTextArea.setText("Price should be a double");
        }
      }
      changePriceNameTextField.setText("Enter product name");
      newPriceTextField.setText("Enter new price");
    }
  }

  /**
   * SetDiscountListener is used to listen to action of
   * setting discount of a products.
   */
  private class SetDiscountListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String productName = setDisNameTextField.getText();
      String discount = setDiscountTextField.getText();
      String startDate = setStartDateTextField.getText();
      String endDate = setEndDateTextField.getText();

      if (inValidString(productName) || inValidString(discount) ||
          inValidString(startDate) || inValidString(endDate)) {
        outputTextArea.setText("Please enter all required information");
      } else {
        try {
          double discountRate = Double.parseDouble(discount);
          outputTextArea.setText(operator.setDiscount(productName, discountRate,
              startDate, endDate, currentUser));
        } catch (Exception notDouble) {
          outputTextArea.setText("Discount ratio should be a double and start date"
              + " and end date should follow the format XXXX-XX-XX");
        }
      }
      setDisNameTextField.setText("Enter product name");
      setDiscountTextField.setText("Enter discount");
      setStartDateTextField.setText("Enter start date");
      setEndDateTextField.setText("Enter end date");
    }
  }

  /**
   * InsertSectionListener is used to listen to action of
   * insert a new StoreSection.
   */
  private class InsertSectionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      new InsertSectionFrame(operator);
    }
  }

  /**
   * AddActionToUserListener is used to listen to action of
   * adding a new action to user.
   */
  private class AddActionToUserListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String actionToAdd = (String) addActionActionComboBox.getSelectedItem();
      String user = (String) addActionUserComboBox.getSelectedItem();

      operator.addActionToUser(actionToAdd, user);
      outputTextArea.setText(currentUser + " offers " + actionToAdd + " action " +
          " to " + user);
    }
  }

  /**
   * DeleteActionFromUserListener is used to listen to action of
   * deleting an action to user.
   */
  private class DeleteActionFromUserListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String actionToDelete = (String) deleteActionActionComboBox.getSelectedItem();
      String user = (String) deleteActionUserComboBox.getSelectedItem();

      operator.deleteActionFromUser(actionToDelete, user);
      outputTextArea.setText(currentUser + " deletes " + actionToDelete + " action " +
          " from " + user);
    }
  }

  /**
   * Listener to activate when user click "Restart as new", "Exit",
   * "Logout" in EventFrame.
   */
  private class ThreeOptionsListener implements ActionListener {

    /**
     * The EventFrame
     */
    private EventFrame frame;
    /**
     * command to activate this ThreeOptionsListener
     */
    private String command;

    /**
     * Create a new LogoutListener with EventFrame frame.
     *
     * @param frame EventFrame
     * @param command command should be one of "Restart as new", "Exit" and "Logout"
     */
    private ThreeOptionsListener(EventFrame frame, String command) {
      this.frame = frame;
      this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      new ConfirmFrame(command, frame);
    }
  }

  /**
   * restart method is built for ConfirmFrame.
   * It is called when user click "Confirm" in
   * ConfirmFrame after clicking "Restart as new"in EventFrame.
   */
  void restart() {
    deleteAllFiles(DIR_NAME);
    System.exit(0);
  }

  private void deleteAllFiles(String dirName) {
    File dir = new File(dirName);
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        file.delete();
      }
    }
  }

  /**
   * exit method is built for ConfirmFrame.
   * It is called when user click "Confirm" in
   * ConfirmFrame after clicking "Exit"in EventFrame.
   */
  void exit() {
    try {
      EventFrame.writeStore(operator.getStore());
      System.exit(0);
    } catch (IOException exception) {
      System.err.println("An IOException is caught");
      exception.printStackTrace();
    }
  }

  // Helper for exit()

  /**
   * Serialize the only one store object and write it into a file with serFileName.
   *
   * @param store the singleton object store to be serialized
   * @throws IOException Exception
   */
  private static void writeStore(Store store) throws IOException {
    OutputStream file = new FileOutputStream(SER_FILE_NAME);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);
    output.writeObject(store);
    output.close();
  }

  /**
   * It is called when user click "Confirm" in
   * ConfirmFrame after clicking "Logout"in EventFrame.
   */
  void logout() {
    this.dispose();
    new LoginFrame(operator);
  }

  /**
   * Determine whether the string is invalid:
   * 1. equals to ""
   * 2. start with "Enter"
   *
   * @param string String to determine
   * @return whether it is invalid.
   */
  private boolean inValidString(String string) {
    return (string.equals("") || string.startsWith("Enter"));
  }

}
