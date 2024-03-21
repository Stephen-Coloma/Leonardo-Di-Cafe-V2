package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import shared.Beverage;
import shared.Food;
import shared.Order;
import shared.Product;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class JSONUtility {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();


    /**
     * Loads a food menu from a JSON file.
     *
     * @param filePath the path to the JSON file containing the food menu
     * @return a HashMap representing the loaded food menu, where keys are food names and values are Food objects
     */
    public static HashMap<String, Food> loadFoodMenu(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<HashMap<String, Food>>() {}.getType();
            HashMap<String, Food> foodMenu= gson.fromJson(reader, type);

            for (String food: foodMenu.keySet()) {
                foodMenu.get(food).setImage(new Object[]{foodMenu.get(food).getImageName(), ImageUtility.getImageBytes(foodMenu.get(food).getImageName())});
            }

            return foodMenu;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves a food menu to a JSON file.
     *
     * @param foodMenu the food menu to be saved, represented as a HashMap where keys are food names and values are Food objects
     */
    public static void saveFoodMenu(HashMap<String, Food> foodMenu) {
        File file = new File("src/main/resources/data/food_menu.json");

        for (String food: foodMenu.keySet()) {
            foodMenu.get(food).setImage(new Object[]{foodMenu.get(food).getImageName(), null});
        }
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(foodMenu, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a beverage menu from a JSON file.
     *
     * @param filePath the File object representing the path to the JSON file containing the beverage menu
     * @return the loaded beverage menu, represented as a HashMap where keys are beverage names and values are Beverage objects
     */
    public static HashMap<String, Beverage> loadBeverageMenu(File filePath) {

        HashMap<String, Beverage> beverageMenu;

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                builder.append(line);
            }

            Gson gson = new GsonBuilder().serializeNulls().create();

            String json = builder.toString();
            Type beverageMenuType = new TypeToken<HashMap<String, Beverage>>(){}.getType();
            HashMap<String, Beverage> beverageMenuList = gson.fromJson(json, beverageMenuType);

            beverageMenu = beverageMenuList;

            //setting the images after returning
            for (String beverage: beverageMenuList.keySet()) {
                beverageMenuList.get(beverage).setImage(new Object[]{beverageMenuList.get(beverage).getImageName(), ImageUtility.getImageBytes(beverageMenuList.get(beverage).getImageName())});
            }

            return beverageMenu;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    } // end of loadBeverageMenu

    /**
     * Saves a beverage menu to a JSON file.
     *
     * @param beverageMenu the beverage menu to be saved, represented as a HashMap where keys are beverage names and values are Beverage objects
     */
    public static void saveBeverageMenu(HashMap<String, Beverage> beverageMenu) {
        File file = new File("src/main/resources/data/beverage_menu.json");

        for (String beverage: beverageMenu.keySet()) {
            beverageMenu.get(beverage).setImage(new Object[]{beverageMenu.get(beverage).getImageName(), null});
        }
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(beverageMenu, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Beverage menu saved successfully!");
    }// end of saveBeverageMenu


    /**
     * Loads a List of Order from Json file
     * @param filepath the File object representing the path to the JSON file containing the beverage menu
     * @return List of Orders
     */
    public static List<Order> loadOrderList(File filepath) {

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .setPrettyPrinting()
                .create();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            Type type = new TypeToken<List<Order>>() {
            }.getType();
            return gson1.fromJson(builder.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * saves a List of Orders to Json file
     *
     * @param orderList creates an empty list of Order Objects
     */
    public static void saveOrderList(List<Order> orderList) {
        File file = new File("src/main/resources/data/order_list.json");

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(orderList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper class to assist Gson with deserialization of abstract classes.
     * Since Gson cannot directly instantiate an abstract class, this method is used in the deserialization process
     */
    public static class ProductDeserializer implements JsonDeserializer<Product> {
        public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            // Check the 'type' field in JSON to determine the concrete subclass to instantiate
            JsonElement productTypeElement = jsonObject.get("type");
            if (productTypeElement != null && productTypeElement.isJsonPrimitive()) {
                String productType = productTypeElement.getAsString();
                switch (productType) {
                    case "f":
                        return context.deserialize(jsonObject, Food.class);
                    case "b":
                        return context.deserialize(jsonObject, Beverage.class);
                    // Add more cases for other concrete subclasses if needed
                    default:
                        throw new JsonParseException("Unknown product type: " + productType);
                }
            } else {
                throw new JsonParseException("'type' field is missing or not a valid JSON primitive");
            }
        }
    }


}
