package frontEnd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This frame is used to input more detailed information about the
 * new scanned-in product. Then this new product will be added to
 * the store inventory.
 *
 * @author Haolin Zhang, Make Zhang, Ziyue Xu
 */
class ScanInNewFrame extends JFrame {

  /**
   * The previous unclosed EventFrame.
   */
  private EventFrame frame;

  /**
   * Event controller with today's store.
   */
  private EventOperator operator;

  /**
   * New product's upc received from previous EventFrame.
   */
  private String upc;

  /**
   * New product's quantity received from previous EventFrame.
   */
  private int quantity;

  /**
   * Input text field for new product's name.
   */
  private JTextField nameTextField;

  /**
   * Input text field for new product's cost.
   */
  private JTextField costTextField;

  /**
   * Input text field for new product's price.
   */
  private JTextField priceTextField;

  /**
   * Selection list of all available sections.
   */
  private JComboBox<String> superSectionComboBox;

  /**
   * Input text field for new product's threshold.
   */
  private JTextField thresholdTextField;

  /**
   * Selection list of all available aisles.
   */
  private JComboBox<String> aisleComboBox;

  /**
   * Input text field for new product's distributor.
   */
  private JTextField distributorTextField;

  /**
   * Allocates a new ScanInNewFrame which is designed for receiving more
   * detailed information of new scanned-in product and add this product
   * to the store inventory.
   *
   * @param upc the upc of new product.
   * @param quantity the quantity of new product.
   * @param frame the previous unclosed EventFrame.
   * @param operator the event controller with today's store.
   */
  ScanInNewFrame(String upc, int quantity, EventFrame frame,
      EventOperator operator) {
    this.upc = upc;
    this.quantity = quantity;
    this.frame = frame;
    this.operator = operator;
    String lineHolder = "           ";

    this.setSize(350, 350);
    this.setLocationRelativeTo(null);
    this.setTitle("Scan in new product");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 0, 0, 0);
    this.add(panel);

    //name
    JLabel nameLabel = new JLabel("Product name:");
    gbc.anchor = GridBagConstraints.LINE_END;
    panel.add(nameLabel, gbc);
    gbc.gridx++;
    nameTextField = new JTextField("", 10);
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(nameTextField, gbc);

    //cost
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel costLabel = new JLabel("Cost:");
    panel.add(costLabel, gbc);
    costTextField = new JTextField("", 10);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(costTextField, gbc);

    //price
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel priceLabel = new JLabel("Price:");
    panel.add(priceLabel, gbc);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    priceTextField = new JTextField("", 10);
    panel.add(priceTextField, gbc);

    //superSection
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel superSectionLabel = new JLabel("Super section:");
    panel.add(superSectionLabel, gbc);
    superSectionComboBox = new JComboBox<>();
    ArrayList<String> sectionNameList = operator.getLeafSectionNameList();
    for (String sectionName : sectionNameList) {
      superSectionComboBox.addItem(sectionName);
    }
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(superSectionComboBox, gbc);

    //threshold
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel thresholdLabel = new JLabel("Threshold:");
    panel.add(thresholdLabel, gbc);
    thresholdTextField = new JTextField("", 10);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(thresholdTextField, gbc);

    //aisle
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel aisleLabel = new JLabel("Aisle:");
    panel.add(aisleLabel, gbc);
    aisleComboBox = new JComboBox<>();
    ArrayList<String> aisleNameList = operator.getAisleNameList();
    for (String aisleName : aisleNameList) {
      aisleComboBox.addItem(aisleName);
    }
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(aisleComboBox, gbc);

    //distributor
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.LINE_END;
    JLabel distributorLabel = new JLabel("Distributor:");
    panel.add(distributorLabel, gbc);
    distributorTextField = new JTextField("", 10);
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(distributorTextField, gbc);

    //Empty Line
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel(lineHolder), gbc);

    //Confirm
    gbc.gridx = 1;
    gbc.gridy++;
    JButton scanInButton = new JButton("Scan-in");
    gbc.anchor = GridBagConstraints.LINE_START;
    panel.add(scanInButton, gbc);
    ScanInListener scanInListener = new ScanInListener(this, frame);
    scanInButton.addActionListener(scanInListener);

    this.setVisible(true);
  }

  /**
   * An ActionListener which receives all information from each text field and
   * pass it to event controller to commit action.
   *
   * @author Haolin Zhang, Ziyue Xu
   */
  private class ScanInListener implements ActionListener {

    /**
     * The current unclosed ScanInNewFrame.
     */
    private JFrame currentFrame;

    /**
     * The super EventFrame where this ScanInNewFrame comes from.
     */
    private EventFrame eventFrame;

    /**
     * Allocates a new ScanInListener which asks event controller to commit action.
     *
     * @param currentFrame the current unclosed ScanInNewFrame.
     */
    ScanInListener(JFrame currentFrame, EventFrame eventFrame) {
      this.currentFrame = currentFrame;
      this.eventFrame = eventFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (nameTextField.getText().equals("") || costTextField.getText().equals("") ||
          priceTextField.getText().equals("") || thresholdTextField.getText().equals("") ||
          distributorTextField.getText().equals("")) {
        eventFrame.getOutputTextArea().setText("Please enter all required information");
      } else {
        try {
          frame.getOutputTextArea().setText(operator.scanInNew(quantity,
              upc, nameTextField.getText(), Double.parseDouble(costTextField.getText()),
              Double.parseDouble(priceTextField.getText()),
              (String) superSectionComboBox.getSelectedItem(),
              Integer.parseInt(thresholdTextField.getText()),
              (String) aisleComboBox.getSelectedItem(), distributorTextField.getText()));
          currentFrame.dispose();
        } catch (Exception notRightType) {
          eventFrame.getOutputTextArea().setText("Cost and Price should be double"
              + " and Threshold should be integer");
        }
      }
    }
  }
}
