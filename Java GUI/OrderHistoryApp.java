import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.border.Border;

/** 
Menus page on the manager side
@author Kevin Tang
@author Dicong Wang
*/
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

    /** 
    @function Constructor to set up the GUI page
    @param m to allow usage of managerFunctions
    @throws none
    */
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

                if(fromText.equals("From") || toText.equals("To")){
                    JOptionPane.showMessageDialog(OrderHistoryApp.this, "ERROR\n\nPlease Input Dates");
                    return;
                }

                if(invalidDate(fromText) || invalidDate(toText)){
                    JOptionPane.showMessageDialog(OrderHistoryApp.this, "ERROR\n\nInvalid Dates");
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

    /** 
    Class to setup the textfield to either default or inputted
    */
    private class TextFieldFocusListener extends FocusAdapter {
        private String defaultText;
        /** 
        @function Constructor to set up TextFieldFocusListener class
        @param defaultText string to set the default
        @return void
        @throws none
        */
        public TextFieldFocusListener(String defaultText) {
            this.defaultText = defaultText;
        }
        /** 
        @function focusGained shows redirecton to the focusevent
        @param e focusevent for setting the source of text
        @return void
        @throws none
        */
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            if (source.getText().equals(defaultText)) {
                source.setText("");
            }
        }
        /** 
        @function focusLost shows if the focusevent text is empty
        @param e focusevent for setting the source of text
        @return void
        @throws none
        */
        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            if (source.getText().isEmpty()) {
                source.setText(defaultText);
            }
        }
    }

    /** 
    @function display the order information from start to end date, including total, revenues
    @param fromDate string of start date
    @param toDate string of end date
    @return void
    @throws none
    */
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

    /** 
    @function invaliddate boolean returns true if the date is not valid
    @param date string of entered date
    @return boolean for validity of date
    @throws none
    */
    private boolean invalidDate(String date) {
        if(date.length() != 10) return true;
        for(int i = 0; i < date.length(); i++) {
            if(i == 4 || i == 7) {
                if(date.charAt(i) != '-' && date.charAt(i) != '/') return true;
            }
            else {
                if(!Character.isDigit(date.charAt(i))) return true;
            }
        }

        return false;
    }
}
