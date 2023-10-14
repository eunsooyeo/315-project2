import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OrderHistoryApp extends JPanel {
    private JTextField fromField;
    private JTextField toField;
    private JTextField monthField;
    private JTextField weekField;
    private JTextField dayField;
    private ManagerFunctions managerFunctions;
    private CardLayout cardLayout;
    private JPanel switchPanel;


    public OrderHistoryApp(ManagerFunctions m) {
        managerFunctions = m;

        setLayout(new BorderLayout());

        // Create a panel for the "From" and "To" fields at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel totalOrdersLabel = new JLabel("Total Orders: ");
        JLabel totalRevenuesLabel = new JLabel("Total Revenue: ");

        JLabel fromLabel = new JLabel("From (YYYY-MM-DD):");
        fromField = new JTextField("From", 10);
        fromField.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font size
        fromField.addFocusListener(new TextFieldFocusListener("From"));

        JLabel toLabel = new JLabel("To (YYYY-MM-DD):");
        toField = new JTextField("To", 10);
        toField.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font size
        toField.addFocusListener(new TextFieldFocusListener("To"));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromText = fromField.getText();
                String toText = toField.getText();
                // You can process and acquire the data from both fields here
                if(fromText.equals("From") || toText.equals("To")){
                    JOptionPane.showMessageDialog(OrderHistoryApp.this, "ERROR\n\nPlease Input Dates");
                    return;
                }
                String[] fromDates = fromText.split("/");
                String[] toDates = toText.split("/");
                totalOrdersLabel.setText("Total Orders: " + "TOTAL ORDERS BUTTON PRESSED");
            }
        });

        topPanel.add(fromLabel);
        topPanel.add(fromField);
        topPanel.add(toLabel);
        topPanel.add(toField);
        topPanel.add(submitButton); // Add the submit button

        add(topPanel, BorderLayout.NORTH);

        // Create a panel for switching between "Orders" and "Revenues"
        switchPanel = new JPanel();
        switchPanel.setBackground(new Color(220, 220, 220)); // Light gray color
        switchPanel.add(totalOrdersLabel);
        switchPanel.add(totalRevenuesLabel);
        
        add(switchPanel, BorderLayout.CENTER);

        

    }

    private class TextFieldFocusListener extends FocusAdapter {
        private String defaultText;

        public TextFieldFocusListener(String defaultText) {
            this.defaultText = defaultText;
        }

        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            if (source.getText().equals(defaultText)) {
                source.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            if (source.getText().isEmpty()) {
                source.setText(defaultText);
            }
        }
    }
}
