package client.controller.login;

import client.controller.landingpage.LandingPageController;
import client.controller.mainmenu.MainMenuClientPageController;
import client.model.fxmlmodel.LoginPageModel;
import client.model.fxmlmodel.MainMenuClientPageModel;
import client.view.fxmlview.LoginPageView;
import client.view.fxmlview.ServerDownView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.exception.AccountAlreadyLoggedIn;
import util.exception.InvalidCredentialsException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.util.Objects;

public class LoginPageController {
    private final LoginPageView loginView;
    private final LoginPageModel loginModel;
    private MainMenuClientPageController mainMenu;
    private FXMLLoader loader;
    private Parent root;
    private Object[] serverResponse;

    public LoginPageController(LoginPageView loginView, LoginPageModel loginModel){
        this.loginView = loginView;
        this.loginModel = loginModel;

        //setting up action for login button
        this.loginView.setActionLoginButton((ActionEvent event)->{
            String username = loginView.getUsernameTextField().getText();
            String password = loginView.getPasswordField().getText();

            if (username.isEmpty() || password.isEmpty()){
                loginView.getNoticeLabel().setText("fill out all details");
                loginView.getNoticeLabel().setVisible(true);
            } else {
                loginView.getNoticeLabel().setVisible(false);
                try {
                    loginModel.authenticate(username, password); //call the model for authentication
                    serverResponse = loginModel.getServerResponse();

                    //parse the server response
                    loadMainMenu(serverResponse, event);

                } catch (IOException e) { //load a new popup when failed to connect to server
                    ServerDownView.showServerErrorUI();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InvalidCredentialsException e) {
                    this.loginView.getNoticeLabel().setText("wrong credentials");
                    this.loginView.getNoticeLabel().setVisible(true);
                    this.loginView.getUsernameTextField().clear();
                    this.loginView.getPasswordField().clear();
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (AccountAlreadyLoggedIn e) {
                    this.loginView.getNoticeLabel().setText("account already logged in");
                    this.loginView.getNoticeLabel().setVisible(true);
                    this.loginView.getUsernameTextField().clear();
                    this.loginView.getPasswordField().clear();
                }
            }
        });

        //setting up action for home button. the button will load the Landing Page
        this.loginView.setActionHomeButton((ActionEvent event) ->{
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
    }// end of LoginPageController

    /**This method parses the response from the server. If Login in successful, load the main menu client page*/
    private void loadMainMenu(Object[] serverResponse, ActionEvent event) throws IOException, NotBoundException {

        loader = new FXMLLoader(getClass().getResource("/fxml/client/main_menu_client_page.fxml"));
        root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/home_screen.css")).toExternalForm());

        //when loading the main menu, pass the clientModel received from the server
        mainMenu = new MainMenuClientPageController(new MainMenuClientPageModel(serverResponse, loginModel.getRegistry()), loader.getController());
        mainMenu.setPrimaryStage(stage);

        stage.setScene(scene);
        stage.show();
    } // end of loadMainMenu
} // end of LoginPageController class
