import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

public class ManagerFunctions {
    private ArrayList<String> drinkNames;
    private double totalPrice;
    private Connection conn;

    public ManagerFunctions(Connection db_connection) {
        conn = db_connection;
    }

    public void updateRecipePrice(String drinkName, String price) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE recipes SET price = " + Double.parseDouble(price) + " WHERE drinkname = "
                    + drinkName + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public void updateRecipeIngredient(String ingredientName, String ingredientValue, String drinkName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE recipes SET ingredient_names = ARRAY_APPEND(ingredient_names,"
                    + ingredientName + "), ingredient_values = ARRAY_APPEND(ingredient_values,"
                    + Double.parseDouble(ingredientValue) + ") WHERE drinkname =" + drinkName + ";";
            // also update inventory if not exist
            sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredientName
                    + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public void createNewRecipe(String drinkId, String drinkName, String[] ingredientNames, String[] ingredientValues,
            String price) {
        try {
            ArrayList<String> ingredient_names = new ArrayList<String>(Arrays.asList(ingredientNames));
            ArrayList<String> ingredient_values = new ArrayList<String>(Arrays.asList(ingredientValues));

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement

            // convert arraylist<string> to arraylist<double>
            ArrayList<Double> ingredient_values_double = new ArrayList<>();
            for (int i = 0; i < ingredient_values.size(); i++) {
                ingredient_values_double.add(Double.parseDouble(ingredient_values.get(i)));
            }

            String sqlStatement = "INSERT INTO recipes (recipeid, drinkname, ingredient_names, ingredient_values, price) VALUES ("
                    + Integer.parseInt(drinkId) + ", " + drinkName + "," + ingredient_names + ","
                    + ingredient_values_double + "," + Double.parseDouble(price) + ");";

            // determine which ingredients need to be created in inventory using Conflict
            for (int i = 0; i < ingredient_names.size(); i++) {
                sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES ("
                        + ingredient_names.get(i) + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
            }
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public void removeRecipe(String drinkName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM recipes WHERE drinkname = " + drinkName + ";";

            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public void createNewEmployee(String employeeName, String password, String pay, String hours, String manager) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "INSERT INTO employee (name, password, pay, hours, manager) VALUES (" + employeeName
                    + "," + password + "," + Double.parseDouble(pay) + "," + Double.parseDouble(hours) + "," + manager
                    + ");";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public void removeEmployeeSQL(String employeeName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM employee WHERE name =" + employeeName + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    public int getNumOfEmployees() {
        int employee = 0;

        try {
            String sqlStatement = "SELECT manager FROM employee;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                boolean isManager = result.getBoolean(1);
                if (!isManager) {
                    employee++;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());

        }
        return employee;

    }

    public int getNumOfManagers() {
        int managers = 0;

        try {
            String sqlStatement = "SELECT manager FROM employee;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                boolean isManager = result.getBoolean(1);
                if (isManager) {
                    managers++;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());

        }
        return managers;
    }

    public ArrayList<String> getEmployeeNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            String sqlStatement = "SELECT name FROM employee;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                String name = result.getString(1);
                names.add(name);
            }
        } catch (Exception e) {
            System.out.println(e.toString());

        }
        return names;
    }

    // returns an arraylist consist of the id, name, password, pay, hours, and
    // manager status
    // ^ all as string
    public ArrayList<String> getEmployeeInfo(String name) {
        ArrayList<String> info = new ArrayList<>();
        try {
            String sqlString = "SELECT * FROM employee WHERE name = '" + name + "';";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlString);

            result.next();
            info.add(Integer.toString(result.getInt(1)));
            info.add(result.getString(2));
            info.add(result.getString(3));
            info.add(Double.toString(result.getDouble(4)));
            info.add(Double.toString(result.getDouble(5)));
            info.add(Boolean.toString(result.getBoolean(6)));
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error getting employee info");
        }

        return info;
    }

    public void updateEmployeeSQL(String employeeName, String hours) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE employees SET hours = " + Double.parseDouble(hours) + " WHERE name = "
                    + employeeName + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    // add amount to ingredient in inventory
    public void addInventory(String ingredient, String newAmount) {
        try {
            // get original amount
            String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
            ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
            result2.next();
            float amount = result2.getFloat("amount");

            float newAmountFloat = Float.parseFloat(newAmount);
            // add newAmount
            amount += newAmountFloat;

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    // remove amount of ingredient from inventory
    public void removeFromInventory(String ingredient, String reduceAmount) {
        try {
            // get original amount
            String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
            ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
            result2.next();
            float amount = result2.getFloat("amount");

            float reduceAmountFloat = Float.parseFloat(reduceAmount);
            // reduce amount by desired amount
            amount -= reduceAmountFloat;

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    // add new ingredient to inventory
    public void createNewInventory(String ingredient, String capacity, String unit) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredient
                    + ", 0," + Double.parseDouble(capacity) + "," + unit + ", TRUE) ON CONFLICT (name) DO NOTHING;";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

    // remove ingredient from inventory
    public void deleteInventory(String ingredient) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM inventory WHERE name =" + ingredient + ";";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
    }

}