package shared.rmiinterfaces;

import client.Client;
import client.model.ClientCallback;
import shared.callback.Broadcast;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackManagement extends Remote {
    void addClientCallback(String clientID, Broadcast clientCallback) throws RemoteException;
    void removeClientCallback(String clientID) throws RemoteException;
} // end of CallbackManagement interface
