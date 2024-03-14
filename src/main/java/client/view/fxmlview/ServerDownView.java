package client.view.fxmlview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerDownView {
    public static void showServerErrorUI() {
        try {
            Parent root = FXMLLoader.load(ServerDownView.class.getResource("/fxml/client/server_error.fxml"));
            Scene serverDownScene = new Scene(root);
            Stage popupStage = new Stage();
            popupStage.setScene(serverDownScene);
            popupStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    } // end of showServerErrorUI
} // end of ServerDownView class
