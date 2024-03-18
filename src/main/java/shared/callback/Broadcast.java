package shared.callback;

import shared.Beverage;
import shared.Food;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Broadcast extends Remote {
    void updateMenu(HashMap<String, Food> foodMenu, HashMap<String, Beverage> beverageMenu) throws RemoteException;
} // end of Broadcast interface
