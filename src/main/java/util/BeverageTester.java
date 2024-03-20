package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.Beverage;

public class BeverageTester {
    public static void main(String[] args) {
        File beverageMenuFile = new File("src/main/resources/data/beverage_menu.json");

       testSave(beverageMenuFile);

       testLoad(beverageMenuFile);
    }

    private static void testSave(File filepath) {
        System.out.println("Saving file .....");

        HashMap<String, Beverage> beverageMenu = new HashMap<>();

        Beverage beverage, beverage1, beverage2, beverage3;

        try {
            // Create beverages
            beverage = new Beverage("Midnight Mocha Iced Coffee", 'b', 4.5, 23, new Object[]{"Black Iced Coffee.png", ImageUtility.getImageBytes("Black Iced Coffee.png")}, "A refreshing blend of rich coffee with a hint of chocolate, perfect for any time of the day.", 15, 10, 5, 10.0, 15.0, 20.0);
            beverage1 = new Beverage("Matcha Frosted Iced Latte", 'b', 4.4, 12, new Object[]{"Black Iced Coffee.png", ImageUtility.getImageBytes("Black Iced Coffee.png")}, "A refreshing combination of matcha and frosted milk, served over ice for a delightful treat.", 15, 10, 5, 10.0, 15.0, 20.0);
            beverage2 = new Beverage("Mocha Madness Frappe", 'b', 4.9, 24, new Object[]{"Mocha Frappe.png", ImageUtility.getImageBytes("Mocha Frappe.png")}, "Experience the ultimate mocha indulgence with this rich and creamy frappe.", 15, 10, 5, 10.0, 15.0, 20.0);
            beverage3 = new Beverage("Okinawa Delight Milk Tea", 'b', 4.2, 32, new Object[]{"Okinawa Milktea.png", ImageUtility.getImageBytes("Okinawa Milktea.png")}, "Transport yourself to Okinawa with this delightful milk tea, boasting rich and aromatic flavors.", 15, 10, 5, 10.0, 15.0, 20.0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Adding beverages to the menu
        beverageMenu.put("Midnight Mocha Iced Coffee", beverage);
        beverageMenu.put("Matcha Frosted Iced Latte", beverage1);
        beverageMenu.put("Mocha Madness Frappe", beverage2);
        beverageMenu.put("Okinawa Delight Milk Tea", beverage3);

        // Serialize and save the beverage menu to a JSON file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(beverageMenu);

        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(json);
            System.out.println("Beverage menu saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testLoad(File file) {

        System.out.println("Testing loadBeverageMenu method:");
        Object beverageMenu;
        beverageMenu = JSONUtility.loadBeverageMenu(file);

        if (beverageMenu != null) {
            System.out.println("Beverage Menu loaded successfully:");
            System.out.println(beverageMenu);
        } else {
            System.out.println("Failed to load Beverage Menu.");
        }
    }
}
