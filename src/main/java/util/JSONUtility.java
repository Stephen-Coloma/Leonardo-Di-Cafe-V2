package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.Beverage;
import shared.Food;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

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
}
