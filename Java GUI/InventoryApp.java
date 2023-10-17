import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

/*
Inventory app sets up GUI for the inventory page in manager side of POS
@author Kevin Tang
@author Dicong Wang
*/
public class InventoryApp extends JPanel {
    private JButton prevItembutton = null;
    private ManagerFunctions managerFunctions;
    private JTextArea detailsTextArea;
    private JTextField nameField;
    private JTextField amountsField;
    private JTextField capacityAmountField;
    private JTextField unitsField;

    /*
    @function Constructor that sets up the inventory interface
    @param m includes usage of managerfunctions class
    @return none
    @throws none
    */
    public InventoryApp(ManagerFunctions m) {
        setLayout(new BorderLayout());
        managerFunctions = m;
        // Create the center panel with a grid of 41 ingredients/items
        JPanel centerPanel = new JPanel(new GridLayout(6, 7));

        ArrayList<String> items = getAllInventoryNames();

        // Create the right sidebar for displaying item details
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BorderLayout());
        detailsTextArea = new JTextArea("Details will be displayed here.");
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));


            //add labels and fields for editing
        nameField = new JTextField(20);
        amountsField = new JTextField(20);
        capacityAmountField = new JTextField(20);
        unitsField  = new JTextField(20);

        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Amount:"));
        editPanel.add(amountsField);
        editPanel.add(new JLabel("Capacity:"));
        editPanel.add(capacityAmountField);
        editPanel.add(new JLabel("Units:"));
        editPanel.add(unitsField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding an item
                // Implement the logic to add an item to the inventory
                // You can open a dialog or prompt the user for item details
                managerFunctions.createNewInventory(nameField.getText(), amountsField.getText(), capacityAmountField.getText(), unitsField.getText());
                updateDisplay();
            }
            
        });
        
        // Create "Edit" button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle editing an item
                // Implement the logic to edit an item in the inventory
                // You can open a dialog or prompt the user for item details to edit
                managerFunctions.updateInventory(nameField.getText(), amountsField.getText(), capacityAmountField.getText(), unitsField.getText());
                updateDisplay();
            }
        });

        // Create "Delete" button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle deleting an item
                // Implement the logic to delete an item from the inventory
                // You can ask for confirmation or selection before deleting
                managerFunctions.deleteInventory(nameField.getText());
                updateDisplay();
            }
        });

        editPanel.add(addButton);
        editPanel.add(editButton);
        editPanel.add(deleteButton);

        // Add components to the right sidebar
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);
        rightSidebar.add(editPanel, BorderLayout.SOUTH);

        // Add center panel and right sidebar to the main panel


        ArrayList<String> lowDrinks = getAllLowInventory();
        for (String item : items) {
            JButton itemButton = new JButton(item);
            if (lowDrinks.contains(item)) {
                // System.out.println("It should be yellow");
                itemButton.setBackground(Color.YELLOW);
                itemButton.setText("<html>" + item + "<br>(low supply)</html>");
                itemButton.setOpaque(true);
            } else
                itemButton.setBackground(Color.GRAY);
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    detailsTextArea.selectAll();
                    detailsTextArea.replaceSelection(item);
                    detailsTextArea.append("\n");
                    ArrayList<String> arr = getInfoForIngredient(item);

                    detailsTextArea.append(arr.get(0));
                    // if (arr.get(1).equals("true")) {
                    // prevItembutton = itemButton;
                    // itemButton.setBackground(Color.YELLOW);
                    // itemButton.setOpaque(true);

                    // }
                    // if (prevItembutton != null && (prevItembutton != itemButton)) {
                    // prevItembutton.setBackground(Color.GRAY);
                    // prevItembutton.setOpaque(false);
                    // }
                    nameField.setText(item);

                    String lines[] = arr.get(0).split("/");
                    String cap = lines[1];

                    int i;
                    for(i = 0; i < cap.length(); i ++) {
                        if(!Character.isDigit(cap.charAt(i)) && cap.charAt(i) != '.') break;
                    }

                    amountsField.setText(lines[0]);
                    capacityAmountField.setText(cap.substring(0, i));
                    unitsField.setText(cap.substring(i));
                }
            });
            centerPanel.add(itemButton);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);
    }

    /*
    @function Get function that returns all inventory less than 10% of amount/capacity
    @param none
    @return arraylist of the names of low ingredients
    @throws error from accessing database

    */
    private ArrayList<String> getAllLowInventory() {
        ArrayList<String> drinksLow = new ArrayList<>();

        try {
            String teamName = "10r";
            String dbName = "csce315331_" + teamName + "_db";
            String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
            dbSetup myCredentials = new dbSetup();

            Connection conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement();
            String sqlString = "SELECT name FROM inventory;";
            ResultSet result = stmt.executeQuery(sqlString);
            ArrayList<String> drinks = new ArrayList<>();
            while (result.next()) {
                drinks.add(result.getString(1));
            }

            for (String name : drinks) {
                sqlString = "SELECT (amount, capacity, unit) FROM inventory WHERE lower(name) = '"
                        + name.toLowerCase() + "';";
                result = stmt.executeQuery(sqlString);
                result.next();
                String str = result.getString(1);
                double amount = Double.parseDouble(str.substring(1, str.indexOf(",")));
                double cap = Double
                        .parseDouble(str.substring(str.indexOf(",") + 1, str.indexOf(",", str.indexOf(",") + 1)));
                String unit = str.substring(str.indexOf(",", str.indexOf(",") + 1) + 1, str.length() - 1);

                if (checkIfLow(name, amount, cap)) {
                    drinksLow.add(name);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting all low inventory names. getAllLowInventory");
        }
        return drinksLow;
    }

    /*
    @function Get function for a list of all ingredients in inventory, includes database connection
    @param none
    @return arraylist of the ingredient names
    @throws error when accessing database

    */
    public ArrayList<String> getAllInventoryNames() {
        ArrayList<String> drinks = new ArrayList<>();
        try {
            String teamName = "10r";
            String dbName = "csce315331_" + teamName + "_db";
            String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
            dbSetup myCredentials = new dbSetup();

            Connection conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement();
            String sqlString = "SELECT name FROM inventory;";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                drinks.add(result.getString(1));
            }

        } catch (Exception e) {
            System.out.println("Error getting all inventory names. getAllInventoryNames");
        }
        return drinks;
    }
    /*
    @function Get function for info about ingredient: name, amount, capacity, unit
    @param name string of ingredient name
    @return arraylist of ingredient info
    @throws error when accessing database

    */
    private ArrayList<String> getInfoForIngredient(String name) {
        ArrayList<String> arr = new ArrayList<>();
        String s = "";
        try {
            String teamName = "10r";
            String dbName = "csce315331_" + teamName + "_db";
            String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
            dbSetup myCredentials = new dbSetup();

            Connection conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement();
            String sqlString = "SELECT (amount, capacity, unit) FROM inventory WHERE lower(name) = '"
                    + name.toLowerCase() + "';";
            ResultSet result = stmt.executeQuery(sqlString);
            result.next();
            String str = result.getString(1);
            double amount = Double.parseDouble(str.substring(1, str.indexOf(",")));
            double cap = Double
                    .parseDouble(str.substring(str.indexOf(",") + 1, str.indexOf(",", str.indexOf(",") + 1)));
            String unit = str.substring(str.indexOf(",", str.indexOf(",") + 1) + 1, str.length() - 1);
            s = Double.toString(amount) + "/" + Double.toString(cap) + unit;
            arr.add(s);
            if (checkIfLow(name, amount, cap)) {
                arr.add("TRUE");
            } else {
                arr.add("FALSE");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting amount");
        }
        return arr;
    }
    /*
    @function Function that returns true if the ingredient's amount/capacity is < 10%
    @param name string of ingredient name
    @param amount string of amount of ingredient
    @param cap string of capacity
    @return boolean for if the item is low and to update database
    @throws error when accessing database

    */
    private boolean checkIfLow(String name, double amount, double cap) {
        try {
            if (amount / cap > 0.1) {
                return false;
            }

            String teamName = "10r";
            String dbName = "csce315331_" + teamName + "_db";
            String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
            dbSetup myCredentials = new dbSetup();

            Connection conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement();
            String sqlString = "UPDATE inventory SET alert = true WHERE lower(name) = '" + name.toLowerCase() + "';";
            stmt.executeUpdate(sqlString);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting amount");
        }
        return false;
    }

    /*
    @function Function that updates to display ingredient information when button is pressed
    @param none
    @return none
    @throws none

    */
    public void updateDisplay(){
        //TODO !!!!
        JPanel centerPanel = (JPanel) getComponent(0);  // Assuming the center panel is the first component
        centerPanel.removeAll();  // Clear the current buttons

        ArrayList<String> items = getAllInventoryNames();
        ArrayList<String> low = getAllLowInventory();
        for (String item : items) {
            JButton itemButton = new JButton(item);
            if(low.contains(item)){
                itemButton.setBackground(Color.YELLOW);
                itemButton.setOpaque(true);
                itemButton.setText("<html>" + item + "<br>(low supply)</html>");
            }
            else{
                itemButton.setBackground(Color.GRAY);
            }
            
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle button click
                    // Display item details in the right sidebar
                    // Implement this as in your original code
                    detailsTextArea.selectAll();
                    detailsTextArea.replaceSelection(item);
                    detailsTextArea.append("\n");
                    ArrayList<String> arr = getInfoForIngredient(item);

                    detailsTextArea.append(arr.get(0));

                    nameField.setText(item);

                    String lines[] = arr.get(0).split("/");
                    String cap = lines[1];

                    int i;
                    for(i = 0; i < cap.length(); i ++) {
                        if(!Character.isDigit(cap.charAt(i)) && cap.charAt(i) != '.') break;
                    }

                    amountsField.setText(lines[0]);
                    capacityAmountField.setText(cap.substring(0, i));
                    unitsField.setText(cap.substring(i));
                }
            });
            centerPanel.add(itemButton);  // Add the new button to the center panel
        }

        // Repaint or revalidate the panel
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         JFrame frame = new JFrame("Inventory Page");
    //         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //         frame.setSize(800, 400);
    //         frame.add(new InventoryApp());
    //         frame.setVisible(true);
    //     });
    // }
}