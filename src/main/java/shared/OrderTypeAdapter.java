package shared;

import com.google.gson.*;
import util.ImageUtility;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderTypeAdapter implements JsonSerializer<Order>, JsonDeserializer<Order> {
    @Override
    public JsonElement serialize(Order order, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject orderObject = new JsonObject();
        orderObject.addProperty("timeStamp", order.getTimeStamp());

        orderObject.addProperty("status", order.isStatus());
        orderObject.addProperty("ID", order.getID());
        double totalPrice =0.0;
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
                        for(Map.Entry<String, Double> prices : beverage.getSizePrice().entrySet()){
                            if((sizes.getKey().equalsIgnoreCase(prices.getKey()))){
                                productObject.addProperty("price", prices.getValue());
                                totalPrice+=prices.getValue();
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
