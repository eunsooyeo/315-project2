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
            String sqlStatement = "UPDATE recipes SET price = " + price + " WHERE lower(drinkname) = '"
                    + drinkName.toLowerCase() + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateRecipePrice");
            e.printStackTrace();
        }
    }

    public void updateRecipeIngredient(String ingredientNames, String ingredientValues, String drinkName) {
        try {

            String[] ingredientNamesAry = ingredientNames.split(",");
            String[] ingredientValuesAry = ingredientValues.split(",");
            // System.out.println("names: " + ingredientNames);
            // ArrayList<String> ingredient_names = new
            // ArrayList<String>(Arrays.asList(ingredientNames));
            // ArrayList<String> ingredient_values = new
            // ArrayList<String>(Arrays.asList(ingredientValues));
            String ingredient_names_string = ingredientNames;
            ingredient_names_string = ingredient_names_string.substring(1, ingredient_names_string.length() - 1);
            String ingredient_values_string = ingredientValues;
            ingredient_values_string = ingredient_values_string.substring(1, ingredient_values_string.length() - 1);

            // create a statement object
            Statement stmt = conn.createStatement();
            // System.out.println("ingredient_names_string: " + ingredient_names_string);
            // create an SQL statement
            String sqlStatement = "UPDATE recipes SET ingredient_names = '{" + ingredient_names_string.toLowerCase()
                    + "}', ingredient_values = '{" + ingredient_values_string + "}' WHERE lower(drinkname) ='"
                    + drinkName.toLowerCase() + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);

            String names;
            for (int i = 0; i < ingredientNamesAry.length; i++) {
                // remove quotes
                if (ingredientNamesAry[i].startsWith("\"")) {
                    names = ingredientNamesAry[i].substring(1, ingredientNamesAry[i].length() - 1);
                } else {
                    names = ingredientNamesAry[i];
                }
                // remove brackets
                if (i == 0) {
                    names = names.substring(1, names.length());
                } else if (i == ingredientNamesAry.length - 1) {
                    names = names.substring(0, names.length() - 1);
                }
                // System.out.println("names: " + names);
                // also take care of spaces
                if (names.startsWith(" ")) {
                    names = names.substring(1, names.length());
                }
                // now remove inside quotes
                if (names.startsWith("\"")) {
                    names = names.substring(1, names.length() - 1);
                }
                createNewInventory(names, "0", "1000", "unit");
                /*
                 * stmt.
                 * executeUpdate("INSERT INTO inventory (name, amount, capacity, unit, alert) SELECT '"
                 * + names +
                 * "', 0, 1000, 'unit', TRUE WHERE NOT EXISTS (SELECT name FROM inventory WHERE lower (name) = '"
                 * + names.toLowerCase() + "')");
                 */
            }
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateRecipeIngredient");
            e.printStackTrace();
        }
    }

    public void createNewRecipe(String drinkId, String drinkName, String[] ingredientNames, String[] ingredientValues,
            String price) {
        try {
            ArrayList<String> ingredient_names = new ArrayList<String>(Arrays.asList(ingredientNames));
            ArrayList<String> ingredient_values = new ArrayList<String>(Arrays.asList(ingredientValues));
            String ingredient_names_string = ingredient_names.toString();
            ingredient_names_string = ingredient_names_string.substring(1, ingredient_names_string.length() - 1);
            String ingredient_values_string = ingredient_values.toString();
            ingredient_values_string = ingredient_values_string.substring(1, ingredient_values_string.length() - 1);

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement

            String sqlStatement = "INSERT INTO recipes (recipeid, drinkname, ingredient_names, ingredient_values, price) SELECT "
                    + (getNumberOfDrinks() + 1) + ", '" + drinkName + "','" + ingredient_names_string.toLowerCase()
                    + "','"
                    + ingredient_values_string + "'," + price
                    + " WHERE NOT EXISTS (SELECT drinkname FROM recipes WHERE lower(drinkname) = '"
                    + drinkName.toLowerCase() + "')";
            // System.out.println("names: " + ingredient_names);
            // System.out.println("names_string: " + ingredient_names_string);
            stmt.executeUpdate(sqlStatement);
            // determine which ingredients need to be created in inventory using Conflict
            String names;
            for (int i = 0; i < ingredient_names.size(); i++) {
                // remove quotes
                if (ingredientNames[i].startsWith("\"")) {
                    names = ingredientNames[i].substring(1, ingredientNames[i].length() - 1);
                } else {
                    names = ingredientNames[i];
                }
                // remove brackets
                if (i == 0) {
                    names = names.substring(1, names.length());
                } else if (i == ingredient_names.size() - 1) {
                    names = names.substring(0, names.length() - 1);
                }
                // also take care of spaces
                if (names.startsWith(" ")) {
                    names = names.substring(1, names.length());
                }
                // now remove inside quotes
                if (names.startsWith("\"")) {
                    names = names.substring(1, names.length() - 1);
                }
                createNewInventory(names, "0", "1000", "unit");
                // System.out.println("names: " + names);
                /*
                 * stmt.
                 * executeUpdate("INSERT INTO inventory (name, amount, capacity, unit, alert) SELECT '"
                 * + names.toLowerCase() +
                 * "', 0, 1000, 'unit', TRUE WHERE NOT EXISTS (SELECT name FROM inventory WHERE lower(name) = '"
                 * + names.toLowerCase() + "')");
                 */
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
            String sqlStatement = "DELETE FROM recipes WHERE lower(drinkname) = '" + drinkName.toLowerCase() + "'";

            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. removeRecipe");
            e.printStackTrace();
        }
    }

    public void createNewEmployee(String id, String employeeName, String password, String pay, String hours,
            String manager) {
        Boolean ret = false;
        int rows = 0;
        try {
            // determine if manager
            String m = "FALSE";
            if (manager.charAt(0) == 'T' || manager.charAt(0) == 't') {
                m = "TRUE";
            }
            // create a statement object
            Statement stmt = conn.createStatement();

            stmt.executeQuery(
                    "SELECT setval(pg_get_serial_sequence('employee','id'), coalesce(max(id)+1, 1), false) FROM employee");
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
            String sqlStatement = "DELETE FROM employee WHERE lower(name) ='" + employeeName.toLowerCase() + "'";
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
            String sqlStatement = "UPDATE employee SET hours = " + hours + ", pay = " + pay + ", password = '"
                    + password + "' WHERE name = '"
                    + employeeName + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. updateEmployeeSQL");
            e.printStackTrace();
        }
    }

    // add amount to ingredient in inventory
    public void updateInventory(String ingredient, String amount, String capacity, String unit) {
        try {
            String alert = "TRUE";
            if (Double.parseDouble(amount) / Double.parseDouble(capacity) > 0.1) {
                alert = "FALSE";
            }

            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + ", capacity = " + capacity + ", unit = '"
                    + unit + "', alert = " + alert + " WHERE lower(name) = '" + ingredient.toLowerCase() + "'";
            // send statement to DBMS
            stmt.executeUpdate(sqlStatement);
        } catch (Exception e) {
            System.out.println("Error accessing Database. addInventory");
            e.printStackTrace();
        }
    }

    // add new ingredient to inventory
    public void createNewInventory(String ingredient, String amount, String capacity, String unit) {
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            stmt.executeUpdate("INSERT INTO inventory (name, amount, capacity, unit, alert) SELECT '"
                    + ingredient.toLowerCase() + "'," + amount + "," + capacity + ", '" + unit
                    + "', TRUE WHERE NOT EXISTS (SELECT name FROM inventory WHERE lower(name) = '"
                    + ingredient.toLowerCase() + "')");
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
            String sqlStatement = "DELETE FROM inventory WHERE lower(name) = '" + ingredient.toLowerCase() + "'";
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

    public ArrayList<String> getAllSupplyHistory() {
        ArrayList<String> supplyHistory = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT * FROM supply_history;";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                String received = (result.getString(8) == null) ? "Not Received" : result.getString(8);
                supplyHistory.add(result.getString(2) + "     #" + result.getString(3) + "    " + result.getString(7)
                        + "     " + received + result.getString(4) + "\n" + result.getString(5) + "\n"
                        + result.getString(6));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting supply history .getAllSupplyHistory");
        }
        return supplyHistory;
    }

    public String getUnitOfIngredient(String name) {
        String unit = "";
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT unit FROM inventory WHERE lower(name) = '" + name.toLowerCase() + "';";
            ResultSet result = stmt.executeQuery(sqlString);
            result.next();
            unit = result.getString(1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting an ingredient's unit .getUnitOfIngredient");
        }
        return unit;
    }

    // @param : string beginningDate, string endDate
    // @return : drinkname, numberOfOrders, totalPrice as an list for every drink
    // ordered within the time period
    public ArrayList<ArrayList<String>> getFilteredSalesHistory(String beginningDate, String endDate) {
        ArrayList<ArrayList<String>> sales = new ArrayList<>();
        TreeMap<String, Double> drinkAndCost = new TreeMap<>();
        TreeMap<String, Integer> drinkAndNum = new TreeMap<>();
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT drink_id, cost FROM orders WHERE date >= '" + beginningDate + "' AND date <= '"
                    + endDate + "';";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                String tmp = result.getString(1);
                String[] drinkIDs = tmp.substring(1, tmp.length() - 1).split(",");
                // double cost = result.getDouble(2);
                for (String s : drinkIDs) {
                    // reference the recipe database, get name and price
                    sqlString = "SELECT drinkname, price FROM recipes WHERE recipeid = " + Integer.parseInt(s) + ";";
                    Statement stmt2 = conn.createStatement();
                    ResultSet result2 = stmt2.executeQuery(sqlString);
                    result2.next();
                    String drinkname = result2.getString(1);
                    double price = result2.getDouble(2);

                    // add name and price to drinkAndCost
                    if (drinkAndCost.containsKey(drinkname)) {
                        double c = drinkAndCost.get(drinkname);
                        drinkAndCost.put(drinkname, c + price);
                        int n = drinkAndNum.get(drinkname);
                        drinkAndNum.put(drinkname, n + 1);
                    } else {
                        drinkAndCost.put(drinkname, price);
                        drinkAndNum.put(drinkname, 1);
                    }
                }
            }

            for (String key : drinkAndCost.keySet()) {
                ArrayList<String> list = new ArrayList<>();
                list.add(key);
                list.add(Integer.toString(drinkAndNum.get(key)));
                list.add(String.format("%,.2f", drinkAndCost.get(key)));
                sales.add(list);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting filtered sales history");
        }
        return sales;

    }

}