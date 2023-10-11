import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
                // Add the selected drink to the right-hand display
                addSelectedDrink(drinkName);
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
            JPanel drinkPanel = new JPanel();
            drinkPanel.setLayout(new BorderLayout());

            JLabel drinkLabel = new JLabel("Selected Drink: " + drink);
            drinkPanel.add(drinkLabel, BorderLayout.WEST);

            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeSelectedDrink(drink);
                }
            });
            drinkPanel.add(removeButton, BorderLayout.EAST);

            displayPanel.add(drinkPanel);
        }
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CashierApp cashierApp = new CashierApp();
            cashierApp.setVisible(true);
        });
    }
}
