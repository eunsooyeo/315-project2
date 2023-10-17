import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
Charge Page GUI for when customer is being charged
@author Kevin Tang
@author Dicong Wang
*/
public class ChargePage extends JDialog {
    private double totalPrice;
    private JButton cancelButton;

    private boolean chargeCanceled;
    /*
    @function Constructor to set up the GUI page
    @param parentFrame  to include reference to original frame of GUI
    @param totalPrice  to show the price on the GUI
    @return none
    @throws none
    */
    public ChargePage(JFrame parentFrame, double totalPrice) {
        super(parentFrame, "Charge Order", true); // Use modal dialog
        this.totalPrice = totalPrice;
        this.chargeCanceled = false;

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the centered section for the order summary
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display the total price
        JLabel priceLabel = new JLabel(String.format("$%.2f", totalPrice));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Display payment instructions
        JLabel paymentLabel = new JLabel("Tap, Insert, or Swipe to Pay");
        paymentLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Display contactless payment information
        JLabel contactlessLabel = new JLabel("Contactless Payment methods are enabled.");
        contactlessLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the charge page
                chargeCanceled = true;
                dispose();
            }
        });
        cancelButton.setFocusPainted(false);

        // Add components to the center panel
        centerPanel.add(priceLabel);
        centerPanel.add(paymentLabel);
        centerPanel.add(contactlessLabel);

        // Add the center panel and cancel button to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(cancelButton, BorderLayout.NORTH);

        Timer timer = new Timer(3000, new ActionListener() { // Timeout for 3s
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the charge page
                if(!chargeCanceled) {
                    openCompletionPopup(parentFrame); // Open the completion popup
                }
            }
        });

        timer.setRepeats(false); // Only trigger once
        timer.start();

        add(mainPanel);
        pack();
        setLocationRelativeTo(parentFrame); // Center the charge page on the CashierApp
    }
    /*
    @function Function to open the completion page when finished
    @param parentFrame to include reference to original frame in GUI
    @return none
    @throws none
    */
    private void openCompletionPopup(JFrame parentFrame) {
        CompletionPopup completionPopup = new CompletionPopup(parentFrame);
        completionPopup.setVisible(true);
    }
    /*
    @function Get function to return the cancel button
    @param none
    @return cancelButton button to cancel an order
    @throws none
    */
    public JButton getCancelButton() {
        return cancelButton;
    }
    /*
    @function Access function to return chargeCanceled variable
    @param none
    @return chargeCanceled boolean if order is canceled
    @throws none
    */
    public boolean isChargeCanceled() {
        return chargeCanceled;
    }
    /*
    @function Access function to set the boolean for chargeCanceled
    @param val to update chargeCanceled boolean
    @return none
    @throws none
    */
    public void setChargeCanceled(boolean val) {
        chargeCanceled = val;
    }
}