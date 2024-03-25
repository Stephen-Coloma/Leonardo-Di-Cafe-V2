package client;

import client.controller.ClientController;
import client.model.ClientModel;
import client.view.ClientView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Client extends Application {
    public static String IP_ADDRESS;
    public static final int PORT = 2000;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        stage.getIcons().add(new Image(getClass().getResource("/images/client/client_app_logo.png").toExternalForm()));
        ClientModel model = new ClientModel();
        ClientView view = new ClientView(stage);
        view.runInterface();

        new ClientController(view);
    } // end of start
} // end of Client class
