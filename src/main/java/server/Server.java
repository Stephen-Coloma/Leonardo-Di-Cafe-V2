package server;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import server.controller.ServerController;
import server.model.rmiservices.AccountManagementService;
import server.model.rmiservices.AuthenticationService;
import server.model.ServerModel;
import server.model.rmiservices.CallbackManagementService;
import server.model.rmiservices.OrderManagementService;
import server.view.ServerView;
import shared.rmiinterfaces.AccountManagement;
import shared.rmiinterfaces.Authentication;
import shared.rmiinterfaces.CallbackManagement;
import shared.rmiinterfaces.OrderManagement;
import util.JSONUtility;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server extends Application {
    private static final int PORT = 2000;
    private ServerModel model;
    private ServerView view;

    public static void main(String[] args) {
        launch(args);
    } // end of main

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image(getClass().getResource("/images/server/server_app_logo.png").toExternalForm()));

        model = new ServerModel();
        view = new ServerView(stage);
        view.runInterface();

        ServerController adminController = new ServerController(model, view);
        adminController.initializeAdminInterface();

        //implement an action when the window is closed
        view.getStage().setOnCloseRequest(windowEvent -> {
            JSONUtility.saveFoodMenu(model.getFoodMenu());
            JSONUtility.saveBeverageMenu(model.getBeverageMenu());
            JSONUtility.saveOrderList(model.getOrderList());
            JSONUtility.saveCustomerAccounts(model.getCustomerAccountList());
        });

        // launch the server
        bindServices();

    } // end of start

    /**This method starts the server in the new thread. Further, instantiate the services it binds already to the registry */
    private void bindServices() {
        new Thread(() -> {
            try {
                //Remote object for Authentication
                Authentication authentication = new AuthenticationService(model);
                OrderManagement orderManagement = new OrderManagementService(model);
                AccountManagement accountManagement = new AccountManagementService(model);
                CallbackManagement callbackManagement = new CallbackManagementService(model);

                //TODO: Add remote objects here before adding to the registry

                //creating a registry
                Registry registry  = LocateRegistry.createRegistry(PORT);

                //binding the friendly name to the registry
                registry.bind("authentication", authentication);
                registry.bind("order management", orderManagement);
                registry.bind("account management", accountManagement);
                registry.bind("callback management", callbackManagement);

                System.out.println("Server bound services successfully");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                throw new RuntimeException(e);
            }

        }).start();
    } // end of bindServices
} // end of Server class
