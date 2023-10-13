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
            String sqlStatement = "UPDATE recipes SET price = " + price + " WHERE drinkname = '"
                    + drinkName + "'";
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateRecipePrice");
        }
    }

    public void updateRecipeIngredient(String ingredientName, String ingredientValue, String drinkName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE recipes SET ingredient_names = ARRAY_APPEND(ingredient_names,'"
                    + ingredientName + "'), ingredient_values = ARRAY_APPEND(ingredient_values,"
                    + ingredientValue + ") WHERE drinkname ='" + drinkName + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);

            // also update inventory if not exist
            stmt.executeUpdate("INSERT INTO inventory (name, amount, capacity, unit, alert) SELECT '"
                        + ingredientName + "', 0, 1000, 'unit', TRUE WHERE NOT EXISTS (SELECT name FROM inventory WHERE name = '" + ingredientName + "')");
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateRecipeIngredient");
        }
    }

    public void createNewRecipe(String drinkId, String drinkName, String[] ingredientNames, String[] ingredientValues,
            String price) {
        try {
            ArrayList<String> ingredient_names = new ArrayList<String>(Arrays.asList(ingredientNames));
            ArrayList<String> ingredient_values = new ArrayList<String>(Arrays.asList(ingredientValues));
            String ingredient_names_string = ingredient_names.toString();
            ingredient_names_string = ingredient_names_string.substring(1,ingredient_names_string.length()-1);
            String ingredient_values_string = ingredient_values.toString();
            ingredient_values_string = ingredient_values_string.substring(1,ingredient_values_string.length()-1);
            
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement

            String sqlStatement = "INSERT INTO recipes (recipeid, drinkname, ingredient_names, ingredient_values, price) VALUES ("
                    + drinkId + ", '" + drinkName + "','" + ingredient_names_string + "','"
                    + ingredient_values_string + "'," + price + ")";
            //System.out.println("names: " + ingredient_names);
            //System.out.println("names_string: " + ingredient_names_string);
            stmt.executeUpdate(sqlStatement);
            // determine which ingredients need to be created in inventory using Conflict
            String names;
            for (int i = 0; i < ingredient_names.size(); i++) {
                //remove quotes
                names = ingredientNames[i].substring(1,ingredientNames[i].length()-1);
                if (i == 0){
                    names = names.substring(1,names.length());
                }
                else if (i == ingredient_names.size()-1){
                    names = names.substring(0,names.length()-1);
                }
                //System.out.println("names: " + names);
                stmt.executeUpdate("INSERT INTO inventory (name, amount, capacity, unit, alert) SELECT '"
                        + names + "', 0, 1000, 'unit', TRUE WHERE NOT EXISTS (SELECT name FROM inventory WHERE name = '" + names + "')");
            }
        } catch (Exception e) {
            System.out.println("Error accessing Database.createNewRecipe");
            e.printStackTrace();
        }
    }

    public void removeRecipe(String drinkName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM recipes WHERE drinkname = '" + drinkName + "'";

            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. removeRecipe");
            e.printStackTrace();
        }
    }

    public void createNewEmployee(String employeeName, String password, String pay, String hours, String manager) {
        Boolean ret = false;
        int rows = 0;
        try {
            //determine if manager
            String m = "FALSE";
            if (manager.charAt(0) == 'T' || manager.charAt(0) == 't'){
                m = "TRUE";
            }
            // create a statement object
            Statement stmt = conn.createStatement();

            stmt.executeQuery("SELECT setval(pg_get_serial_sequence('employee','id'), coalesce(max(id)+1, 1), false) FROM employee");
            // create an SQL statement
            String sqlStatement = "INSERT INTO employee (name, password, pay, hours, manager) VALUES ('" + employeeName
                    + "','" + password + "'," + pay + "," + hours + "," + m + ")";

            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. createNewEmployee");
            e.printStackTrace();
        }
    }

    public void removeEmployeeSQL(String employeeName) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM employee WHERE name ='" + employeeName + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. removeEmployeeSQL");
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
            System.out.println("Error access dtabase. getNumEmployee");

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
            System.out.println("Error accessing database, get num of managers");

        }
        return managers;
    }

    public TreeMap<String, Boolean> getEmployeeNames() {
        TreeMap<String, Boolean> map = new TreeMap<>();
        try {
            String sqlStatement = "SELECT name, manager FROM employee;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                String name = result.getString(1);
                boolean isManager = result.getBoolean(2);
                map.put(name, isManager);
            }
        } catch (Exception e) {
            System.out.println("error accessing database, getEmployee Names");

        }
        return map;
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

    public void updateEmployeeSQL(String employeeName, String hours, String password, String pay) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE employee SET hours = " + hours + ", pay = " + pay +  ", password = " + password + " WHERE name = '"
                    + employeeName + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateEmployeeSQL");
            e.printStackTrace();
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
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient;
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. addInventory");
            e.printStackTrace();
        }
    }

    // remove amount of ingredient from inventory
    public void removeFromInventory(String ingredient, String reduceAmount) {
        try {
            // get original amount
            String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "'";
            ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
            result2.next();
            float amount = result2.getFloat("amount");

            float reduceAmountFloat = Float.parseFloat(reduceAmount);
            // reduce amount by desired amount
            amount -= reduceAmountFloat;

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient;
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. removeFromInventory");
            e.printStackTrace();
        }
    }

    // add new ingredient to inventory
    public void createNewInventory(String ingredient, String capacity, String unit) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredient
                    + ", 0," + capacity + "," + unit + ", TRUE) ON CONFLICT (name) DO NOTHING";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. createNewInventory");
            e.printStackTrace();
        }
    }

    // remove ingredient from inventory
    public void deleteInventory(String ingredient) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "DELETE FROM inventory WHERE name =" + ingredient;
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. deleteInventory");
            e.printStackTrace();
        }
    }

    public int getNumberOfDrinks() {
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT COUNT(*) FROM recipes;";
            ResultSet result = stmt.executeQuery(sqlString);
            result.next();
            return result.getInt(1);

        } catch (Exception e) {
            System.out.println("Error counting recipes. getNumberofDrinks");
        }
        return -1;
    }

    public ArrayList<String> getAllDrinkNames() {
        ArrayList<String> drinks = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT drinkname FROM recipes;";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                drinks.add(result.getString(1));
            }

        } catch (Exception e) {
            System.out.println("Error getting all drink names. getAllDrinkNames");
        }
        return drinks;
    }

    public ArrayList<String> getDrinkInfo(String name) {
        ArrayList<String> info = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT * FROM recipes WHERE lower(drinkname) = '" + name.toLowerCase() + "';";
            ResultSet result = stmt.executeQuery(sqlString);
            result.next();
            info.add(Integer.toString(result.getInt(1)));
            info.add(result.getString(2));
            info.add(result.getArray(3).toString());
            info.add(result.getArray(4).toString());
            info.add(Double.toString(result.getDouble(5)));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting drink info. getDrinkInfo");
        }
        return info;
    }

}