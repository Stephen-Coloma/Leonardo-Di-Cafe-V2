package shared.rmiinterfaces;

import shared.Order;
import shared.Product;
import util.exception.OutOfStockException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface OrderManagement extends Remote {
    Order checkout(Order order) throws RemoteException, OutOfStockException;
    void review(List<Product> ratedProducts) throws RemoteException;
    void logout(String clientId) throws RemoteException;
}
