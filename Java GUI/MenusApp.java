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
    //private JTextField idField;
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
    private JPanel drinkListPanel;

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

        // Simulate a list of drinks
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

        // Create the center panel with a scrollable list of drinks
        centerPanel = new JPanel(new BorderLayout());

        // Create a scroll pane to hold the drink list
        JScrollPane scrollPane = new JScrollPane();
        drinkListPanel = new JPanel();
        drinkListPanel.setLayout(new BoxLayout(drinkListPanel, BoxLayout.Y_AXIS));

        drinkButtons = new JButton[numDrinks];

        for (int i = 0; i < numDrinks; i++ ) {
            JPanel drinkRow = new JPanel(new BorderLayout());
            drinkRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

            JLabel drinkLabel = new JLabel(allDrinks.get(i));
            drinkButtons[i] = new JButton(allDrinks.get(i));

            drinkLabel.setHorizontalAlignment(SwingConstants.CENTER);
            drinkButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            drinkButtons[i].setBackground(new Color(240, 240, 240));

            drinkRow.add(drinkLabel, BorderLayout.CENTER);
            drinkRow.add(drinkButtons[i], BorderLayout.CENTER);

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

            drinkListPanel.add(drinkRow);
        }

        scrollPane.setViewportView(drinkListPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Create the panel for displaying drink details
        JPanel rightPanel = new JPanel(new BorderLayout());
        drinkDetailsTextArea = new JTextArea();
        rightPanel.add(new JScrollPane(drinkDetailsTextArea), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Create the panel for drink information and editing
        //JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JScrollPane rscroll = new JScrollPane(rightPanel);
        add(rscroll, BorderLayout.EAST);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        // Add labels and fields for editing
        nameField = new JTextField(20);
        //idField = new JTextField(20);
        priceField = new JTextField(20);
        ingredientsField = new JTextField(20);
        ingredientsValueField = new JTextField(20);
        editDrinkButton = new JButton("Edit Drink");
        addDrinkButton = new JButton("Add Drink");
        removeDrinkButton = new JButton("Remove Drink");

        //editPanel.add(new JLabel("ID:"));
        //editPanel.add(idField);
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
                drinkNames[selectedDrink] = nameField.getText();
                drinkIDs[selectedDrink] = managerFunctions.getDrinkInfo(drinkNames[selectedDrink]).get(0);
                drinkPrices[selectedDrink] = priceField.getText();
                drinkIngredients[selectedDrink] = ingredientsField.getText();
                drinkIngredientsAmounts[selectedDrink] = ingredientsValueField.getText();

                //update recipe database
                managerFunctions.updateRecipeIngredient(drinkIngredients[selectedDrink],drinkIngredientsAmounts[selectedDrink],drinkNames[selectedDrink]);
                managerFunctions.updateRecipePrice(drinkNames[selectedDrink], drinkPrices[selectedDrink]);
                drinkButtons[selectedDrink].setText(drinkNames[selectedDrink]); // Update the drink button text
                updateDisplay();
            }
        });

        // Add action listeners for the "Add Drink" button
        addDrinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Drink button clicked");
                // Add a new drink
                addDrink(nameField.getText(), priceField.getText(), ingredientsField.getText(), ingredientsValueField.getText());
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

    private void addDrink(String name, String price, String ingredients, String ingredientAmounts) {
        // Add a new drink to the arrays
        int numDrinks = drinkButtons.length + 1;
        String[] newIds = new String[numDrinks];
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

        String[] ingredientOfDrink = ingredients.split(",");
        String[] ingredientAmountsOfDrink = ingredientAmounts.split(",");
        //update database
        managerFunctions.createNewRecipe(Integer.toString(managerFunctions.getNumberOfDrinks() + 1), name, ingredientOfDrink, ingredientAmountsOfDrink, price);
        
        // Copy existing data to the new arrays
        for (int i = 0; i < drinkButtons.length; i++) {
            newIds[i] = drinkIDs[i];
            newNames[i] = drinkNames[i];
            newPrices[i] = drinkPrices[i];
            newIngredients[i] = drinkIngredients[i];
            newIngredientAmounts[i] = drinkIngredientsAmounts[i];
        }

        // Add the new drink data
        newIds[numDrinks -1] = Integer.toString(managerFunctions.getNumberOfDrinks() + 1);
        newNames[numDrinks - 1] = name;
        newPrices[numDrinks - 1] = price;
        newIngredients[numDrinks - 1] = ingredients;
        newIngredientAmounts[numDrinks - 1] = ingredientAmounts;

        // Update arrays with new data
        drinkIDs = newIds;
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
        //update database
        managerFunctions.removeRecipe(drinkNames[selectedDrink]);
        
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
        //updateDisplay();

        nameField.setText("");
        priceField.setText("");
        ingredientsField.setText("");
        ingredientsValueField.setText("");
        drinkDetailsTextArea.setText("Select a drink");
    }

    private void updateDrinkButtons() {
        // Clear existing drink buttons
        drinkListPanel.removeAll();
        /*for (int i = 0; i < drinkButtons.length; i++) {
            drinkListPanel.remove(drinkButtons[i]);
        } */

        // Create new drink buttons based on the updated data
        drinkButtons = new JButton[drinkNames.length];

        for (int i = 0; i < drinkNames.length; i++) {
            JPanel drinkRow = new JPanel(new BorderLayout());
            drinkRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

            JLabel drinkLabel = new JLabel(drinkNames[i]);
            drinkButtons[i] = new JButton(drinkNames[i]);

            drinkLabel.setHorizontalAlignment(SwingConstants.CENTER);
            drinkButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            drinkButtons[i].setBackground(new Color(240, 240, 240));

            drinkRow.add(drinkLabel, BorderLayout.CENTER);
            drinkRow.add(drinkButtons[i], BorderLayout.CENTER);

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

            drinkListPanel.add(drinkRow);
        }

        drinkListPanel.revalidate();
        drinkListPanel.repaint();
        updateDisplay();
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
