package util;

import shared.Food;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONUtilityTester {
    public static void main(String[] args) {
        String filePath = "src/main/resources/data/food_menu.json";

        Map<String, Food> foodMenu = new HashMap<>();
        try {

            Food food = new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 190);
            Food food1 = new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 190);
            Food food2= new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 190);
            Food food3 = new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 190);
            Food food4 = new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 190);

            foodMenu.put(food.getName(), food);

            JSONUtility.saveFoodMenu(foodMenu, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Food> updatedFoodMenu = JSONUtility.loadFoodMenu(filePath);
    }
}