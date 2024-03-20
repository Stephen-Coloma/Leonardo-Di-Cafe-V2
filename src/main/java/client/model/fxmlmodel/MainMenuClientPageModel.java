package client.model.fxmlmodel;

import client.model.ClientModel;
import shared.*;
import shared.callback.Broadcast;
import shared.rmiinterfaces.CallbackManagement;
import shared.rmiinterfaces.OrderManagement;
import util.exception.OutOfStockException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

public class MainMenuClientPageModel {
    //data to be accessed after login by the user
    private final ClientModel clientModel;
    private Registry registry;
    private OrderManagement orderManagement;
    private CallbackManagement callbackManagement;
//    private Socket socket;
//    private ObjectOutputStream out;
//    private ObjectInputStream in;

    public MainMenuClientPageModel(Object[] clientModelData, Registry registry) throws NotBoundException, RemoteException {
        //the serverResponse[2] responds Object[] {customer, foodMenu, beverageMenu}

        Customer customer = ((Customer) clientModelData[0]);
        HashMap<String, Food> foodMenu = (HashMap<String, Food>) clientModelData[1];
        HashMap<String, Beverage> beverageMenu = ((HashMap<String, Beverage>) clientModelData[2]);

        clientModel = new ClientModel(customer, foodMenu, beverageMenu);

        this.registry = registry;
        this.orderManagement = (OrderManagement) registry.lookup("order management");
        callbackManagement = (CallbackManagement) registry.lookup("callback management");
    }

    public Order processCheckout(String clientId, Order order) throws OutOfStockException, RemoteException {
        Order successfulOrder = orderManagement.checkout(order);
        return successfulOrder;
    }

    public void processReview(String clientId, List<Product> ratedProducts) throws RemoteException {
        orderManagement.review(ratedProducts);
    }

    public void processLogout(String clientId) throws RemoteException {
        orderManagement.logout(clientId);
        callbackManagement.removeClientCallback(clientId);
    }

    public void registerCallback(String clientID, Broadcast clientCallback) throws RemoteException {
        callbackManagement.addClientCallback(clientID, clientCallback);
    }

    public ClientModel getClientModel() {
        return clientModel;
    }



//    public void setSocket(Socket socket) {
//        this.socket = socket;
//    }
//
//    public ObjectOutputStream getOut() {
//        return out;
//    }
//
//    public void setOut(ObjectOutputStream out) {
//        this.out = out;
//    }
//
//    public ObjectInputStream getIn() {
//        return in;
//    }
//
//    public void setIn(ObjectInputStream in) {
//        this.in = in;
//    }


    public Registry getRegistry() {
        return registry;
    }
}