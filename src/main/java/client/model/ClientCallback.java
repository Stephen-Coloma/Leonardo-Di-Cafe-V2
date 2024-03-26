package client.model;

import shared.Beverage;
import shared.Food;
import shared.callback.Broadcast;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ClientCallback extends UnicastRemoteObject implements Broadcast {

    private final ClientModel clientModel;

    public ClientCallback(ClientModel clientModel) throws RemoteException {
        this.clientModel = clientModel;
    }

    @Override
    public void updateMenu(HashMap<String, Food> foodMenu, HashMap<String, Beverage> beverageMenu) throws RemoteException {
        System.out.println("updated menu");
        clientModel.setFoodMenu(foodMenu);
        clientModel.setBeverageMenu(beverageMenu);
        clientModel.setMenuUpdate(true);
    } // end of updateMenu
} // end of ClientCallback class
