package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.Food;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
}
