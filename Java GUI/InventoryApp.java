import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

public class InventoryApp extends JPanel {
    private JButton prevItembutton = null;
    private ManageFunctions managerFunctions;
    public InventoryApp(ManagerFunctions m) {
        setLayout(new BorderLayout());
        managerFunctions = m;
        // Create the center panel with a grid of 41 ingredients/items
        JPanel centerPanel = new JPanel(new GridLayout(6, 7));

        ArrayList<String> items = getAllInventoryNames();

        // Create the right sidebar for displaying item details
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BorderLayout());
        //JTextArea detailsTextArea = new JTextArea("Details will be displayed here.");
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));


            //add labels and fields for editing
        JTextField nameField = new JTextField(20);
        JTextField amountsField = new JTextField(20);
        JTextField capacityAmountField = new JTextField(20);
        JTextField unitsField  = new JTextField(20);

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
                managerfunctions.deleteInventory(nameField.getText());
            }
        });



        ArrayList<String> lowDrinks = getAllLowInventory();
        for (String item : items) {
            JButton itemButton = new JButton(item);
            if (lowDrinks.contains(item)) {
                // System.out.println("It should be yellow");
                itemButton.setBackground(Color.YELLOW);
                itemButton.setOpaque(true);
            } else
                itemButton.setBackground(Color.GRAY);
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    /*
                     * TODO
                     ****************************************************************************************************************************/
                    // Display item details in the right sidebar
                    // Make its contents editable after retrieveing from Database
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

                }
            });
            centerPanel.add(itemButton);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);
    }

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
    public void updateDisplay(){
        //TODO !!!!
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