package server.model.rmiservices;

import client.model.ClientCallback;
import server.model.ServerModel;
import shared.callback.Broadcast;
import shared.rmiinterfaces.CallbackManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackManagementService extends UnicastRemoteObject implements CallbackManagement {

    private ServerModel model;

    public CallbackManagementService(ServerModel model) throws RemoteException {
        this.model = model;
    }

    @Override
    public void addClientCallback(String clientID, Broadcast clientCallback) throws RemoteException {
        model.getClientCallbacks().put(clientID, clientCallback);
        System.out.println("Client ID:" + clientID + " [Connected]");
        System.out.println("[Connected Clients: " + model.getClientCallbacks().size() + "]");
    }

    @Override
    public void removeClientCallback(String clientID) throws RemoteException {
        model.getClientCallbacks().remove(clientID);
        System.out.println("Client ID:" + clientID + " [Disconnected]");
        System.out.println("[Connected Clients: " + model.getClientCallbacks().size() + "]");
    }
} // end of CallbackManagementService class
