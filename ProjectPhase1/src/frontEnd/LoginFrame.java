package frontEnd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * This frame is used to login a user.
 *
 * @author Haolin Zhang, Make Zhang, Ziyue Xu
 */
class LoginFrame extends JFrame {

  /**
   * Input text field for user name.
   */
  private JTextField loginTextField;

  /**
   * Label for warning invalid user name input.
   */
  private JLabel invalidInputMessage;

  /**
   * Event controller with today's store.
   */
  private EventOperator operator;

  /**
   * Constant blank line holder string.
   */
  private static final String lineHolder = "            ";

  /**
   * Allocates a new LoginFrame which is used to login a user.
   *
   * @param operator event controller with today's store.
   */
  LoginFrame(EventOperator operator) {

    this.operator = operator;
    this.setSize(600, 300);
    this.setLocationRelativeTo(null);
    this.setTitle("Login Screen");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JPanel panel = new JPanel(new GridBagLayout());
    this.add(panel);
    GridBagConstraints gridLayout = new GridBagConstraints();
    gridLayout.gridx = 0;
    gridLayout.gridy = 0;
    gridLayout.anchor = GridBagConstraints.LINE_END;

    //login input
    panel.add(new JLabel("Enter your position: "), gridLayout);
    gridLayout.gridx ++;
    gridLayout.anchor = GridBagConstraints.LINE_START;
    loginTextField = new JTextField(15);
    panel.add(loginTextField, gridLayout);
    gridLayout.gridy ++;
    gridLayout.anchor = GridBagConstraints.LINE_START;

    //waning message
    invalidInputMessage = new JLabel(lineHolder);
    invalidInputMessage.setVisible(true);
    panel.add(invalidInputMessage, gridLayout);

    //login button
    gridLayout.gridy ++;
    gridLayout.anchor = GridBagConstraints.LINE_START;
    JButton loginButton = new JButton("Login");
    panel.add(loginButton, gridLayout);
    LoginListener loginListener = new LoginListener(this);
    loginButton.addActionListener(loginListener);

    this.setVisible(true);
  }

  /**
   * An ActionListener which commits action when login button was clicked.
   *
   * @author Haolin Zhang
   */
  private class LoginListener implements ActionListener {

    /**
     * The current unclosed LoginFrame.
     */
    private JFrame frame;

    /**
     * Allocates a new LoginListener which commits action when
     * login button was clicked.
     *
     * @param frame the current unclosed LoginFrame.
     */
    LoginListener(JFrame frame) {
      this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      String loginUser = loginTextField.getText();
      if (operator.whetherLegalUser(loginUser)) {
        frame.dispose();
        new EventFrame(operator, loginUser);
      } else {
        invalidInputMessage.setText("Invalid user! Please enter again.");
      }
    }
  }
}
