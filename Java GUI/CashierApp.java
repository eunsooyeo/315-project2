import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter; // Import WindowAdapter
import java.awt.event.WindowEvent; // Import WindowEvent
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CashierApp extends JFrame {
    private JPanel leftPanel;
    private JPanel middlePanel;
    private JPanel rightPanel;
    private List<JButton> drinkButtons;
    private JPanel displayPanel;
    private List<String> selectedDrinks;
    private Order order;
    private ManagerFunctions managerFunctions;

    private double totalPrice;
    private double taxAmount;

    // Components for displaying price, tax, and total price
    private JLabel totalPriceLabel;
    private JLabel taxLabel;
    private JLabel totalAmountLabel;

    private JButton chargeButton;

    public CashierApp(ManagerFunctions m, Order o) {
        setTitle("Cashier Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        totalPrice = 0.0;

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the sidebar on the left with drink categories
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 1));

        String[] categories = {
                "Milk Tea", "Tea", "Fruit Tea", "Fresh Milk", "Ice Blended", "Tea Mojito", "Creama"
        };

        drinkButtons = new ArrayList<>();
        for (String category : categories) {
            JButton categoryButton = new JButton(category);
            categoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Update the middle panel to display specific drinks for the selected category
                    updateMiddlePanel(category);
                }
            });
            categoryButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryButton.setFocusPainted(false);
            categoryButton.setBorderPainted(false);
            categoryButton.setBackground(new Color(223, 227, 238)); // Light gray
            leftPanel.add(categoryButton);
            drinkButtons.add(categoryButton);
        }

        order = o;
        managerFunctions = m;

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogoutPopup(CashierApp.this, order, managerFunctions);
            }
        });
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setBackground(new Color(181, 184, 192)); // Dark gray
        leftPanel.add(logoutButton);

        // Create the middle panel to display specific drinks
        middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        middlePanel.setPreferredSize(new Dimension(300, 300)); // Smaller size

        // Create the right panel to display ordered drinks and buttons
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS)); // Vertical layout
        JScrollPane displayScrollPane = new JScrollPane(displayPanel);
        displayScrollPane.setPreferredSize(new Dimension(400, 200));

        chargeButton = new JButton("Charge: $0.00");
        JButton ticketsButton = new JButton("Print Ticket");

        // Make the buttons span the full width
        chargeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        chargeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!chargeButton.getText().equals("Charge: $0.00")) {
                    // Create the charge page and pass the total price
                    double totalAmount = totalPrice + taxAmount;
                    ChargePage chargePage = new ChargePage(CashierApp.this, totalAmount);

                    chargePage.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            chargePage.setChargeCanceled(true);
                        }
                    });

                    // Make the charge page visible
                    chargePage.setVisible(true);

                    Timer timer = new Timer(0, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(!chargePage.isChargeCanceled()) {
                                // clear dislpayed drinks
                                clearSelectedDrinks();
                            }
                        }
                    });

                    timer.setRepeats(false); // Only trigger once
                    timer.start();
                }
            }
        });

        totalPriceLabel = new JLabel("Total Price: $0.00");
        taxLabel = new JLabel("Tax: $0.00");
        totalAmountLabel = new JLabel("Total Amount: $0.00");

        // Add components to the rightPanel
        rightPanel.add(displayScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
        rightPanel.add(chargeButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        rightPanel.add(totalPriceLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        rightPanel.add(taxLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        rightPanel.add(totalAmountLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        // Initialize the list of selected drinks
        selectedDrinks = new ArrayList<>();

        // Add components to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void updateMiddlePanel(String category) {
        middlePanel.removeAll();
        for (JButton button : drinkButtons) {
            button.setEnabled(true);
        }

        // Implement code to display specific drinks for the selected category
        switch (category) {
            case "Milk Tea":
                displaySpecificDrink("Classic milk black tea");
                displaySpecificDrink("Honey milk tea");
                displaySpecificDrink("Coffee milk tea");
                displaySpecificDrink("Matcha red bean milk tea");
                break;
            case "Tea":
                displaySpecificDrink("Wintermelon tea");
                displaySpecificDrink("Classic black tea");
                break;
            case "Fruit Tea":
                displaySpecificDrink("Mango green tea");
                displaySpecificDrink("Mango & passion fruit tea");
                displaySpecificDrink("Hawaii fruit tea with aiyu jelly");
                displaySpecificDrink("Passion fruit, orange, and grapefruit tea");
                break;
            case "Fresh Milk":
                displaySpecificDrink("Wintermelon with fresh milk");
                displaySpecificDrink("Cocoa lover with fresh milk");
                break;
            case "Ice Blended":
                displaySpecificDrink("Milk tea ice blended with pearl");
                displaySpecificDrink("Taro ice blended with pudding");
                displaySpecificDrink("Strawberry ice blended with lychee jelly & ice cream");
                break;
            case "Tea Mojito":
                displaySpecificDrink("Lime mojito");
                displaySpecificDrink("Peach mojito");
                break;
            case "Creama":
                displaySpecificDrink("Coffee creama");
                displaySpecificDrink("Cocoa creama");
                break;
        }

        middlePanel.revalidate();
        middlePanel.repaint();
    }

    private void displaySpecificDrink(String drinkName) {
        JButton drinkButton = new JButton(drinkName);
        drinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a CustomizeDrinkPopup for the selected drink
                CustomizeDrinkPopup popup = new CustomizeDrinkPopup(CashierApp.this, drinkName, order);
                if (popup != null) {
                    // Wait for the pop-up to be closed and get the customized drink details
                    popup.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            if (popup.getSelectedIce() != null || popup.getSelectedSweetness() != null ||
                                    (!popup.getSelectedToppings().isEmpty())) {
                                // Create a customized drink string based on the selections
                                String customizedDrink = drinkName + "\n";
                                if (popup.getSelectedIce() != null) {
                                    customizedDrink += popup.getSelectedIce() + "\n";
                                }
                                if (popup.getSelectedSweetness() != null) {
                                    customizedDrink += popup.getSelectedSweetness() + "\n";
                                }
                                if (!popup.getSelectedToppings().isEmpty()) {
                                    customizedDrink += String.join(", ", popup.getSelectedToppings());
                                } else {
                                    customizedDrink += "No Toppings";
                                }
                                addSelectedDrink(customizedDrink);
                            }
                        }
                    });
                }
            }
        });
        middlePanel.add(drinkButton);
    }

    private void addSelectedDrink(String drinkName) {
        selectedDrinks.add(drinkName);
        updateTotalPrice();
        updateDisplayPanel();
    }

    private void removeSelectedDrink(String drinkName) {
        selectedDrinks.remove(drinkName);
        updateTotalPrice();
        updateDisplayPanel();
    }

    private void updateTotalPrice() {
        // TODO **************************************************************** connect
        // to DB and match prices
        // Calculate the total price based on the number of selected drinks
        double drinkPrice = 5.0; // Set the base price per drink - DELETE THIS LATER

        totalPrice = selectedDrinks.size() * drinkPrice;
        taxAmount = totalPrice * 0.0825;
        updateDisplayPanel();
    }

    private void updateDisplayPanel() {
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
        taxLabel.setText(String.format("Tax: $%.2f", taxAmount));
        double totalAmount = totalPrice + taxAmount;
        totalAmountLabel.setText(String.format("Total Amount: $%.2f", totalAmount));
        chargeButton.setText(String.format("Charge: $%.2f", totalAmount));

        displayPanel.removeAll();

        for (String drink : selectedDrinks) {
            String[] drinkLines = drink.split("\n");

            JPanel drinkPanel = new JPanel();
            drinkPanel.setLayout(new BoxLayout(drinkPanel, BoxLayout.Y_AXIS)); // Vertical layout

            for (String line : drinkLines) {
                JLabel lineLabel = new JLabel(line);
                drinkPanel.add(lineLabel);
            }

            // Create remove and edit buttons
            JButton removeButton = new JButton("Remove");
            JButton editButton = new JButton("Edit");

            // Add action listeners for remove and edit buttons
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //////////////////////////TODO: REMOVE DRINK FROM DATABASE //////////////////////////
                    order.restoreInventory(drink);
                    removeSelectedDrink(drink);
                }
            });

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Reopen the CustomizeDrinkPopup with the selected drink's name
                    String drinkName = drinkLines[0]; // Get the original drink name
                    CustomizeDrinkPopup popup = new CustomizeDrinkPopup(CashierApp.this, drinkName, order);
                    if (popup != null) {
                        popup.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                if (popup.getSelectedIce() != null || popup.getSelectedSweetness() != null ||
                                        (popup.getSelectedToppings() != null
                                                && !popup.getSelectedToppings().isEmpty())) {
                                    // Create a customized drink string based on the selections
                                    String customizedDrink = drinkName + "\n"; // TODO
                                                                               // *****************************************88
                                                                               // ADD PRICE NEXT TO NAME
                                    if (popup.getSelectedIce() != null) {
                                        customizedDrink += popup.getSelectedIce() + "\n";
                                    }
                                    if (popup.getSelectedSweetness() != null) {
                                        customizedDrink += popup.getSelectedSweetness() + "\n";
                                    }
                                    if (popup.getSelectedToppings() != null && !popup.getSelectedToppings().isEmpty()) {
                                        customizedDrink += String.join(", ", popup.getSelectedToppings());
                                    } else {
                                        customizedDrink += "No Toppings";
                                    }
                                    order.editInventory(drink, customizedDrink);
                                    updateSelectedDrink(drink, customizedDrink);
                                }
                            }
                        });
                    }
                }
            });

            // Add components to the drink panel
            drinkPanel.add(removeButton);
            drinkPanel.add(editButton);

            displayPanel.add(drinkPanel);
        }

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private void updateSelectedDrink(String oldDrink, String newDrink) {
        // Replace the old drink with the new drink in the selected drinks list
        int index = selectedDrinks.indexOf(oldDrink);
        if (index >= 0) {
            selectedDrinks.set(index, newDrink);
            updateDisplayPanel();
        }
    }

    private void clearSelectedDrinks() {
        selectedDrinks.clear(); // Clear the list of selected drinks
        totalPrice = 0.0; // Reset the total price to zero
        taxAmount = 0.0;
        updateDisplayPanel(); // Update the display panel to reflect the changes
    }

    private void clearSelectedDrinks() {
        selectedDrinks.clear(); // Clear the list of selected drinks
        totalPrice = 0.0; // Reset the total price to zero
        taxAmount = 0.0;
        updateDisplayPanel(); // Update the display panel to reflect the changes
    }

    /*
     * public static void main(String[] args) {
     * 
     * SwingUtilities.invokeLater(() -> {
     * CashierApp cashierApp = new CashierApp(order);
     * cashierApp.setVisible(true);
     * });
     * }
     */
}
