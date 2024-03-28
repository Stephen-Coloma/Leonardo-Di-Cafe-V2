package client.view.fxmlview;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.view.inventory.BeverageEditDetailsPopupView;

import java.io.IOException;
import java.util.Objects;

public class AccountDetailsView {
    @FXML
    private TextArea usernameTA;
    @FXML
    private TextArea nameTA;
    @FXML
    private TextArea addressTA;
    @FXML
    private TextArea emailTA;
    @FXML
    private TextArea passwordTA;
    @FXML
    private Button saveBT;
    @FXML
    private Button deleteAccountBT;
    @FXML
    private Button closeBT;
    private static Stage popupStage;

    public static <T> T loadAccountDetailsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(AccountDetailsView.class.getResource("/fxml/client/account_details.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(AccountDetailsView.class.getResource("/values/account_details.css")).toExternalForm());

            popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setFullScreen(false);
            popupStage.setScene(scene);
            popupStage.show();

            return loader.getController();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
    } // end of loadAccountDetailsPopup

    public void setUsernameTA(String text) {
        usernameTA.setText(text);
    }

    public void setNameTA(String text) {
        nameTA.setText(text);
    }

    public void setAddressTA(String text) {
        addressTA.setText(text);
    }

    public void setEmailTA(String text) {
        emailTA.setText(text);
    }

    public void setPasswordTA(String text) {
        passwordTA.setText(text);
    }

    public TextArea getUsernameTA() {
        return usernameTA;
    }

    public TextArea getNameTA() {
        return nameTA;
    }

    public TextArea getAddressTA() {
        return addressTA;
    }

    public TextArea getEmailTA() {
        return emailTA;
    }

    public TextArea getPasswordTA() {
        return passwordTA;
    }

    public Button getSaveBT() {
        return saveBT;
    }

    public Button getDeleteAccountBT() {
        return deleteAccountBT;
    }

    public Button getCloseBT() {
        return closeBT;
    }

    public Stage getPopupStage() {
        return popupStage;
    }
} // end of accountDetails class
