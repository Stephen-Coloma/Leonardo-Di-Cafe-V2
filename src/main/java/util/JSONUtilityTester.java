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

            Food chocolateCharm = new Food("Chocolate Charm", 'f', 4.5, 19, new Object[]{"Chocolate Charm.png", ImageUtility.getImageBytes("Chocolate Charm.png")}, "Irresistible chocolate cake that charms the taste buds with every bite.", 19, 16.5, 130);
            Food boxOf6Cupcakes = new Food("Box of 6 Cupcakes", 'f', 4.6, 24, new Object[]{"Box Of 6 Cupcakes.jpg", ImageUtility.getImageBytes("Box Of 6 Cupcakes.jpg")}, "Enjoy a variety of flavors with this box containing 6 delectable cupcakes.", 100, 18.0, 100);
            Food chocolateChipCookie = new Food("Chocolate Chip Cookie", 'f', 4.2, 17, new Object[]{"Chocolate Chip Cookie.png", ImageUtility.getImageBytes("Chocolate Chip Cookie.png")}, "A classic favorite, loaded with delicious chocolate chips.", 15, 12.0, 100);
            Food vanillaBerryBlissCupcake = new Food("Vanilla Berry Bliss Cupcake", 'f', 4.6, 23, new Object[]{"Vanilla Cupcake With Blueberry Toppings.png", ImageUtility.getImageBytes("Vanilla Cupcake With Blueberry Toppings.png")}, "Experience pure joy with this vanilla cupcake topped with fresh blueberries and strawberries.", 17, 13.0, 110);
            Food blueberryBlissCookie = new Food("Blueberry Bliss Cookie", 'f', 4.2, 22, new Object[]{"Blueberry Cookie.png", ImageUtility.getImageBytes("Blueberry Cookie.png")}, "A delightful blend of blueberries and sweetness, baked to perfection.", 15, 12.0, 85);
            Food honeyGlazedHeavenCookie = new Food("Honey Glazed Heaven Cookie", 'f', 4.4, 18, new Object[]{"Honey Cookie.png", ImageUtility.getImageBytes("Honey Cookie.png")}, "Sweetness meets perfection in this divine honey-glazed cookie.", 100, 13.0, 16);
            Food chocolateOverloadCupcake = new Food("Chocolate Overload Cupcake", 'f', 4.9, 30, new Object[]{"Chocolate Overload Cupcake.png", ImageUtility.getImageBytes("Chocolate Overload Cupcake.png")}, "A chocolate lover's dream come true - an explosion of chocolate flavors in every bite.", 140, 14.0, 19);
            Food swayLoaf = new Food("Sway Loaf", 'f', 4.1, 11, new Object[]{"Sway Loaf.png", ImageUtility.getImageBytes("Sway Loaf.png")}, "Soft and fluffy bread that'll make your taste buds sway with joy.", 88, 12.5, 14);
            Food cheeseCharm = new Food("Cheese Charm", 'f', 4.2, 12, new Object[]{"Cheese Charm.png", ImageUtility.getImageBytes("Cheese Charm.png")}, "Artisan bread with a delightful blend of assorted cheeses.", 90, 12.0, 15);
            Food chocoliciousDelightCookie = new Food("Chocolicious Delight Cookie", 'f', 4.7, 25, new Object[]{"Chocolate Cookie.png", ImageUtility.getImageBytes("Chocolate Cookie.png")}, "A heavenly combination of rich chocolate, perfect for indulgence.", 110, 14.0, 18);
            Food lunaLoaf = new Food("Luna Loaf", 'f', 4.7, 20, new Object[]{"Luna Loaf.png", ImageUtility.getImageBytes("Luna Loaf.png")}, "A celestial blend of ingredients in this heavenly loaf of bread.", 105, 14.5, 19);
            Food cookiesAndCreamDream = new Food("Cookies and Cream Dream", 'f', 4.6, 20, new Object[]{"Cookies And Cream Cookie.png", ImageUtility.getImageBytes("Cookies And Cream Cookie.png")}, "Creamy, crunchy, and utterly delicious - a dream come true for cookie lovers.", 95, 13.0, 17);
            Food garlicDelight = new Food("Garlic Delight", 'f', 4.5, 18, new Object[]{"Garlic Delight.png", ImageUtility.getImageBytes("Garlic Delight.png")}, "Classic garlic-flavored bread, perfect for dipping or pairing with pasta.", 100, 13.0, 16);
            Food cranZest = new Food("Cran Zest", 'f', 4.0, 10, new Object[]{"Cran Zest.png", ImageUtility.getImageBytes("Cran Zest.png")}, "Whole grain bread with cranberries for a tangy twist.", 80, 11.0, 14);
            Food pistachioPleasureCupcake = new Food("Pistachio Pleasure Cupcake", 'f', 4.1, 17, new Object[]{"Pistachio Cupcake.png", ImageUtility.getImageBytes("Pistachio Cupcake.png")}, "Delight your taste buds with the unique flavor of pistachio in this delightful cupcake.", 70, 9.0, 13);
            Food cherryDelight = new Food("Cherry Delight", 'f', 4.3, 17, new Object[]{"Cherry Delight.png", ImageUtility.getImageBytes("Cherry Delight.png")}, "Light and fluffy cake filled with delicious cherry goodness.", 120, 18.0, 20);
            Food strawberryCrown = new Food("Strawberry Crown", 'f', 4.2, 14, new Object[]{"Strawberry Crown.png", ImageUtility.getImageBytes("Strawberry Crown.png")}, "Delicate cake crowned with fresh and juicy strawberries.", 110, 16.0, 18);
            Food buttercreamDreamCupcake = new Food("Buttercream Dream Cupcake", 'f', 4.5, 22, new Object[]{"Buttercream Cupcake.png", ImageUtility.getImageBytes("Buttercream Cupcake.png")}, "Indulge in the velvety smoothness of buttercream atop a moist cupcake base.", 90, 12.0, 16);
            Food boxOf12Cupcakes = new Food("Box of 12 Cupcakes", 'f', 4.8, 28, new Object[]{"Box Of 12 Cupcakes.jpg", ImageUtility.getImageBytes("Box Of 12 Cupcakes.jpg")}, "Double the joy with this box containing 12 mouthwatering cupcakes in various flavors.", 150, 30.0, 20);
            Food chocoChiffon = new Food("Choco Chiffon", 'f', 4.7, 22, new Object[]{"Choco Chiffon.png", ImageUtility.getImageBytes("Choco Chiffon.png")}, "Airy and moist chocolate chiffon cake that melts in your mouth.", 135, 17.5, 18);
            Food oatmealChipCrunchCookie = new Food("Oatmeal Chip Crunch Cookie", 'f', 4.3, 21, new Object[]{"Oatmeal Chip Cookie.png", ImageUtility.getImageBytes("Oatmeal Chip Cookie.png")}, "A hearty blend of oats and chocolate chips, providing a satisfying crunch in every bite.", 90, 12.0, 14);
            Food honeyHue = new Food("Honey Hue", 'f', 4.3, 15, new Object[]{"Honey Hue.png", ImageUtility.getImageBytes("Honey Hue.png")}, "Golden-brown bread drizzled with honey for a sweet and savory experience.", 95, 12.5, 17);
            Food mochaMarvel = new Food("Mocha Marvel", 'f', 4.4, 16, new Object[]{"Mocha Marvel.png", ImageUtility.getImageBytes("Mocha Marvel.png")}, "Marvelously delicious cake blending the flavors of chocolate and coffee.", 125, 17.0, 17);
            Food boxOf12Cookies = new Food("Box of 12 Cookies", 'f', 4.5, 18, new Object[]{"Box Of 12 Cookies.jpg", ImageUtility.getImageBytes("Box Of 12 Cookies.jpg")}, "A delightful assortment of 12 freshly baked cookies.", 120, 15.0, 20);
            Food oreoOverload = new Food("Oreo Overload", 'f', 4.9, 28, new Object[]{"Oreo Overload.png", ImageUtility.getImageBytes("Oreo Overload.png")}, "Indulgent cake loaded with layers of Oreo goodness.", 160, 21.0, 23);
            Food plainAndSimpleCookie = new Food("Plain and Simple Cookie", 'f', 4.0, 15, new Object[]{"Plain Cookie.png", ImageUtility.getImageBytes("Plain Cookie.png")}, "Sometimes, simplicity is key. Enjoy the classic flavor of a plain cookie done just right.", 80, 10.0, 13);
            Food redVelvetTemptationCookie = new Food("Red Velvet Temptation Cookie", 'f', 4.8, 27, new Object[]{"Red Velvet Cookie.png", ImageUtility.getImageBytes("Red Velvet Cookie.png")}, "Indulge in the decadent allure of red velvet, crafted into a delightful cookie.", 120, 15.0, 19);
            Food chocolateStrawberrySensationCupcake = new Food("Chocolate Strawberry Sensation Cupcake", 'f', 4.7, 26, new Object[]{"Chocolate Cupcake With Strawberry Frosting.png", ImageUtility.getImageBytes("Chocolate Cupcake With Strawberry Frosting.png")}, "A heavenly combination of rich chocolate cupcake, luscious strawberry frosting, and fresh strawberry topping.", 120, 15.0, 20);
            Food caramelStrawberryTemptationCupcake = new Food("Caramel Strawberry Temptation Cupcake", 'f', 4.4, 19, new Object[]{"Caramel Cupcake With Strawberry Topping.png", ImageUtility.getImageBytes("Caramel Cupcake With Strawberry Topping.png")}, "Experience bliss with caramel-frosted cupcake topped with succulent strawberries.", 80, 11.0, 14);
            Food chocolateBread = new Food("Chocolate Bread", 'f', 4.8, 25, new Object[]{"Chocolate Bread.png", ImageUtility.getImageBytes("Chocolate Bread.png")}, "Rich chocolate-infused bread with a hint of sweetness.", 110, 14.0, 18);
            Food latteLush = new Food("Latte Lush", 'f', 4.6, 21, new Object[]{"Latte Lush.png", ImageUtility.getImageBytes("Latte Lush.png")}, "Coffee-infused cake with a lush and creamy texture.", 140, 18.5, 20);
            Food nutNudge = new Food("Nut Nudge", 'f', 4.6, 16, new Object[]{"Nut Nudge.png", ImageUtility.getImageBytes("Nut Nudge.png")}, "Bread featuring a generous amount of assorted nuts for a delightful crunch.", 92, 13.5, 16);
            Food darkDelight = new Food("Dark Delight", 'f', 4.8, 25, new Object[]{"Dark Delight.png", ImageUtility.getImageBytes("Dark Delight.png")}, "Intensely rich and dark chocolate cake for the ultimate chocolate lover.", 145, 19.0, 21);
            Food caramelCascade = new Food("Caramel Cascade", 'f', 4.9, 30, new Object[]{"Caramel Cascade.png", ImageUtility.getImageBytes("Caramel Cascade.png")}, "Decadent caramel-flavored cake with a cascading drizzle.", 150, 20.0, 22);
            Food boxOf3Cupcakes = new Food("Box of 3 Cupcakes", 'f', 4.2, 18, new Object[]{"Box Of Three Cupcakes.jpg", ImageUtility.getImageBytes("Box Of Three Cupcakes.jpg")}, "A small treat to satisfy your cravings, this box contains 3 delicious cupcakes.", 50, 8.0, 12);
            Food multigrainMarvel = new Food("Multigrain Marvel", 'f', 4.4, 14, new Object[]{"Multigrain Marvel.png", ImageUtility.getImageBytes("Multigrain Marvel.png")}, "A nutritious mix of grains, seeds, and nuts in every slice.", 85, 11.5, 13);
            Food bananaBlissCupcake = new Food("Banana Bliss Cupcake", 'f', 4.3, 20, new Object[]{"Banana Cupcake.png", ImageUtility.getImageBytes("Banana Cupcake.png")}, "A delightful banana-flavored cupcake that melts in your mouth.", 75, 10.0, 15);


            // foodMenu.put(food.getName(), food);
            foodMenu.put(chocolateCharm.getName(), chocolateCharm);
            foodMenu.put(boxOf6Cupcakes.getName(), boxOf6Cupcakes);
            foodMenu.put(chocolateChipCookie.getName(), chocolateChipCookie);
            foodMenu.put(vanillaBerryBlissCupcake.getName(), vanillaBerryBlissCupcake);
            foodMenu.put(blueberryBlissCookie.getName(), blueberryBlissCookie);
            foodMenu.put(honeyGlazedHeavenCookie.getName(), honeyGlazedHeavenCookie);
            foodMenu.put(chocolateOverloadCupcake.getName(), chocolateOverloadCupcake);
            foodMenu.put(swayLoaf.getName(), swayLoaf);
            foodMenu.put(cheeseCharm.getName(), cheeseCharm);
            foodMenu.put(chocoliciousDelightCookie.getName(), chocoliciousDelightCookie);
            foodMenu.put(lunaLoaf.getName(), lunaLoaf);
            foodMenu.put(cookiesAndCreamDream.getName(), cookiesAndCreamDream);
            foodMenu.put(garlicDelight.getName(), garlicDelight);
            foodMenu.put(cranZest.getName(), cranZest);
            foodMenu.put(pistachioPleasureCupcake.getName(), pistachioPleasureCupcake);
            foodMenu.put(lunaLoaf.getName(), lunaLoaf);
            foodMenu.put(cookiesAndCreamDream.getName(), cookiesAndCreamDream);
            foodMenu.put(garlicDelight.getName(), garlicDelight);
            foodMenu.put(cranZest.getName(), cranZest);
            foodMenu.put(pistachioPleasureCupcake.getName(), pistachioPleasureCupcake);
            foodMenu.put(cherryDelight.getName(), cherryDelight);
            foodMenu.put(strawberryCrown.getName(), strawberryCrown);
            foodMenu.put(buttercreamDreamCupcake.getName(), buttercreamDreamCupcake);
            foodMenu.put(boxOf12Cupcakes.getName(), boxOf12Cupcakes);
            foodMenu.put(chocoChiffon.getName(), chocoChiffon);
            foodMenu.put(oatmealChipCrunchCookie.getName(), oatmealChipCrunchCookie);
            foodMenu.put(honeyHue.getName(), honeyHue);
            foodMenu.put(mochaMarvel.getName(), mochaMarvel);
            foodMenu.put(boxOf12Cookies.getName(), boxOf12Cookies);
            foodMenu.put(oreoOverload.getName(), oreoOverload);
            foodMenu.put(plainAndSimpleCookie.getName(), plainAndSimpleCookie);
            foodMenu.put(redVelvetTemptationCookie.getName(), redVelvetTemptationCookie);
            foodMenu.put(chocolateStrawberrySensationCupcake.getName(), chocolateStrawberrySensationCupcake);
            foodMenu.put(caramelStrawberryTemptationCupcake.getName(), caramelStrawberryTemptationCupcake);
            foodMenu.put(chocolateBread.getName(), chocolateBread);
            foodMenu.put(latteLush.getName(), latteLush);
            foodMenu.put(nutNudge.getName(), nutNudge);
            foodMenu.put(darkDelight.getName(), darkDelight);
            foodMenu.put(caramelCascade.getName(), caramelCascade);
            foodMenu.put(boxOf3Cupcakes.getName(), boxOf3Cupcakes);
            foodMenu.put(multigrainMarvel.getName(), multigrainMarvel);
            foodMenu.put(bananaBlissCupcake.getName(), bananaBlissCupcake);

            JSONUtility.saveFoodMenu(foodMenu, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Food> updatedFoodMenu = JSONUtility.loadFoodMenu(filePath);
    }
}