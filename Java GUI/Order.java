import java.math.BigDecimal;
import java.sql.*;
import javax.naming.spi.DirStateFactory.Result;
import java.util.*;

public class Order {
    private ArrayList<String> drinkNames;
    private ArrayList<Float> prices;
    public static Connection conn;

    public Order(Connection db_connection) {
        conn = db_connection;
    }
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
    public boolean makeOrder() {
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

                // Array ingredientsArr = result.getArray("ingredient_names");
                // String[] ingredients = (String[]) ingredientsArr.getArray();
                // Array ingredientAmountArr = result.getArray("ingredient_values");
                // BigDecimal[] ingredientAmount = (BigDecimal[]) ingredientAmountArr.getArray();
                // float price = result.getFloat("price");

                // make subtractions from inventory database
                // for (int i = 0; i < ingredients.length; i++) {
                //     String ingredient = ingredients[i].toLowerCase();
                //     String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
                //     ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
                //     result2.next();
                //     float amount = result2.getFloat("amount");
                //     if (ingredient.equals("brown sugar") || ingredient.equals("fructose")
                //             || ingredient.equals("honey") || ingredient.equals("white sugar")) {
                //         amount -= (ingredientAmount[i]).intValue() * sugarLevel.get(index) / 100;
                //     }
                //     amount -= (ingredientAmount[i]).intValue();
                //     if (amount < 0) {
                //         return false;
                //     }
                //     String updateIngredientAmount = "UPDATE inventory SET amount = " + amount + " WHERE lower(name) = '"
                //             + ingredient + "';";
                //     conn.createStatement().executeUpdate(updateIngredientAmount);
                // }
                // update ice
                // String getIceAmount = "SELECT amount FROM inventory WHERE name = 'Ice';";
                // result = conn.createStatement().executeQuery(getIceAmount);
                // result.next();
                // String updateIceAmount = "UPDATE inventory SET amount = "
                //         + (result.getInt("amount") - iceLevel.get(index) / 10)
                //         + " WHERE name = 'Ice';";
                // conn.createStatement().executeUpdate(updateIceAmount);

                drinkIDs[index] = recipeID;
                totalPrice += prices.get(index);

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


    public boolean updateInventory(String drinkName, String iceLevel, String sugarLevel, ArrayList<String> toppings) {
        //check the ingredients available
        // find recipe of drinkName from recipe database
        int drinkID;
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

            //first determine sugar level
            Integer sugarLevelInt = 100;
            if (sugarLevel == "0% sugar"){
                sugarLevelInt = 0;
            }
            else if (sugarLevel == "30% sugar"){
                sugarLevelInt = 30;
            }
            else if (sugarLevel == "50% sugar"){
                sugarLevelInt = 50;
            }
            else if (sugarLevel == "80% sugar"){
                sugarLevelInt = 80;
            }

            // make subtractions from inventory database
            for (int i = 0; i < ingredients.length; i++) {
                String ingredient = ingredients[i].toLowerCase();
                String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
                ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
                result2.next();
                float amount = result2.getFloat("amount");
                if (ingredient.equals("brown sugar") || ingredient.equals("fructose")
                        || ingredient.equals("honey") || ingredient.equals("white sugar")) {
                    amount -= (ingredientAmount[i]).floatValue() * sugarLevelInt / 100.0;
                }
                amount -= (ingredientAmount[i]).floatValue();
                if (amount < 0) {
                    return false;
                }
                String updateIngredientAmount = "UPDATE inventory SET amount = " + amount + " WHERE lower(name) = '"+ ingredient + "';";
                conn.createStatement().executeUpdate(updateIngredientAmount);
            }
            //determine ice level
            Integer iceLevelInt = 0;
            if (iceLevel.equals("Regular Ice")){
                iceLevelInt = 100;
            }
            else if (iceLevel.equals("Light Ice")){
                iceLevelInt = 50;
            }
            else if (iceLevel.equals("Extra Ice")){
                iceLevelInt = 150;
            }

            // update ice
            String getIceAmount = "SELECT amount FROM inventory WHERE name = 'Ice';";
            result = conn.createStatement().executeQuery(getIceAmount);
            result.next();
            float amount = result.getInt("amount");
            amount -= iceLevelInt /10;
            if (amount < 0) {
                return false;
            }
            String updateIceAmount = "UPDATE inventory SET amount = "
                    + (result.getInt("amount") - iceLevelInt / 10)
                    + " WHERE name = 'Ice';";
            conn.createStatement().executeUpdate(updateIceAmount);

            //now check the toppings
            // update topping, each topping is +10 in inventory
            for (int index = 0; index < toppings.size(); index++) {
                String topping = toppings.get(index).trim().toLowerCase();
                String getToppingAmount = "SELECT amount FROM inventory WHERE lower(name) = '" + topping + "';";
                try {
                    result = conn.createStatement().executeQuery(getToppingAmount);
                    if(!result.next()) {
                        continue;
                    }
                    amount = result.getFloat("amount");
                    amount -= 10;
                
                    String updateToppingAmount = "UPDATE inventory SET amount = "
                            + amount
                            + " WHERE lower(name) = '" + topping + "';";
                    conn.createStatement().executeUpdate(updateToppingAmount);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    return false;
                }
            }

            //add back Cups, Straws, Plastic Cover, Napkins
            List<String> items = Arrays.asList("Cups", "Straws", "Plastic Cover", "Napkins");
            for(int index = 0; index < items.size(); index++) {
                String item = items.get(index);
                String getItemAmount = "SELECT amount FROM inventory WHERE name = '" + item + "';";

                try {
                    result = conn.createStatement().executeQuery(getItemAmount);
                    if(!result.next()) {
                        continue;
                    }
                    amount = result.getFloat("amount");
                    amount -= 1;
                
                    String updateItemAmount = "UPDATE inventory SET amount = "
                            + amount
                            + " WHERE name = '" + item + "';";
                    conn.createStatement().executeUpdate(updateItemAmount);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    return false;
                }
            }

            drinkNames.add(drinkName);
            prices.add(price);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean restoreInventory(String drinkInfo) {
        //parse drink info
        String[] tokens = drinkInfo.split("\n");

        String drinkName = (tokens[0]).trim();
        String iceLevel = (tokens[1]).trim();
        String sugarLevel = (tokens[2]).trim();
        String[] toppingString = (tokens[3]).split(",");
        
        
        ArrayList<String> toppings = new ArrayList<String>(Arrays.asList(toppingString));

        //check the ingredients available
        // find recipe of drinkName from recipe database
        int drinkID;
        try {
            String queryString = "SELECT * FROM recipes WHERE lower(drinkname) = '" + drinkName.toLowerCase()
                    + "'";
            ResultSet result = conn.createStatement().executeQuery(queryString);
            result.next();

            int recipeID = result.getInt("recipeid");

            Array ingredientsArr = result.getArray("ingredient_names");
            String[] ingredients = (String[]) ingredientsArr.getArray();
            Array ingredientAmountArr = result.getArray("ingredient_values");
            BigDecimal[] ingredientAmount = (BigDecimal[]) ingredientAmountArr.getArray();
            float price = result.getFloat("price");

            //first determine sugar level
            Integer sugarLevelInt = 100;
            if (sugarLevel == "0% sugar"){
                sugarLevelInt = 0;
            }
            else if (sugarLevel == "30% sugar"){
                sugarLevelInt = 30;
            }
            else if (sugarLevel == "50% sugar"){
                sugarLevelInt = 50;
            }
            else if (sugarLevel == "80% sugar"){
                sugarLevelInt = 80;
            }

            // make additions from inventory database
            for (int i = 0; i < ingredients.length; i++) {
                String ingredient = ingredients[i].toLowerCase();
                String getIngredientAmount = "SELECT * FROM inventory WHERE lower(name) = '" + ingredient + "';";
                ResultSet result2 = conn.createStatement().executeQuery(getIngredientAmount);
                result2.next();
                float amount = result2.getFloat("amount");
                if (ingredient.equals("brown sugar") || ingredient.equals("fructose")
                        || ingredient.equals("honey") || ingredient.equals("white sugar")) {
                    amount += (ingredientAmount[i]).floatValue() * sugarLevelInt / 100.0;
                }
                amount += (ingredientAmount[i]).floatValue();
    
                String updateIngredientAmount = "UPDATE inventory SET amount = " + amount + " WHERE lower(name) = '"+ ingredient + "';";
                conn.createStatement().executeUpdate(updateIngredientAmount);
            }
            //determine ice level
            Integer iceLevelInt = 0;
            if (iceLevel.equals("Regular Ice")){
                iceLevelInt = 100;
            }
            else if (iceLevel.equals("Light Ice")){
                iceLevelInt = 50;
            }
            else if (iceLevel.equals("Extra Ice")){
                iceLevelInt = 150;
            }

            // update ice
            String getIceAmount = "SELECT amount FROM inventory WHERE name = 'Ice';";
            result = conn.createStatement().executeQuery(getIceAmount);
            result.next();
            float amount = result.getInt("amount");
            amount += iceLevelInt / 10;
        
            String updateIceAmount = "UPDATE inventory SET amount = "
                    + amount
                    + " WHERE name = 'Ice';";
            conn.createStatement().executeUpdate(updateIceAmount);

            //now check the toppings
            // update topping, each topping is +10 in inventory
            for (int index = 0; index < toppings.size(); index++) {
                String topping = toppings.get(index).trim().toLowerCase();
                String getToppingAmount = "SELECT amount FROM inventory WHERE lower(name) = '" + topping + "';";
                try {
                    result = conn.createStatement().executeQuery(getToppingAmount);
                    if(!result.next()) {
                        continue;
                    }
                    amount = result.getFloat("amount");
                    amount += 10;
                
                    String updateToppingAmount = "UPDATE inventory SET amount = "
                            + amount
                            + " WHERE lower(name) = '" + topping + "';";
                    conn.createStatement().executeUpdate(updateToppingAmount);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    return false;
                }
            }

            //add back Cups, Straws, Plastic Cover, Napkins
            List<String> items = Arrays.asList("Cups", "Straws", "Plastic Cover", "Napkins");
            for(int index = 0; index < items.size(); index++) {
                String item = items.get(index);
                String getItemAmount = "SELECT amount FROM inventory WHERE name = '" + item + "';";

                try {
                    result = conn.createStatement().executeQuery(getItemAmount);
                    if(!result.next()) {
                        continue;
                    }
                    amount = result.getFloat("amount");
                    amount += 1;
                
                    String updateItemAmount = "UPDATE inventory SET amount = "
                            + amount
                            + " WHERE name = '" + item + "';";
                    conn.createStatement().executeUpdate(updateItemAmount);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    return false;
                }
            }
        
            drinkNames.remove(drinkName);
            prices.remove(price);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }
}