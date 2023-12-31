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
/** 
GUI design for cashier interface
@author Kevin Tang
@author Dicong Wang
*/
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

    public static Connection conn = null;

    /** 
    *Constructor to layout cashier page
    @param m to include managerfunctions
    @param o order to include the order functions

    */
    public CashierApp(ManagerFunctions m, Order o) {
        
        // order.assign(o);
        order = o;
        managerFunctions = m;

        setTitle("Cashier Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        totalPrice = 0.0;

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        int rows = 8;
        boolean extraDrinks = managerFunctions.getNumberOfDrinks() > 19;
        if(extraDrinks) rows = 9; 

        // Create the sidebar on the left with drink categories
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(rows, 1));

        List<String> categories = new ArrayList<>();
        categories.add("Milk Tea");
        categories.add("Tea");
        categories.add("Fruit Tea");
        categories.add("Fresh Milk");
        categories.add("Ice Blended");
        categories.add("Tea Mojito");
        categories.add("Creama");

        if (extraDrinks) {
            categories.add("Seasonal");
        }

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
                if (!chargeButton.getText().equals("Charge: $0.00")) {
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
                            if (!chargePage.isChargeCanceled()) {
                                //update order history
                                order.makeOrder(selectedDrinks, totalAmount);
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

        String teamName = "10r";
        String dbName = "csce315331_" + teamName + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        // connect to database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /** 
    Updates the middle panel for which category of drinks is selected
    @param category string of the selected drink category

    */
    private void updateMiddlePanel(String category) {
        middlePanel.removeAll();
        for (JButton button : drinkButtons) {
            button.setEnabled(true);
        }

        ArrayList<String> extraDrinksArr = managerFunctions.getAllSortedDrinks();

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
            case "Seasonal":
                for(int i = 19; i < extraDrinksArr.size(); i++) {
                    displaySpecificDrink(extraDrinksArr.get(i));
                }
                break;
        }

        middlePanel.revalidate();
        middlePanel.repaint();
    }

    /** 
    Function to set up display popup of drink selection
    @param drinkName to show which drink was selected

    */
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

    /** 
    Function that adds the selected drink information to the right side panel and updates the price
    @param drinkName string of added drink

    */
    private void addSelectedDrink(String drinkName) {
        selectedDrinks.add(drinkName);
        updateTotalPrice();
        updateDisplayPanel();
    }
    /** 
    Function that removes the selected drink information from the right side panel and updates the price
    @param drinkName string of drink

    */
    private void removeSelectedDrink(String drinkName) {
        selectedDrinks.remove(drinkName);
        updateTotalPrice();
        updateDisplayPanel();
    }
    /** 
    Function that updates total price using information from database and toppings 
    @throws Errors from accessing database

    */
    private void updateTotalPrice() {
        // Calculate the total price based on the number of selected drinks
        double drinksPrice = 0.0;
        List<String> tempList = new ArrayList<>();

        try {
            for (String drink : selectedDrinks) {
                String[] lines = drink.split("\n");
                String name = lines[0];

                if(lines[0].contains("$")) {
                    name = lines[0].substring(0, lines[0].indexOf("$"));
                    name = name.substring(0, name.length() - 1);
                }

                double price;
                String queryString = "SELECT price FROM recipes WHERE lower(drinkname) = '" + name.toLowerCase()
                        + "';";
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(queryString);
                result.next();
                price = result.getDouble(1);

                String[] tempToppings = lines[lines.length - 1].split(", ");
                double extra = 0;

                if(!tempToppings[0].trim().toLowerCase().equals("no toppings")) {
                    extra += tempToppings.length * 0.5;
                }

                drinksPrice += price;
                drinksPrice += extra;

                String updatedDrink;
            
                if(extra != 0) updatedDrink = name + " $" + price + " + $" + extra + "\n" + drink.substring(drink.indexOf('\n') + 1);
                else updatedDrink = name + " $" + price + "\n" + drink.substring(drink.indexOf('\n') + 1);

                tempList.add(updatedDrink);
            }
        } catch (Exception e) {
            System.out.println("error updating total price");
        }

        if(!tempList.isEmpty()) selectedDrinks = tempList;

        totalPrice = drinksPrice;
        taxAmount = drinksPrice * 0.0825;
        updateDisplayPanel();
    }

    /** 
    Function that updates the right panel to include tax and total prices and allow for edit and removal of drinks

    */
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
                    ////////////////////////// TODO: REMOVE DRINK FROM DATABASE
                    ////////////////////////// //////////////////////////
                    order.restoreInventory(drink);
                    removeSelectedDrink(drink);
                }
            });

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Reopen the CustomizeDrinkPopup with the selected drink's name
                    String drinkName = drinkLines[0]; // Get the original drink name
                    order.restoreInventory(drink);
                    CustomizeDrinkPopup popup = new CustomizeDrinkPopup(CashierApp.this, drinkName, order);
                    if (popup != null) {
                        popup.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                if (popup.getSelectedIce() != null || popup.getSelectedSweetness() != null ||
                                        (popup.getSelectedToppings() != null
                                                && !popup.getSelectedToppings().isEmpty())) {
                                    // Create a customized drink string based on the selections
                                    String customizedDrink = drinkName + "\n";
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
                                    updateSelectedDrink(drink, customizedDrink);
                                    updateTotalPrice();
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
    /** 
    Function that updates the selected drink information to the right side panel and updates the price
    @param oldDrink string of the original drink
    @param newDrink string of new drink selected

    */
    private void updateSelectedDrink(String oldDrink, String newDrink) {
        // Replace the old drink with the new drink in the selected drinks list
        int index = selectedDrinks.indexOf(oldDrink);
        if (index >= 0) {
            selectedDrinks.set(index, newDrink);
            updateDisplayPanel();
        }
    }
    /** 
    Function that clears the right panel and resets price to zero
    
    */
    private void clearSelectedDrinks() {
        selectedDrinks.clear(); // Clear the list of selected drinks
        totalPrice = 0.0; // Reset the total price to zero
        taxAmount = 0.0;
        updateDisplayPanel(); // Update the display panel to reflect the changes
    }
}
