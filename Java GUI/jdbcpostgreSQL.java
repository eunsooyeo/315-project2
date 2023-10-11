import java.sql.*;
//import javax.swing.JOptionPane;

/*
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */
public class jdbcpostgreSQL {
  public static void updateRecipePrice(String drinkName, double price){
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
  public static void updateRecipeIngredient(String ingredientName, double ingredientValue, String drinkName){
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //create an SQL statement
        String sqlStatement = "UPDATE recipes
            SET
                ingredient_names = ARRAY_APPEND(ingredient_names," + ingredientName + "),
                ingredient_values = ARRAY_APPEND(ingredient_values," + ingredientValue + ")
            WHERE drinkname =" drinkName + ";";
            //also update inventory if not exist
            sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) 
                VALUES (" + ingredientName + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
        //send statement to DBMS
        ResultSet result = stmt.executeQuery(sqlStatement);
      } 
      catch (Exception e){
      System.out.println("Error accessing Database.");
      }
  }

  public static void createNewRecipe(int drinkId, String drinkName, Vector<String> ingredient_names, Vector<double> ingredient_values, double price){
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //create an SQL statement
        String sqlStatement = "INSERT INTO recipes (recipeid, drinkname, ingredient_names, ingredient_values, price) 
            VALUES (" + drinkId + ", " + drinkName + "," + ingredient_names + ","  + ingredient_values + "," +  price ");";

        //determine which ingredients need to be created in inventory using Conflict 
        for (unsigned int i = 0; i < ingredient_names.size(); i++){
          sqlStatement += "INSERT INTO inventory (name, amount, capacity, unit, alert) 
                VALUES (" + ingredient_names.at(i) + ", 0, 1000, 'unit', TRUE) ON CONFLICT (name) DO NOTHING;";
        }
        //send statement to DBMS
        ResultSet result = stmt.executeQuery(sqlStatement);
      } 
      catch (Exception e){
      System.out.println("Error accessing Database.");
      }
  }
  public static void createNewEmployee(String employeeName, String password, double pay, double hours, bool manager){
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
  public static void removeEmployee(String employeeName){
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

  public static void main(String args[]) {
    //dbSetup hides my username and password
    dbSetup my = new dbSetup();
    //Building the connection
     Connection conn = null;
     try {
        //Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(
          "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_10r_db",
           my.user, my.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     System.out.println("Opened database successfully");
     String cus_lname = "";
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
       String sqlStatement = "SELECT cus_lname FROM customer";
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       System.out.println("Customer Last names from the Database.");
       System.out.println("______________________________________");
       while (result.next()) {
         System.out.println(result.getString("cus_lname"));
       }
   } catch (Exception e){
     System.out.println("Error accessing Database.");
   }
    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class
