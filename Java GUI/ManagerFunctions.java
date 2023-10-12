import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

public class ManagerFunctions {
    private ArrayList<String> drinkNames;
    private double totalPrice;
    private Connection conn;

    public ManagerFunctions(Connection db_connection) {
        db_connection = conn;
    }

    public void updateRecipePrice(String drinkName, double price){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "UPDATE recipes SET price = " + price + " WHERE drinkname = " + drinkName + ";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        }
    }
    public void updateRecipeIngredient(String ingredientName, double ingredientValue, String drinkName){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "UPDATE recipes SET ingredient_names = ARRAY_APPEND(ingredient_names," + ingredientName + "), ingredient_values = ARRAY_APPEND(ingredient_values," + ingredientValue + ") WHERE drinkname =" + drinkName + ";";
                //also update inventory if not exist
                sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredientName + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        }
    }

    public void createNewRecipe(int drinkId, String drinkName, ArrayList<String> ingredient_names, ArrayList<Double> ingredient_values, double price){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "INSERT INTO recipes (recipeid, drinkname, ingredient_names, ingredient_values, price) VALUES (" + drinkId + ", " + drinkName + "," + ingredient_names + ","  + ingredient_values + "," +  price + ");";

            //determine which ingredients need to be created in inventory using Conflict 
            for (int i = 0; i < ingredient_names.size(); i++){
            sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredient_names.get(i) + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
            }
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        }
    }
    public void createNewEmployee(String employeeName, String password, double pay, double hours, boolean manager){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "INSERT INTO employee (name, password, pay, hours, manager) VALUES (" + employeeName + "," + password + "," + pay + "," + hours + "," + manager + ");";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        }
    }
    public void removeEmployee(String employeeName){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "DELETE FROM employee WHERE name =" + employeeName + ";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        }
    }

    //add amount to ingredient in inventory
    public void addInventory(String ingredient, float newAmount){
        try{
            //get original amount
            String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
            ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
            result2.next();
            float amount = result2.getFloat("amount");

            //add newAmount
            amount += newAmount;

            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient + ";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        } 
    }

    //remove amount of ingredient from inventory
    public void removeFromInventory(String ingredient, float reduceAmount){
        try{
            //get original amount
            String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
            ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
            result2.next();
            float amount = result2.getFloat("amount");

            //reduce amount by desired amount
            amount -= reduceAmount;

            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "UPDATE inventory SET amount = " + amount + " WHERE name = " + ingredient + ";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        } 
    }

    //add new ingredient to inventory
    public void createNewInventory(String ingredient, float capacity, String unit){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "INSERT INTO inventory (name, amount, capacity, unit, alert) VALUES (" + ingredient + ", 0," + capacity + "," + unit + ", TRUE) ON CONFLICT (name) DO NOTHING;";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        } 
    }

    //remove ingredient from inventory
    public void deleteInventory(String ingredient){
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "DELETE FROM inventory WHERE name =" + ingredient + ";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
        } 
        catch (Exception e){
            System.out.println("Error accessing Database.");
        } 
    }

}