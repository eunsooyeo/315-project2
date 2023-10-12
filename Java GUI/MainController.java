import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    public static Connection conn = null;

    public static void main(String args[]) {

        String teamName = "10r";
        String dbName = "csce315331_" + teamName + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        //connect to database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        try {
            // create a statement object
            Statement createStmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // try {
        //     conn.close();
        //     System.out.println("Connection Closed.");
        // } catch (Exception e) {
        //     System.out.println("Connection NOT Closed.");
        // }

        //create order class
        Order order = new Order(conn);

        //open GUI
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp(order);
            loginApp.setVisible(true);
        });

        // Test ================ ================ ================ ================
        // ArrayList<String> drinknames = new ArrayList<String>();
        // drinknames.add("Honey milk tea");
        // ArrayList<Integer> sugarlevel = new ArrayList<Integer>();
        // sugarlevel.add(50);
        // ArrayList<Integer> icelevel = new ArrayList<Integer>();
        // icelevel.add(50);
        // ArrayList<String> toppingnames = new ArrayList<String>();
        // toppingnames.add("Aiyu Jelly");
        // toppingnames.add("Pearls");
        // Order(drinknames, sugarlevel, icelevel, toppingnames);

        // ================ ================ ================ ================
        // ================

        // closing the connection
       
    }
}
