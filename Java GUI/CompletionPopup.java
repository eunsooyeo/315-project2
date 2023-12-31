import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** 
Completion Popup for GUI to show final screen after order placed
@author Kevin Tang
@author Dicong Wang
*/
public class CompletionPopup extends JDialog {
    /** 
    Constructor to set up GUI for page
    @param parentFrame  to reference original frame of GUI
    */
    public CompletionPopup(JFrame parentFrame) {
        super(parentFrame, "Order Completed", true);

        // Create the content of the completion popup
        JPanel content = new JPanel(new BorderLayout());

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>Transaction completed.<br>Order approved.</div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a button to start a new sale
        JButton newSaleButton = new JButton("New Sale");
        newSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the completion popup
                dispose();
            }
        });

        // Add components to the completion popup
        content.add(messageLabel, BorderLayout.CENTER);
        content.add(newSaleButton, BorderLayout.SOUTH);

        setContentPane(content);

        // Set size, position, and other properties as needed
        setSize(300, 150);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
