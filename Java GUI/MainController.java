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

    /*
     * @Return : boolean
     * * false if the order fails to be created
     * * may be due to reasons: insufficient ingredient or errors
     */
    /*
     * @Param
     * * drink names: {string}
     * * sugar level: {0 || 30 || 50 || 80 || 100 || 120}
     * * ice level: {0 || 50 || 100}
     * * toppings: {string}
     */
    /*
     * @Function
     * * updates inventory
     * * updates orders
     * * returns true if everything functions properly, false otherwise
     */
    public static boolean Order(ArrayList<String> drinkNames, ArrayList<Integer> sugarLevel,
            ArrayList<Integer> iceLevel,
            ArrayList<String> toppings) {
        // find recipe of drinkName from recipe database
        int[] drinkIDs = new int[drinkNames.size()];
        float totalPrice = 0.0f;
        for (int index = 0; index < drinkNames.size(); index++) {
            String drinkName = drinkNames.get(index);
            try {
                String queryString = "SELECT * FROM recipes WHERE lower(drinkname) = '" + drinkName.toLowerCase()
                        + "';";
                ResultSet result = conn.createStatement().executeQuery(queryString);
                result.next();
                int recipeID = result.getInt("recipeid");

                Array ingredientsArr = result.getArray("ingredient_names");
                String[] ingredients = (String[]) ingredientsArr.getArray();
                Array ingredientAmountArr = result.getArray("ingredient_values");
                BigDecimal[] ingredientAmount = (BigDecimal[]) ingredientAmountArr.getArray();
                float price = result.getFloat("price");

                // make subtractions from inventory database
                for (int i = 0; i < ingredients.length; i++) {
                    String ingredient = ingredients[i].toLowerCase();
                    String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
                    ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
                    result2.next();
                    float amount = result2.getFloat("amount");
                    if (ingredient.equals("brown sugar") || ingredient.equals("fructose")
                            || ingredient.equals("honey") || ingredient.equals("white sugar")) {
                        amount -= (ingredientAmount[i]).intValue() * sugarLevel.get(index) / 100;
                    }
                    amount -= (ingredientAmount[i]).intValue();
                    if (amount < 0) {
                        return false;
                    }
                    String updateIngredientAmount = "UPDATE inventory SET amount = " + amount + " WHERE lower(name) = '"
                            + ingredient + "';";
                    conn.createStatement().executeUpdate(updateIngredientAmount);
                }
                // update ice
                String getIceAmount = "SELECT amount FROM inventory WHERE name = 'Ice';";
                result = conn.createStatement().executeQuery(getIceAmount);
                result.next();
                String updateIceAmount = "UPDATE inventory SET amount = "
                        + (result.getInt("amount") - iceLevel.get(index) / 10)
                        + " WHERE name = 'Ice';";
                conn.createStatement().executeUpdate(updateIceAmount);

                drinkIDs[index] = recipeID;
                totalPrice += price;

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                return false;
            }
        }

        // update topping, each topping is -10 in inventory
        for (int index = 0; index < toppings.size(); index++) {
            String topping = toppings.get(index).toLowerCase();
            String getToppingAmount = "SELECT amount FROM inventory WHERE lower(name) = '" + topping + "';";
            try {
                ResultSet result = conn.createStatement().executeQuery(getToppingAmount);
                result.next();
                String updateToppingAmount = "UPDATE inventory SET amount = "
                        + (result.getInt("amount") - 10)
                        + " WHERE lower(name) = '" + topping + "';";
                conn.createStatement().executeUpdate(updateToppingAmount);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                return false;
            }

        }
        java.time.LocalTime time = java.time.LocalTime.now();
        String updateOrdersString = "INSERT INTO orders(drink_id, date, time, cost) VALUES (ARRAY"
                + Arrays.toString(drinkIDs)
                + ", '" + java.time.LocalDate.now().toString() + "', '"
                + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "', "
                + totalPrice + ");";

        try {
            conn.createStatement().executeUpdate(updateOrdersString);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

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

        //open GUI
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp();
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
