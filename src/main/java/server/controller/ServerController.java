package server.controller;

import javafx.application.Platform;
import server.model.MainMenuAdminModel;
import server.model.ServerModel;
import server.model.listeners.MainMenuAdminObserver;
import server.view.MainMenuAdminView;
import server.view.ServerView;
import shared.Customer;
import shared.Order;
import shared.Product;
import shared.callback.Broadcast;
import util.PushNotification;
import util.exception.AccountAlreadyLoggedIn;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class ServerController implements MainMenuAdminObserver {
    private final ServerModel model;
    private final ServerView view;
//    private Socket clientSocket;
//    private ObjectInputStream streamReader;
//    private ObjectOutputStream streamWriter;
    private MainMenuAdminModel mainMenuAdminModel;

    public ServerController(ServerModel model, ServerView view) {
        this.model = model;
        this. view = view;
    } // end of constructor

//    public void setClientSocket(Socket clientSocket) {
//        this.clientSocket = clientSocket;
//    } // end of setClientSocket

    private void setComponentActions() {
        Platform.runLater(() -> {
            mainMenuAdminModel = new MainMenuAdminModel();
            mainMenuAdminModel.setCustomerAccountList(model.getCustomerAccountList());
            mainMenuAdminModel.setOrderList(model.getOrderList());
            mainMenuAdminModel.setFoodMenu(model.getFoodMenu());
            mainMenuAdminModel.setBeverageMenu(model.getBeverageMenu());
            mainMenuAdminModel.registerObserver(this);
            MainMenuAdminView mainMenuAdminView = view.getLoader().getController();

            MainMenuAdminController mainMenuAdminController = new MainMenuAdminController(mainMenuAdminModel, mainMenuAdminView);
            mainMenuAdminController.start();
        });
    } // end of setComponentActions

//    public void run() {
//        try {
//            streamReader = new ObjectInputStream(clientSocket.getInputStream());
//            streamWriter = new ObjectOutputStream(clientSocket.getOutputStream());
//
//            model.registerServerController(this);
//
//            listenToClient();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    } // end of run

    public void initializeAdminInterface() {
        System.out.println("Obtained Main Menu Controller");
        setComponentActions();
        System.out.println("Successfully added actions");
    }

    @Override
    public void notifyMenuChanges(String code, boolean menuChanges) {
        if (menuChanges) {
            if ("STATUS_CHANGE".equals(code)) {
                model.setOrderList(mainMenuAdminModel.getOrderList());
            } else if ("INVENTORY_CHANGE".equals(code)) {
                model.setFoodMenu(mainMenuAdminModel.getFoodMenu());
                model.setBeverageMenu(mainMenuAdminModel.getBeverageMenu());
                PushNotification.toastSuccess("Inventory Status", "Updated inventory stocks and details");
            } else if ("NEW_FOOD_PRODUCT".equals(code)) {
                model.setFoodMenu(mainMenuAdminModel.getFoodMenu());
                PushNotification.toastSuccess("New Product", "Food added to the list");
            } else if ("NEW_BEVERAGE_PRODUCT".equals(code)) {
                model.setBeverageMenu(mainMenuAdminModel.getBeverageMenu());
                PushNotification.toastSuccess("New Product", "Beverage added to the list");
            }
            model.notifyClients();
        }
    } // end of notifyMenuChanges

//    public synchronized void sendData(String clientID, String code, Object data) {
//        Object[] response = {clientID, code, data};
//        try {
//            if (!clientSocket.isClosed()) {
//                streamWriter.writeObject(response);
//                streamWriter.flush();
//                streamWriter.reset();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    } // end of sendData

//    private void closeConnection() {
//        try {
//            streamReader.close();
//            streamWriter.close();
//            clientSocket.close();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//    }
} // end of ServerController
