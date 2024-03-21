package server.model;

import server.controller.ServerController;
import shared.*;
import shared.callback.Broadcast;
import util.JSONUtility;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**Server Model class holds the data that will eventually be accessed by all the clients.
 * The idea is, when a client places an order,it will update the menu of this server model (e.g. by decrementing it)
 * and that the updated menu will be visible to other clients.
 * <p>
 * There is a predefined menu products for food, beverage and orders which will be loaded from xml files when the server model is initialized.
 */
public class ServerModel {
    private final List<ServerController> serverControllers = new ArrayList<>();
    private HashMap<String, Broadcast> clientCallbacks = new HashMap<>();
    private HashMap<String, Food> foodMenu; //Hashmap for faster searching
    private HashMap<String, Beverage> beverageMenu;
    private final List<Customer> customerAccountList;
    private List<Order> orderList; //List for listing only orders
    private final List<String> userLoggedIn;

    public void notifyClients() {
        try {
            for (Broadcast client : clientCallbacks.values()) {
                client.updateMenu(foodMenu, beverageMenu);
            }
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    public void registerServerController(ServerController controller) {
        serverControllers.add(controller);
    }

    public List<ServerController> getActiveServerControllers() {
        return serverControllers;
    }

    /**The constructor of the server model should load the predefined menu products*/
    public ServerModel() {
        foodMenu = JSONUtility.loadFoodMenu("src/main/resources/data/food_menu.json");
        beverageMenu = JSONUtility.loadBeverageMenu(new File("src/main/resources/data/beverage_menu.json"));
        customerAccountList = (List<Customer>) JSONUtility.loadCustomerAccounts("src/main/resources/data/customer_account_list.json");
        orderList = (List<Order>) JSONUtility.loadOrderList(new File("src/main/resources/data/order_list.json"));

        //set up
        userLoggedIn = new ArrayList<>();
    }

    public void setFoodMenu(HashMap<String, Food> foodMenu) {
        this.foodMenu = foodMenu;
    }

    public void setBeverageMenu(HashMap<String, Beverage> beverageMenu) {
        this.beverageMenu = beverageMenu;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public HashMap<String, Food> getFoodMenu() {
        return foodMenu;
    }

    public HashMap<String, Beverage> getBeverageMenu() {
        return beverageMenu;
    }

    public List<Customer> getCustomerAccountList() {
        return customerAccountList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public List<ServerController> getServerControllers() {
        return serverControllers;
    }

    public List<String> getUserLoggedIn() {
        return userLoggedIn;
    }

    public HashMap<String, Broadcast> getClientCallbacks() {
        return clientCallbacks;
    }
}