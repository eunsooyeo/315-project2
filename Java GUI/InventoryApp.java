import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

public class InventoryApp extends JPanel {
    public InventoryApp() {
        setLayout(new BorderLayout());

        // Create the center panel with a grid of 41 ingredients/items
        JPanel centerPanel = new JPanel(new GridLayout(6, 7));
        String[] items = {
            "Aiyu Jelly", "Aloe Vera", "Black Tea", "Brown Sugar", "Cocoa", "Coffee", "Creama",
            "Creamer", "Crystal Boba", "Cups", "Fructose", "Grapefruit", "Green Tea", "Herb Jelly",
            "Honey", "Ice", "Ice Cream", "Lime", "Lychee Jelly", "Mango", "Matcha", "Milk", "Mint",
            "Napkins", "Okinawa", "Oolong Tea", "Orange", "Passionfruit", "Peach", "Pearls", "Mini Pearl",
            "Pineapple", "Plastic Cover", "Pudding", "Red Bean", "Strawberry", "Straws", "Taro", "Water",
            "White Sugar", "Wintermelon"
        };

        // Create the right sidebar for displaying item details
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BorderLayout());
        JTextArea detailsTextArea = new JTextArea("Details will be displayed here.");
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);

        for (String item : items) {
            JButton itemButton = new JButton(item);
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
                    if (arr.get(1).equals("true")) {
                        itemButton.setBackground(Color.YELLOW);
                    }

                }
            });
            centerPanel.add(itemButton);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);
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
            System.out.println(sqlString);
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
                arr.add("true");
            } else {
                arr.add("false");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventory Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new InventoryApp());
            frame.setVisible(true);
        });
    }
}