package client.controller.signup;

import client.controller.landingpage.LandingPageController;
import client.controller.login.LoginPageController;
import client.model.fxmlmodel.LoginPageModel;
import client.model.fxmlmodel.SignUpPageModel;
import client.view.fxmlview.ServerDownView;
import client.view.fxmlview.SignUpPageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.exception.AccountExistsException;

import java.io.IOException;
import java.rmi.NotBoundException;

public class SignUpPageController {
    private final SignUpPageView signUpView;
    private final SignUpPageModel signUpModel;
    private FXMLLoader loader;
    private Parent root;
    private boolean serverResponse;

    public SignUpPageController(SignUpPageView signUpView, SignUpPageModel signUpModel){
        this.signUpView = signUpView;
        this.signUpModel = signUpModel;

        //setting up action for login button
        this.signUpView.setActionSignUpButton((ActionEvent event)->{
            String fullName = signUpView.getFullNameTextField().getText();
            String username = signUpView.getUserNameTextField().getText();
            String address = signUpView.getAddressTextField().getText();
            String email = signUpView.getEmailTextField().getText();
            String password = signUpView.getPasswordField().getText();

            if (fullName.isEmpty() || username.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()){
                signUpView.getNoticeLabel().setText("fill out all details");
                signUpView.getNoticeLabel().setVisible(true);
            } else if (!signUpView.getTermsAndServicesCheckBox().isSelected()) {
                signUpView.getNoticeLabel().setText("accept terms and policies");
                signUpView.getNoticeLabel().setVisible(true);
            } else {
                signUpView.getNoticeLabel().setVisible(false);
                try {
                    signUpModel.authenticate(fullName, username, address, email, password); //call the model for authentication
                    serverResponse = signUpModel.getServerResponse();

                    //parse the server response
                    loadLoginMenu(serverResponse, event);

                } catch (IOException e) { //load a new popup when failed to connect to server
                    ServerDownView.showServerErrorUI();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (AccountExistsException e) {
                    this.signUpView.getNoticeLabel().setText("account exists");
                    this.signUpView.getNoticeLabel().setVisible(true);
                    this.signUpView.getUserNameTextField().clear();
                    this.signUpView.getUserNameTextField().setPromptText("try another one");
                }
            }
        });

        //setting up action for home button. the button will load the Landing Page
        this.signUpView.setActionHomeButton((ActionEvent event) ->{
            //load the view before getting its controller
            try {
                loader = new FXMLLoader(getClass().getResource("/fxml/client/landing_page.fxml"));
                root = loader.load();

                new LandingPageController(loader.getController());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    /**This method parses the response from the server. If Login in successful, load the main menu client page*/
    private void loadLoginMenu(boolean isSuccessful, ActionEvent event) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
        root = loader.load();

        new LoginPageController(loader.getController(), new LoginPageModel(signUpModel.getRegistry()));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
} // end of SignUpPageController class
