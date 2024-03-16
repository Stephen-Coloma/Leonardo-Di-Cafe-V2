package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.Beverage;
import shared.Food;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JSONUtility {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Map<String, Food> loadFoodMenu(String filePath) {
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

    public static void saveFoodMenu(Map<String, Food> foodMenu, String filePath) {
        for (String food: foodMenu.keySet()) {
            foodMenu.get(food).setImage(new Object[]{foodMenu.get(food).getImageName(), null});
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(foodMenu, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadBeverageMenu(File filePath) {

        Object beverageMenu;

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                builder.append(line);
            }

            Gson gson = new GsonBuilder().serializeNulls().create();

            String json = builder.toString();
            Type beverageMenuType = new TypeToken<HashMap<String, Beverage>>(){}.getType();
            HashMap<String, Beverage>beveragemenuList = gson.fromJson(json, beverageMenuType);

            beverageMenu = beveragemenuList;

            return beverageMenu;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    } // end of loadBeverageMenu

    public static void saveBeverageMenu(HashMap<String, Beverage> beverageMenu, File filepath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filepath)) {
            gson.toJson(beverageMenu, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Beverage menu saved successfully!");
    }// end of saveBeverageMenu
}
