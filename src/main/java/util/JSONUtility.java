package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import shared.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtility {
    private static final Gson defaultGson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static Gson customerLoaderAndSaver = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Customer.class, new CustomerTypeAdapter())
            .registerTypeAdapter(Order.class, new OrderTypeAdapter())
            .create();

    private static Gson orderLoaderAndSaver = new GsonBuilder()
            .registerTypeAdapter(Product.class, new ProductDeserializer())
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
            Type type = new TypeToken<HashMap<String, Food>>() {
            }.getType();
            HashMap<String, Food> foodMenu = defaultGson.fromJson(reader, type);

            for (String food : foodMenu.keySet()) {
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

        for (String food : foodMenu.keySet()) {
            foodMenu.get(food).setImage(new Object[]{foodMenu.get(food).getImageName(), null});
        }
        try (FileWriter writer = new FileWriter(file)) {
            defaultGson.toJson(foodMenu, writer);
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

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            Gson gson = new GsonBuilder().serializeNulls().create();

            String json = builder.toString();
            Type beverageMenuType = new TypeToken<HashMap<String, Beverage>>() {
            }.getType();
            HashMap<String, Beverage> beverageMenuList = gson.fromJson(json, beverageMenuType);

            beverageMenu = beverageMenuList;

            //setting the images after returning
            for (String beverage : beverageMenuList.keySet()) {
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

        for (String beverage : beverageMenu.keySet()) {
            beverageMenu.get(beverage).setImage(new Object[]{beverageMenu.get(beverage).getImageName(), null});
        }
        try (FileWriter writer = new FileWriter(file)) {
            defaultGson.toJson(beverageMenu, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Beverage menu saved successfully!");
    }// end of saveBeverageMenu


    /**
     * Loads a List of Order from Json file
     *
     * @param filepath the File object representing the path to the JSON file containing the beverage menu
     * @return List of Orders
     */
    public static List<Order> loadOrderList(File filepath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            Type type = new TypeToken<List<Order>>() {
            }.getType();
            return orderLoaderAndSaver.fromJson(builder.toString(), type);
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
            defaultGson.toJson(orderList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Customer> loadCustomerAccounts(String filePath) {

        List<Customer> customerList;
        try (FileReader fileReader = new FileReader(filePath)) {
            Type customerType = new TypeToken<List<Customer>>() {
            }.getType();
            customerList = customerLoaderAndSaver.fromJson(fileReader, customerType);
            System.out.println(customerList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return customerList;

    }

    public static void saveCustomerAccounts(List<Customer> customerAccountList) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/data/customer_account_list.json")) {
            customerLoaderAndSaver.toJson(customerAccountList, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public static class CustomerTypeAdapter implements JsonSerializer<Customer>, JsonDeserializer<Customer> {
        @Override
        public JsonElement serialize(Customer customer, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", customer.getName());
            jsonObject.addProperty("username", customer.getUsername());
            jsonObject.addProperty("address", customer.getAddress());
            jsonObject.addProperty("email", customer.getEmail());
            jsonObject.addProperty("password", customer.getPassword());

            // Serialize orderHistory
            JsonArray orderHistoryArray = new JsonArray();
            for (Order order : customer.getOrderHistory()) {
                orderHistoryArray.add(jsonSerializationContext.serialize(order));
            }
            jsonObject.add("orderHistory", orderHistoryArray);

            return jsonObject;
        }

        @Override
        public Customer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String username = jsonObject.get("username").getAsString();
            String address = jsonObject.get("address").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();

            // Deserialize orderHistory
            List<Order> orderHistory = new ArrayList<>();
            JsonArray orderHistoryArray = jsonObject.getAsJsonArray("orderHistory");
            for (JsonElement element : orderHistoryArray) {
                orderHistory.add(jsonDeserializationContext.deserialize(element, Order.class));
            }

            Customer customer = new Customer(name, username, address, email, password);
            customer.setOrderHistory(orderHistory);

            return customer;
        }
    }

    public static class OrderTypeAdapter implements JsonSerializer<Order>, JsonDeserializer<Order> {
        @Override
        public JsonElement serialize(Order order, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject orderObject = new JsonObject();
            orderObject.addProperty("timeStamp", order.getTimeStamp());

            orderObject.addProperty("status", order.isStatus());
            orderObject.addProperty("ID", order.getID());
            double totalPrice = 0.0;
            JsonArray orderHistoryArray = new JsonArray();
            for (Product product : order.getOrders()) {
                JsonObject productObject = new JsonObject();
                productObject.addProperty("productName", product.getName());
                productObject.addProperty("type", product.getType());
                productObject.addProperty("review", product.getReview());
                productObject.addProperty("reviewCount", product.getReviewCount());
                productObject.addProperty("description", product.getDescription());
                productObject.addProperty("amountSold", product.getAmountSold());
                productObject.addProperty("image", product.getImageName());
                if (product instanceof Food) {
                    Food food = (Food) product;
                    totalPrice += food.getPrice();
                    productObject.addProperty("quantity", food.getQuantity());
                    productObject.addProperty("price", food.getPrice());
                } else if (product instanceof Beverage) {
                    Beverage beverage = (Beverage) product;
                    for (Map.Entry<String, Integer> sizes : beverage.getSizeQuantity().entrySet()) {
                        if (sizes.getValue() != 0) {
                            productObject.addProperty("size", sizes.getKey());
                            productObject.addProperty("quantity", sizes.getValue());
                            for (Map.Entry<String, Double> prices : beverage.getSizePrice().entrySet()) {
                                if ((sizes.getKey().equalsIgnoreCase(prices.getKey()))) {
                                    productObject.addProperty("price", prices.getValue());
                                    totalPrice += prices.getValue();
                                }
                            }
                        }
                    }
                }
                orderHistoryArray.add(productObject);
            }
            orderObject.addProperty("totalPrice", totalPrice);
            orderObject.add("orders", orderHistoryArray);

            return orderObject;
        }

        @Override
        public Order deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject orderObject = jsonElement.getAsJsonObject();
            String timeStamp = orderObject.get("timeStamp").getAsString();
            double totalPrice = orderObject.get("totalPrice").getAsDouble();
            boolean status = orderObject.get("status").getAsBoolean();
            int id = orderObject.get("ID").getAsInt();

            List<Product> products = new ArrayList<>();
            JsonArray ordersArray = orderObject.getAsJsonArray("orders");
            for (JsonElement element : ordersArray) {
                JsonObject productObject = element.getAsJsonObject();
                String productName = productObject.get("productName").getAsString();
                char typeChar = productObject.get("type").getAsCharacter();
                double review = productObject.get("review").getAsDouble();
                int reviewCount = productObject.get("reviewCount").getAsInt();
                String description = productObject.get("description").getAsString();
                int amountSold = productObject.get("amountSold").getAsInt();
                String imageName = productObject.get("image").getAsString();
                Object[] image = new Object[0];
                try {
                    image = new Object[]{imageName, ImageUtility.getImageBytes(imageName)};
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Product product;
                if (typeChar == 'b') {
                    String size = productObject.get("size").getAsString();
                    int quantity = productObject.get("quantity").getAsInt();
                    double price = productObject.get("price").getAsDouble();
                    if (size.equalsIgnoreCase("small")) {
                        product = new Beverage(productName, typeChar, review, reviewCount, image, description, quantity, 0, 0, price, 0, 0);
                    } else if (size.equalsIgnoreCase("medium")) {
                        product = new Beverage(productName, typeChar, review, reviewCount, image, description, 0, quantity, 0, 0.0, price, 0);
                    } else {
                        product = new Beverage(productName, typeChar, review, reviewCount, image, description, 0, 0, quantity, 0.0, 0, price);
                    }
                } else {
                    int quantity = productObject.get("quantity").getAsInt();
                    double price = productObject.get("price").getAsDouble();
                    product = new Food(productName, typeChar, review, reviewCount, image, description, quantity, price);
                }
                product.setAmountSold(amountSold);
                products.add(product);
            }

            return new Order(products, timeStamp, totalPrice, status, id);
        }
    }
}
