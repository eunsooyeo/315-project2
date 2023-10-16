import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.border.Border;

public class OrderHistoryApp extends JPanel {
    private JTextField fromField;
    private JTextField toField;
    private JTextField monthField;
    private JTextField weekField;
    private JTextField dayField;
    private ManagerFunctions managerFunctions;
    private CardLayout cardLayout;
    private JPanel switchPanel;
    private JPanel drinksPanel; // New panel for drink labels
    private JLabel totalOrdersLabel;
    private JLabel totalRevenuesLabel;

    public OrderHistoryApp(ManagerFunctions m) {
        managerFunctions = m;

        setLayout(new BorderLayout());

        // Create a panel for the "From" and "To" fields at the top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30)); // Add padding

        JPanel dateFieldsPanel = new JPanel();
        dateFieldsPanel.setLayout(new BoxLayout(dateFieldsPanel, BoxLayout.X_AXIS));

        totalOrdersLabel = new JLabel("Total Orders: ");
        totalRevenuesLabel = new JLabel("Total Revenue: ");

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

                displayOrders(fromText, toText);
            }
        });

        dateFieldsPanel.add(fromLabel);
        dateFieldsPanel.add(fromField);
        dateFieldsPanel.add(toLabel);
        dateFieldsPanel.add(toField);
        dateFieldsPanel.add(submitButton);

        topPanel.add(dateFieldsPanel, BorderLayout.NORTH);

        JPanel totalLabelsPanel = new JPanel();
        totalLabelsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        totalLabelsPanel.add(totalOrdersLabel);
        totalLabelsPanel.add(totalRevenuesLabel);

        topPanel.add(totalLabelsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        drinksPanel = new JPanel(new GridLayout(0, 3));
        drinksPanel.setBackground(new Color(207, 209, 212)); // Background color
        drinksPanel.setBorder(BorderFactory.createEmptyBorder(7, 30, 7, 30)); // Padding

        JPanel switchPanel = new JPanel(new BorderLayout());
        switchPanel.add(drinksPanel, BorderLayout.CENTER);

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

    public void displayOrders(String fromDate, String toDate) {
        drinksPanel.removeAll();

        ArrayList<ArrayList<String>> sales = managerFunctions.getFilteredSalesHistory(fromDate, toDate);
        int totalOrders = 0;
        double totalRevenue = 0.0;

        for(int i = 0; i < sales.size(); i++) {
            //String orderData = sales.get(i).get(0) + "    Total: " + sales.get(i).get(1) + "    Revenue:$" + sales.get(i).get(2);
            String orderNameText = "  " + sales.get(i).get(0);
            String orderTotalText = "Total: " + sales.get(i).get(1);
            String orderRevenueText = "Revenue: $" + sales.get(i).get(2);

            JLabel orderName = new JLabel(orderNameText);
            JLabel orderTotal = new JLabel(orderTotalText);
            JLabel orderRevenue = new JLabel(orderRevenueText);
            
            Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(207, 209, 212));

            orderName.setBorder(bottomBorder);
            orderTotal.setBorder(bottomBorder);
            orderRevenue.setBorder(bottomBorder);

            // Set a lighter background color for each label
            orderName.setBackground(new Color(240, 240, 240));
            orderTotal.setBackground(new Color(240, 240, 240));
            orderRevenue.setBackground(new Color(240, 240, 240));

            orderName.setOpaque(true);
            orderTotal.setOpaque(true);
            orderRevenue.setOpaque(true);

            drinksPanel.add(orderName);
            drinksPanel.add(orderTotal);
            drinksPanel.add(orderRevenue);

            totalOrders += Integer.parseInt(sales.get(i).get(1));
            totalRevenue += Double.parseDouble(sales.get(i).get(2));
        }

        totalOrdersLabel.setText("Total Orders: " + totalOrders);
        totalRevenuesLabel.setText("Total Revenue: $" + totalRevenue);

        drinksPanel.revalidate();
        drinksPanel.repaint();
    }
}
