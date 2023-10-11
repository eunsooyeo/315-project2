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
                    // Update the middle panel to display drinks for the selected category
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

        // Create the middle panel to display drinks
        middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create the right panel to display ordered drinks and buttons
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        displayPanel = new JPanel();
        displayPanel.setPreferredSize(new Dimension(200, 200));
        displayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton chargeButton = new JButton("Charge");
        JButton ticketsButton = new JButton("Tickets");

        rightPanel.add(displayPanel);
        rightPanel.add(chargeButton);
        rightPanel.add(ticketsButton);

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

        switch (category) {
            case "Milk Tea":
                addDrink("Classic milk black tea");
                addDrink("Honey milk tea");
                addDrink("Coffee milk tea");
                addDrink("Matcha red bean milk tea");
                break;
            case "Tea":
                addDrink("Wintermelon tea");
                addDrink("Classic black tea");
                break;
            case "Fruit Tea":
                addDrink("Mango green tea");
                addDrink("Mango & passion fruit tea");
                addDrink("Hawaii fruit tea with aiyu jelly");
                addDrink("Passion fruit, orange, and grapefruit tea");
                break;
            case "Fresh Milk":
                addDrink("Wintermelon with fresh milk");
                addDrink("Cocoa lover with fresh milk");
                break;
            case "Ice Blended":
                addDrink("Milk tea ice blended with pearl");
                addDrink("Taro ice blended with pudding");
                addDrink("Strawberry ice blended with lychee jelly & ice cream");
                break;
            case "Tea Mojito":
                addDrink("Lime mojito");
                addDrink("Peach mojito");
                break;
            case "Creama":
                addDrink("Coffee creama");
                addDrink("Cocoa creama");
                break;
        }

        middlePanel.revalidate();
        middlePanel.repaint();
    }

    private void addDrink(String drinkName) {
        JButton drinkButton = new JButton(drinkName);
        drinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display the selected drink in the display panel
                displayDrink(drinkName);
            }
        });
        middlePanel.add(drinkButton);
    }

    private void displayDrink(String drinkName) {
        // Implement the logic to display the selected drink in the display panel
        displayPanel.removeAll();
        JLabel drinkLabel = new JLabel("Selected Drink: " + drinkName);
        displayPanel.add(drinkLabel);
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