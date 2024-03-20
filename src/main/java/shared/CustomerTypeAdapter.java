package shared;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerTypeAdapter implements JsonSerializer<Customer>, JsonDeserializer<Customer> {
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
