import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;  // Import WindowAdapter
import java.awt.event.WindowEvent;    // Import WindowEvent
import java.util.*;
import java.sql.*;


public class MenusApp extends JPanel {
    private JTextArea drinkDetailsTextArea;
    private JTextField idField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField ingredientsField;
    private JTextField ingredientsValueField;
    private JButton editDrinkButton;
    private JButton addDrinkButton;
    private JButton removeDrinkButton;
    private JButton[] drinkButtons;
    private String[] drinkNames;
    private String[] drinkPrices;
    private String[] drinkIngredients;
    private String[] drinkIngredientsAmounts;
    private String[] drinkIDs;
    private int selectedDrink;
    private JPanel centerPanel;
    private ManagerFunctions managerFunctions;

    public MenusApp(ManagerFunctions m) {
        setLayout(new BorderLayout());
        managerFunctions = m;
        int numDrinks = managerFunctions.getNumberOfDrinks(); // Specify the number of initial drinks

        // Initialize arrays to store drink data
        drinkIDs = new String[numDrinks];
        drinkNames = new String[numDrinks];
        drinkPrices = new String[numDrinks];
        drinkIngredients = new String[numDrinks];
        drinkIngredientsAmounts = new String[numDrinks];

        ArrayList<String> allDrinks = managerFunctions.getAllDrinkNames();

        for (int i = 0; i < numDrinks; i++) {
            ArrayList<String> drinkInformation = new ArrayList<>();
            drinkInformation = managerFunctions.getDrinkInfo(allDrinks.get(i));
            
            drinkIDs[i] = drinkInformation.get(0);
            drinkNames[i] = allDrinks.get(i); // Initialize drink names
            drinkPrices[i] = drinkInformation.get(4); // Initialize drink prices
            drinkIngredients[i] = drinkInformation.get(2); // Initialize drink ingredients
            drinkIngredientsAmounts[i] = drinkInformation.get(3);

        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        // Create the center panel for drink buttons
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(750, 300));

        // Create a scroll pane for the drink buttons panel
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // Create a grid layout for the drink buttons with 5 columns
        int maxColumns = 5;
        centerPanel.setLayout(new GridLayout(0, maxColumns)); // 0 rows, maxColumns columns
        

        drinkButtons = new JButton[numDrinks];
        for (int i = 0; i < numDrinks; i++) {
            drinkButtons[i] = new JButton(drinkNames[i]);
            

            Dimension buttonSize = new Dimension(150, 50); // Set your preferred button size here
            drinkButtons[i].setPreferredSize(buttonSize);
            // Increase the font size for the buttons
            Font buttonFont = new Font(drinkButtons[i].getFont().getName(), Font.PLAIN, 18);
            drinkButtons[i].setFont(buttonFont);

            final int drinkNumber = i; // Capture the current drink number
            drinkButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDrink = drinkNumber;
                    nameField.setText(drinkNames[selectedDrink]); // Update the name field
                    priceField.setText(drinkPrices[selectedDrink]); // Update the price field
                    ingredientsField.setText(drinkIngredients[selectedDrink]); // Update the ingredients field
                    ingredientsValueField.setText(drinkIngredientsAmounts[selectedDrink]); //update ingredients amount field
                    updateDisplay();
                }
            });

            centerPanel.add(drinkButtons[i]);
        }

        add(scrollPane, BorderLayout.WEST); // Place the scrollable panel on the left

        // Create the panel for drink information and editing
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JScrollPane rscroll = new JScrollPane(rightPanel);
        add(rscroll, BorderLayout.EAST);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        // Add labels and fields for editing
        nameField = new JTextField(20);
        idField = new JTextField(20);
        priceField = new JTextField(20);
        ingredientsField = new JTextField(20);
        ingredientsValueField = new JTextField(20);
        editDrinkButton = new JButton("Edit Drink");
        addDrinkButton = new JButton("Add Drink");
        removeDrinkButton = new JButton("Remove Drink");

        editPanel.add(new JLabel("ID:"));
        editPanel.add(idField);
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Ingredients:"));
        editPanel.add(ingredientsField);
        editPanel.add(new JLabel("Ingredient amounts:"));
        editPanel.add(ingredientsValueField);
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
                drinkIDs[selectedDrink] = idField.getText();
                drinkNames[selectedDrink] = nameField.getText();
                drinkPrices[selectedDrink] = priceField.getText();
                drinkIngredients[selectedDrink] = ingredientsField.getText();
                drinkIngredientsAmounts[selectedDrink] = ingredientsValueField.getText();

                drinkButtons[selectedDrink].setText(drinkNames[selectedDrink]); // Update the drink button text
                updateDisplay();

                //update recipe database
                managerFunctions.updateRecipeIngredient(drinkIngredients[selectedDrink],drinkIngredientsAmounts[selectedDrink],drinkNames[selectedDrink]);
                managerFunctions.updateRecipePrice(drinkNames[selectedDrink], drinkPrices[selectedDrink]);
            }
        });

        // Add action listeners for the "Add Drink" button
        addDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Drink button clicked");
                // Add a new drink
                addDrink(nameField.getText(), priceField.getText(), ingredientsField.getText(), ingredientsValueField.getText());
                String[] ingredientOfDrink = ingredientsField.getText().split(",");
                String[] ingredientAmountsOfDrink = ingredientsValueField.getText().split(",");
                //update database
                managerFunctions.createNewRecipe(idField.getText(), nameField.getText(), ingredientOfDrink, ingredientAmountsOfDrink, priceField.getText());
            }
        });

        // Add action listeners for the "Remove Drink" button
        removeDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle removing the selected drink and update the UI
                removeDrink();

                //update database
                managerFunctions.removeRecipe(drinkNames[selectedDrink]);
            }
        });

    }

    private void addDrink(String name, String price, String ingredients, String ingredientAmounts) {
        // Add a new drink to the arrays
        int numDrinks = drinkButtons.length + 1;
        String[] newNames = new String[numDrinks];
        String[] newPrices = new String[numDrinks];
        String[] newIngredients = new String[numDrinks];
        String[] newIngredientAmounts = new String[numDrinks];
        for (int i = 0; i < drinkNames.length; i++) {
            if (drinkNames[i].equals(name)) {
            // Drink with the same name already exists, so we won't add it again
            return;
            }
        }
        // Copy existing data to the new arrays
        for (int i = 0; i < drinkButtons.length; i++) {
            newNames[i] = drinkNames[i];
            newPrices[i] = drinkPrices[i];
            newIngredients[i] = drinkIngredients[i];
            newIngredientAmounts[i] = drinkIngredientsAmounts[i];
        }

        // Add the new drink data
        newNames[numDrinks - 1] = name;
        newPrices[numDrinks - 1] = price;
        newIngredients[numDrinks - 1] = ingredients;
        newIngredientAmounts[numDrinks - 1] = ingredientAmounts;

        // Update arrays with new data
        drinkNames = newNames;
        drinkPrices = newPrices;
        drinkIngredients = newIngredients;
        drinkIngredientsAmounts = newIngredientAmounts;

        // Update drink buttons and display
        updateDrinkButtons();
        updateDisplay();

        // Update the UI to reflect the changes
        centerPanel.revalidate();
        centerPanel.repaint();
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
        String[] newIngredientAmounts = new String[numDrinks];

        // Copy data to the new arrays, excluding the removed drink
        for (int i = 0, j = 0; i < drinkButtons.length; i++) {
            if (i != selectedDrink) {
                newNames[j] = drinkNames[i];
                newPrices[j] = drinkPrices[i];
                newIngredients[j] = drinkIngredients[i];
                newIngredientAmounts[j] = drinkIngredientsAmounts[i];
                j++;
            }
        }

        // Update arrays with new data
        drinkNames = newNames;
        drinkPrices = newPrices;
        drinkIngredients = newIngredients;
        drinkIngredientsAmounts = newIngredientAmounts;

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
            Font buttonFont = new Font(drinkButtons[i].getFont().getName(), Font.PLAIN, 18);
            drinkButtons[i].setFont(buttonFont);

            final int drinkNumber = i; // Capture the current drink number
            drinkButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDrink = drinkNumber;
                    nameField.setText(drinkNames[selectedDrink]); // Update the name field
                    priceField.setText(drinkPrices[selectedDrink]); // Update the price field
                    ingredientsField.setText(drinkIngredients[selectedDrink]); // Update the ingredients field
                    ingredientsValueField.setText(drinkIngredientsAmounts[selectedDrink]);
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
            String id = drinkIDs[selectedDrink];
            String price = drinkPrices[selectedDrink];
            String ingredients = drinkIngredients[selectedDrink];
            String ingredientAmounts = drinkIngredientsAmounts[selectedDrink];
            String drinkInfo = "Drink Info:\nID: " + id + "\nName: "+ name + "\nPrice: $" + price + "\nIngredients: " + ingredients + "\nIngredient amounts: " + ingredientAmounts;
            drinkDetailsTextArea.setText(drinkInfo);
        } else {
            drinkDetailsTextArea.setText("Select a drink");
        }
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menus Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            MenusApp ma = new MenusApp(managerFunctions);
            frame.add(ma);
            frame.setVisible(true);
        });
    }*/
}
