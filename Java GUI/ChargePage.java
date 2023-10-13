import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChargePage extends JDialog {
    private double totalPrice;
    private JButton cancelButton;

    private boolean chargeCanceled;

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

    private void openCompletionPopup(JFrame parentFrame) {
        CompletionPopup completionPopup = new CompletionPopup(parentFrame);
        completionPopup.setVisible(true);
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public boolean isChargeCanceled() {
        return chargeCanceled;
    }

    public void setChargeCanceled(boolean val) {
        chargeCanceled = val;
    }
}