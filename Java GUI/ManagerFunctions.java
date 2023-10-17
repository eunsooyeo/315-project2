import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;
/*
All functions that interact with database for the manager side of the POS system
@author Sarah Brasseaux
@author Eunsoo Yeo
@author Yuqian Cao
*/import java.util.stream.Collectors;

public class ManagerFunctions {
    private ArrayList<String> drinkNames;
    private double totalPrice;
    private Connection conn;

    /*
    @function ManagerFunctions constructor that initializes the database connection
    @param db_connection to use the existing database connection

    */
    public ManagerFunctions(Connection db_connection) {
        conn = db_connection;
    }
    /*
    @function Function to update the price for a recipe in the database when a manager edits it in the POS
    @param drinkName to know what recipe to access
    @param price the new price to change the recipe
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to update the ingredients for a recipe in the database when a manager edits it in the POS
    @param ingredientNames string of the new recipe ingredients
    @param ingredientValues string of the new recipe amounts
    @param drinkName string of the recipe name changing ingredients for
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to create a new recipe in the database when a manager enters it in the POS
    @param drinkId string of the new unique drink id
    @param drinkName string of the new recipe name
    @param ingredientNames  string array of the new recipe ingredients
    @param ingredientValues string array of the new recipe amounts
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to delete recipe in the database when a manager removes it in the POS
    @param drinkName string of the recipe name removing
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to create a new employee in the database when a manager enters it in the POS
    @param id string of the new employee id
    @param employeeName string of the new employee name
    @param password string of employe password
    @param pay string of the employee's pay
    @param hours string of the employee's hours working
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to remove an employee in the database when a manager enters it in the POS
    @param employeeName to know which employee removing
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function for number of employees in database
    @param none
    @return number of employees in database, or 0
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Get function for number of managers in database
    @param none
    @return number of managers in database, or 0
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function for employee names and if manager
    @param none
    @return map of employees with string of name and boolean of if manager
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function for an employee's information
    @param name string of the employee's name
    @return list of strings of the employee's information consisting of id, name, password, pay, hours, and manager status
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Function to update an employee in the database when a manager enters it in the POS
    @param employeeName to know which employee editing
    @param hours string of new hours
    @param password string of new password
    @param pay string of new pay
    @return none
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Function to update an ingredient in the inventory database when a manager enters it in the POS
    @param ingredient string of name to access
    @param amount string of new amount
    @param capacity string of new capacity
    @param unit string of new unit
    @return none
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Function to create new ingredient in the database when a manager enters it in the POS
    @param ingredient string of new ingredient name
    @param amount string of new ingredient amount
    @param capacity string of new capacity
    @param unit string of new unit
    @return none
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Function to remove an ingredient from inventory in the database when a manager enters it in the POS
    @param ingredient string of name to know what ingredient to remove
    @return none
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function to get number of recipes in database
    @param none
    @return number of drinks
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Get function to get the list of drink names in the recipe table
    @param none
    @return arraylist of strings of the drink names
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function to get list of recipes in table sorted by id
    @param none
    @return arraylist of strings of drinknames
    @throws when error updating the database using the SQL commands

    */
    public ArrayList<String> getAllSortedDrinks() {
        ArrayList<String> drinks = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT drinkname FROM recipes ORDER BY recipeid;";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                drinks.add(result.getString(1));
            }

        } catch (Exception e) {
            System.out.println("Error getting all sorted drink names. getAllSortedDrinks");
        }
        return drinks;
    }

    /*
    @function Get function to get drink info of selected drink from database
    @param name string of the drink name
    @return arraylist of strings of the drink information by column
    @throws when error updating the database using the SQL commands

    */
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

    /*
    @function Get function to get list of supply history from database
    @param none
    @return arraylist of strings of supply information
    @throws when error updating the database using the SQL commands

    */
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
    /*
    @function Get function to get unit of ingredient from database
    @param name string of the ingredient name
    @return string of the unit
    @throws when error updating the database using the SQL commands

    */
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
    // @function Get function to get filtered sales history from a start to end date
    // @param string beginningDate
    // @param string endDate
    // @return drinkname, numberOfOrders, totalPrice as a list for every drink
    // @throws when error updating the database using the SQL commands
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

        /*
    @function Checks if arraylist item exists in targetlist
    @param targetlist list of lists 
    @param item list of strings
    @return arraylist of strings of the drink information by column
    @throws when error updating the database using the SQL commands

    */
    private int containsList(ArrayList<ArrayList<String>> targetlist, ArrayList<String> item) {
        ArrayList<String> item2 = new ArrayList<>();
        item2.add(item.get(1));
        item2.add(item.get(0));
        for (int i = 0; i < targetlist.size(); i++) {
            if (targetlist.get(i).equals(item) || targetlist.get(i).equals(item2)) {
                return i;
            }
        }
        return -1;
    }

    /*
    @function Returns a treemap with key being an arraylist (of size 2) of the drinksIDs,
    with value being the number of its occurrence from a start to end date
    @param beginningDate string of start date
    @param endDate string of end date
    @return treemap of arraylist string, integer for each time 2 drinks are ordered together
    @throws when error updating the database using the SQL commands

    */
    public TreeMap<ArrayList<String>, Integer> getWhatSalesTogether(String beginningDate, String endDate) {
        ArrayList<String[]> list = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sqlString = "SELECT drink_id FROM orders WHERE date >= '" + beginningDate + "' AND date <= '"
                    + endDate + "';";
            ResultSet result = stmt.executeQuery(sqlString);
            while (result.next()) {
                String s = result.getString(1);
                s = s.substring(1, s.length() - 1);
                String[] sstr = s.split(",");
                list.add(sstr);
                // System.out.println(Arrays.toString(sstr));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting what sales together");
        }

        ArrayList<ArrayList<String>> recordName = new ArrayList<>();
        ArrayList<Integer[]> recordAmount = new ArrayList<>();
        for (String[] str : list) {
            if (str.length < 2)
                continue;
            for (int i = 0; i < str.length; i++) {
                for (int j = i + 1; j < str.length; j++) {
                    if (str[i].equals(str[j]))
                        continue;
                    ArrayList<String> tmpName = new ArrayList<>();
                    tmpName.add(str[i]);
                    tmpName.add(str[j]);
                    int index = containsList(recordName, tmpName);
                    if (index > -1) {
                        Integer[] integer = new Integer[2];
                        integer[0] = recordAmount.get(index)[0] + 1;
                        integer[1] = recordAmount.get(index)[1];
                        recordAmount.set(index, integer);
                    } else {
                        recordName.add(tmpName);
                        Integer[] integer = new Integer[2];
                        integer[0] = 1;
                        integer[1] = recordAmount.size();
                        recordAmount.add(integer);
                    }
                }

            }
        }

        Collections.sort(recordAmount, new Comparator<Integer[]>() {
            public int compare(Integer[] int1, Integer[] int2) {
                return -(int1[0] - int2[0]);
            }
        });
        TreeMap<ArrayList<String>, Integer> map = new TreeMap<>();

        for (Integer[] intarr : recordAmount) {
            map.put(recordName.get(intarr[1]), intarr[0]);
        }
        return map;
    }

    /*
    @function Get function to return arraylist of excess report for what items in inventory are in excess
    @param beginningDate string of start date
    @return arraylist of ingredients that are in excess from the beginning date to present
    @throws when error updating the database using the SQL commands

    */
    public ArrayList<String> getExcessReport(String beginningDate) {
        // set endDate to be current date
        String endDate = java.time.LocalDate.now().toString();
        double numDrinks = 0.0;
        TreeMap<String, Double> currItemAndAmount = new TreeMap<>();
        TreeMap<String, Double> salesItemAndAmount = new TreeMap<>();
        ArrayList<String> excessItems = new ArrayList<>();

        try {
            // call the inventory
            Statement stmt = conn.createStatement();
            String callInventory = "SELECT name, amount FROM inventory";
            ResultSet result = stmt.executeQuery(callInventory);

            // loop through each row in the inventory
            while (result.next()) {
                String item = result.getString(1);
                Double amount = result.getDouble(2);

                // populate map with all the ingredients and its current amount
                currItemAndAmount.put(item, amount);
                // populate map with items and initialize to 0
                salesItemAndAmount.put(item, 0.0);
            }

            String getOrders = "SELECT drink_id FROM orders WHERE date >= '" + beginningDate + "' AND date <= '"
                    + endDate + "';";
            result = stmt.executeQuery(getOrders);

            // loop through all the orders within the time frame
            while (result.next()) {
                String tmp = result.getString(1);
                String[] drinkIDs = tmp.substring(1, tmp.length() - 1).split(",");

                // loop through each of the drinks/recipes
                for (String s : drinkIDs) {
                    ++numDrinks;
                    //for each recipe get the ingredients and their amounts
                    // reference the recipe database, get name and price
                    String sqlString = "SELECT ingredient_names, ingredient_values FROM recipes WHERE recipeid = "
                            + Integer.parseInt(s) + ";";
                    Statement stmt2 = conn.createStatement();
                    ResultSet result2 = stmt2.executeQuery(sqlString);
                    result2.next();
                    tmp = result2.getString(1);
                    String[] ingredient_names = tmp.substring(1, tmp.length() - 1).split(",");
                    tmp = result2.getString(2);
                    String[] ingredient_values = tmp.substring(1, tmp.length() - 1).split(",");

                    for (int i = 0; i < ingredient_names.length; ++i) {
                        if(ingredient_names[i].contains("\"")) {
                            ingredient_names[i] = ingredient_names[i].substring(1, ingredient_names[i].length() - 1);
                        }
                        double currAmount = salesItemAndAmount.get(ingredient_names[i]);
                        double newAmount = currAmount + Double.parseDouble(ingredient_values[i]);
                        salesItemAndAmount.replace(ingredient_names[i], newAmount);
                    }
                }
            }

            //account for cups, napkins, straws, and plastic cover
            List<String> items = Arrays.asList("cups", "straws", "plastic cover", "napkins");
            for(String item: items) {
                salesItemAndAmount.replace(item, numDrinks);
            }

            //account for cups, napkins, straws, and plastic cover
            List<String> items = Arrays.asList("cups", "straws", "plastic cover", "napkins");
            for(String item: items) {
                salesItemAndAmount.replace(item, numDrinks);
            }

            // loop through the ingredients array
            for (String key : currItemAndAmount.keySet()) {
                double salesAmount = salesItemAndAmount.get(key);
                double totalAmount = currItemAndAmount.get(key) + salesAmount;

                //compare to the current amount array and determine if it's 10%
                if (salesAmount< (totalAmount * 0.1)) {
                    excessItems.add(key);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error getting excess report");
        }

        return excessItems;
    }

}