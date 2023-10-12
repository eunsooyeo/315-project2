import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;  // Import WindowAdapter
import java.awt.event.WindowEvent;    // Import WindowEvent
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

    public CashierApp() {
        setTitle("Cashier Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the sidebar on the left with drink categories
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(7, 1));

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

        JButton chargeButton = new JButton("Charge");
        JButton ticketsButton = new JButton("Tickets");

        // Make the buttons span the full width
        chargeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(displayScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
        rightPanel.add(chargeButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        rightPanel.add(ticketsButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

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
                CustomizeDrinkPopup popup = new CustomizeDrinkPopup(CashierApp.this, drinkName);
                if (popup != null) {
                    // Wait for the pop-up to be closed and get the customized drink details
                    popup.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            if (popup.getSelectedIce() != null || popup.getSelectedSweetness() != null ||
                                    popup.getSelectedToppings() != null) {
                                // Create a customized drink based on the selections and add it to the right-hand display
                                String customizedDrink = drinkName + " - " +
                                        (popup.getSelectedIce() != null ? popup.getSelectedIce() + " " : "") +
                                        (popup.getSelectedSweetness() != null ? popup.getSelectedSweetness() + " " : "") +
                                        (popup.getSelectedToppings() != null ? String.join(", ", popup.getSelectedToppings()) : "");
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
        updateDisplayPanel();
    }

    private void removeSelectedDrink(String drinkName) {
        selectedDrinks.remove(drinkName);
        updateDisplayPanel();
    }

    private void updateDisplayPanel() {
        displayPanel.removeAll();

        for (String drink : selectedDrinks) {
            // Split the drink string to separate the name and options
            String[] drinkParts = drink.split(" - ");
            
            JPanel drinkPanel = new JPanel();
            drinkPanel.setLayout(new BoxLayout(drinkPanel, BoxLayout.Y_AXIS)); // Vertical layout
            
            // Create a label for the name
            JLabel nameLabel = new JLabel(drinkParts[0]);
            drinkPanel.add(nameLabel);
            
            if (drinkParts.length > 1) {
                String options = drinkParts[1];
                String[] optionsArray = options.split(", ");
                
                for (String option : optionsArray) {
                    // Handle null toppings
                    if (!option.equals("null")) {
                        JLabel optionLabel = new JLabel(option);
                        drinkPanel.add(optionLabel);
                    }
                }
                
                // Handle the case where all toppings are null
                if (optionsArray.length == 1 && optionsArray[0].equals("null")) {
                    JLabel noToppingsLabel = new JLabel("No Toppings");
                    drinkPanel.add(noToppingsLabel);
                }
            } else {
                JLabel noToppingsLabel = new JLabel("No Toppings");
                drinkPanel.add(noToppingsLabel);
            }
            
            // Create remove and edit buttons
            JButton removeButton = new JButton("Remove");
            JButton editButton = new JButton("Edit");
            
            // Add action listeners for remove and edit buttons
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeSelectedDrink(drink);
                }
            });
            
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Reopen the CustomizeDrinkPopup with the selected drink's name
                    CustomizeDrinkPopup popup = new CustomizeDrinkPopup(CashierApp.this, drinkParts[0]);
                    if (popup != null) {
                        popup.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                if (popup.getSelectedIce() != null || popup.getSelectedSweetness() != null ||
                                        popup.getSelectedToppings() != null) {
                                    // Create a customized drink based on the selections and update the right-hand display
                                    String customizedDrink = drinkParts[0] + " - ";
                                    if (popup.getSelectedIce() != null) {
                                        customizedDrink += popup.getSelectedIce() + "\n";
                                    }
                                    if (popup.getSelectedSweetness() != null) {
                                        customizedDrink += popup.getSelectedSweetness() + "\n";
                                    }
                                    if (popup.getSelectedToppings() != null) {
                                        if (popup.getSelectedToppings() != null && popup.getSelectedToppings().length > 0) {
                                            customizedDrink += String.join(", ", popup.getSelectedToppings());
                                        } else {
                                            customizedDrink += "No Toppings";
                                        }
                                    }
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

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            CashierApp cashierApp = new CashierApp();
            cashierApp.setVisible(true);
        });
    }
}
