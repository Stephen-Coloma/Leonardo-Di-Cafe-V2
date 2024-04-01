package client.controller.landingpage;

import client.Client;
import client.controller.ClientController;
import client.controller.login.LoginPageController;
import client.controller.signup.SignUpPageController;
import client.model.fxmlmodel.LoginPageModel;
import client.model.fxmlmodel.SignUpPageModel;
import client.view.fxmlview.LandingPageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PrimitiveIterator;

public class LandingPageController {
    private FXMLLoader loader;
    private Parent root;
    private LoginPageController loginPageController;
    private SignUpPageController signUpPageController;
    private Registry registry;

    /**Constructor*/
    public LandingPageController(LandingPageView view){
        try {
            registry = LocateRegistry.getRegistry(Client.IP_ADDRESS, Client.PORT);
        }catch (Exception e){
            e.printStackTrace();
        }

        //setting up action for login button. this action loads up the login view
        view.setActionLoginButton((ActionEvent event)->{
            //load first the view before getting its controller
            try {
                loader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = loader.load();

                loginPageController = new LoginPageController(loader.getController(), new LoginPageModel(registry));

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //setting up signup button listeners
        view.setActionSignUpButton((ActionEvent event)->{
            //load first the view before getting its controller
            try {
                loader = new FXMLLoader(getClass().getResource("/fxml/client/signup_page.fxml"));
                root = loader.load();

                signUpPageController = new SignUpPageController(loader.getController(), new SignUpPageModel(registry));

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
