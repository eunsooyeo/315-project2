import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenusApp extends JPanel {
    private JTextArea drinkDetailsTextArea;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField ingredientsField;
    private JButton editDrinkButton;
    private JButton addDrinkButton;
    private JButton removeDrinkButton;
    private JButton[] drinkButtons;
    private String[] drinkNames;
    private String[] drinkPrices;
    private String[] drinkIngredients;
    private int selectedDrink;
    private JPanel centerPanel;

    public MenusApp() {
        setLayout(new BorderLayout());

        int numDrinks = 20; // Specify the number of initial drinks

        // Initialize arrays to store drink data
        drinkNames = new String[numDrinks];
        drinkPrices = new String[numDrinks];
        drinkIngredients = new String[numDrinks];
        for (int i = 0; i < numDrinks; i++) {
            drinkNames[i] = "Drink " + (i + 1); // Initialize drink names
            drinkPrices[i] = "0.00"; // Initialize drink prices
            drinkIngredients[i] = "Unknown Ingredients"; // Initialize drink ingredients
        }

        // Create the center panel for drink buttons
        centerPanel = new JPanel();

        // Create a scroll pane for the drink buttons panel
        JScrollPane scrollPane = new JScrollPane(centerPanel);

        // Create a grid layout for the drink buttons with 5 columns
        int maxColumns = 5;
        centerPanel.setLayout(new GridLayout(0, maxColumns)); // 0 rows, maxColumns columns

        drinkButtons = new JButton[numDrinks];
        for (int i = 0; i < numDrinks; i++) {
            drinkButtons[i] = new JButton(drinkNames[i]);
            
            // Increase the font size for the buttons
            Font buttonFont = new Font(drinkButtons[i].getFont().getName(), Font.PLAIN, 36);
            drinkButtons[i].setFont(buttonFont);

            final int drinkNumber = i; // Capture the current drink number
            drinkButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDrink = drinkNumber;
                    nameField.setText(drinkNames[selectedDrink]); // Update the name field
                    priceField.setText(drinkPrices[selectedDrink]); // Update the price field
                    ingredientsField.setText(drinkIngredients[selectedDrink]); // Update the ingredients field
                    updateDisplay();
                }
            });

            centerPanel.add(drinkButtons[i]);
        }

        add(scrollPane, BorderLayout.WEST); // Place the scrollable panel on the left

        // Create the panel for drink information and editing
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        // Add labels and fields for editing
        nameField = new JTextField(20);
        priceField = new JTextField(20);
        ingredientsField = new JTextField(20);
        editDrinkButton = new JButton("Edit Drink");
        addDrinkButton = new JButton("Add Drink");
        removeDrinkButton = new JButton("Remove Drink");

        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Ingredients:"));
        editPanel.add(ingredientsField);
        editPanel.add(editDrinkButton);
        editPanel.add(addDrinkButton);
        editPanel.add(removeDrinkButton);

        // Add the panel to the right side
        rightPanel.add(editPanel, BorderLayout.NORTH);

        // Create the text area for displaying drink details
        drinkDetailsTextArea = new JTextArea();
        rightPanel.add(new JScrollPane(drinkDetailsTextArea), BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);

        // Add action listeners for the "Edit Drink" button
        editDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle editing the drink and updating the drink info
                drinkNames[selectedDrink] = nameField.getText();
                drinkPrices[selectedDrink] = priceField.getText();
                drinkIngredients[selectedDrink] = ingredientsField.getText();

                drinkButtons[selectedDrink].setText(drinkNames[selectedDrink]); // Update the drink button text
                updateDisplay();
            }
        });

        // Add action listeners for the "Add Drink" button
        addDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new drink
                addDrink("New Drink", "0.00", "Unknown Ingredients");
            }
        });

        // Add action listeners for the "Remove Drink" button
        removeDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle removing the selected drink and update the UI
                removeDrink();
            }
        });
    }

    private void addDrink(String name, String price, String ingredients) {
        // Add a new drink to the arrays
        int numDrinks = drinkButtons.length + 1;
        String[] newNames = new String[numDrinks];
        String[] newPrices = new String[numDrinks];
        String[] newIngredients = new String[numDrinks];

        // Copy existing data to the new arrays
        for (int i = 0; i < drinkButtons.length; i++) {
            newNames[i] = drinkNames[i];
            newPrices[i] = drinkPrices[i];
            newIngredients[i] = drinkIngredients[i];
        }

        // Add the new drink data
        newNames[numDrinks - 1] = name;
        newPrices[numDrinks - 1] = price;
        newIngredients[numDrinks - 1] = ingredients;

        // Update arrays with new data
        drinkNames = newNames;
        drinkPrices = newPrices;
        drinkIngredients = newIngredients;

        // Update drink buttons and display
        updateDrinkButtons();
        updateDisplay();
    }

    private void removeDrink() {
        if (drinkButtons.length == 0) {
            return; // No drinks to remove
        }

        int numDrinks = drinkButtons.length - 1;
        if (selectedDrink >= numDrinks) {
            selectedDrink = numDrinks;
        }

        // Create new arrays without the removed drink
        String[] newNames = new String[numDrinks];
        String[] newPrices = new String[numDrinks];
        String[] newIngredients = new String[numDrinks];

        // Copy data to the new arrays, excluding the removed drink
        for (int i = 0, j = 0; i < drinkButtons.length; i++) {
            if (i != selectedDrink) {
                newNames[j] = drinkNames[i];
                newPrices[j] = drinkPrices[i];
                newIngredients[j] = drinkIngredients[i];
                j++;
            }
        }

        // Update arrays with new data
        drinkNames = newNames;
        drinkPrices = newPrices;
        drinkIngredients = newIngredients;

        // Update drink buttons and display
        updateDrinkButtons();
        updateDisplay();
    }

    private void updateDrinkButtons() {
        // Clear existing drink buttons
        for (int i = 0; i < drinkButtons.length; i++) {
            centerPanel.remove(drinkButtons[i]);
        }

        // Create new drink buttons based on the updated data
        drinkButtons = new JButton[drinkNames.length];
        for (int i = 0; i < drinkNames.length; i++) {
            drinkButtons[i] = new JButton(drinkNames[i]);

            // Increase the font size for the buttons
            Font buttonFont = new Font(drinkButtons[i].getFont().getName(), Font.PLAIN, 36);
            drinkButtons[i].setFont(buttonFont);

            final int drinkNumber = i; // Capture the current drink number
            drinkButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDrink = drinkNumber;
                    nameField.setText(drinkNames[selectedDrink]); // Update the name field
                    priceField.setText(drinkPrices[selectedDrink]); // Update the price field
                    ingredientsField.setText(drinkIngredients[selectedDrink]); // Update the ingredients field
                    updateDisplay();
                }
            });

            centerPanel.add(drinkButtons[i]);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void updateDisplay() {
        if (selectedDrink >= 0 && selectedDrink < drinkButtons.length) {
            String name = drinkNames[selectedDrink];
            String price = drinkPrices[selectedDrink];
            String ingredients = drinkIngredients[selectedDrink];
            String drinkInfo = "Drink Info:\nName: " + name + "\nPrice: $" + price + "\nIngredients: " + ingredients;
            drinkDetailsTextArea.setText(drinkInfo);
        } else {
            drinkDetailsTextArea.setText("Select a drink");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menus Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);

            frame.add(new MenusApp());
            frame.setVisible(true);
        });
    }
}
