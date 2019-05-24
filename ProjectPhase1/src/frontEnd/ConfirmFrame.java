package frontEnd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This frame is used to confirm user's choice of restart, exit and logout,
 * avoiding user's accident click.
 * Plus, lower the chance of making mistake for the new users.
 *
 * @author Ziyue Xu
 */
class ConfirmFrame extends JFrame {

  /**
   * The frame triggering this Confirm frame.
   */
  private EventFrame sourceFrame;

  /**
   * An empty line to hold space.
   */
  private static final String LINE_HOLDER = "            ";

  /**
   * Create a new ConfirmFrame. It is triggered by frame and is
   * created to handle event.
   *
   * @param event The type of event need to be handled by this frame.
   * @param frame The frame that triggered this ConfirmFrame.
   */
  ConfirmFrame(String event, EventFrame frame) {
    this.sourceFrame = frame;
    // Create tha new ConfirmFrame.
    this.setSize(450, 300);
    this.setLocationRelativeTo(null);
    this.setTitle("Please Confirm");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Add panel and set GridBagLayout.
    JPanel panel = new JPanel(new GridBagLayout());
    this.add(panel);
    GridBagConstraints gridLayout = new GridBagConstraints();
    gridLayout.gridx = 0;
    gridLayout.gridy = 0;
    gridLayout.anchor = GridBagConstraints.CENTER;

    JLabel messageToConfirm = new JLabel("");
    // Reset text on this Label according to event.
    switch (event) {
      case "Restart as new":
        messageToConfirm.setText("WARNING: THIS WILL RESET ALL SETTINGS!");
        break;
      case "Exit":
        messageToConfirm.setText("WARNING: THIS WILL END TODAY!");
        break;
      case "Logout":
        break;
      default:
        break;
    }
    panel.add(messageToConfirm, gridLayout);

    gridLayout.gridy++;
    panel.add(new JLabel(String.format("Are you sure you want to %s ?", event)), gridLayout);

    gridLayout.gridx = 0;
    gridLayout.gridy++;
    panel.add(new JLabel(LINE_HOLDER), gridLayout);

    gridLayout.gridy++;
    // Add button "Confirm" at the left part of the frame.
    gridLayout.anchor = GridBagConstraints.LINE_START;
    JButton confirmButton = new JButton("Confirm");
    confirmButton.addActionListener(new ConfirmListener(this, event));
    panel.add(confirmButton, gridLayout);

    // Add button "Cancel" at the right part of the frame.
    gridLayout.anchor = GridBagConstraints.LINE_END;
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new CancelListener(this));
    panel.add(cancelButton, gridLayout);

    this.setVisible(true);
  }

  /**
   * ConfirmListener is used to handle confirm action of user.
   * This listener handle three different
   * conditions: "Restart as new", "Exit", "Logout",
   * if user click "confirm"
   */
  private class ConfirmListener implements ActionListener {

    /**
     * The ConfirmFrame which this listener listen to.
     */
    private JFrame confirmFrame;

    /**
     * message represented the command to confirm.
     */
    private String message;

    /**
     * Create a new ConfirmListener with ConfirmFrame frame and message m.
     * The message represent the command to confirm.
     *
     * @param frame The ConfirmFrame this listener listen to.
     * @param m The command to confirm.
     */
    private ConfirmListener(JFrame frame, String m) {
      this.confirmFrame = frame;
      this.message = m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.confirmFrame.dispose();
      switch (message) {
        case "Restart as new":
          sourceFrame.restart();
          break;
        case "Exit":
          sourceFrame.exit();
          break;
        case "Logout":
          sourceFrame.logout();
          break;
        default:
          break;
      }
    }
  }

  /**
   * CancelListener is used to handle cancel action of user.
   * if user click "Cancel", this ConfirmFrame will disappear.
   */
  private class CancelListener implements ActionListener {

    /**
     * The ConfirmFrame which this listener listen to.
     */
    private JFrame frame;

    /**
     * Create a new ConfirmListener with ConfirmFrame frame.
     * This will cancel all commands.
     *
     * @param frame The ConfirmFrame this listener listen to.
     */
    CancelListener(JFrame frame) {
      this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      frame.dispose();
    }
  }

}
