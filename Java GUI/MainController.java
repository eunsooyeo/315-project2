import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is for the main control of the GUI and sets up the database
 * connection. Has main where all other pages eventually connect to
 * 
 * @author Kevin Tang
 * @author Dicong Wang
 */
public class MainController {
    public static Connection conn = null;

    public static void main(String args[]) {

        String teamName = "10r";
        String dbName = "csce315331_" + teamName + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        // connect to database
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

        // create order class
        Order order = new Order(conn);

        // create managerFunctions class
        ManagerFunctions managerFunctions = new ManagerFunctions(conn);

        // open GUI
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp(order, managerFunctions);
            loginApp.setVisible(true);
        });
    }
}
