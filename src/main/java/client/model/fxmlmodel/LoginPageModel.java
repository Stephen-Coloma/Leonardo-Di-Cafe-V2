package client.model.fxmlmodel;


import shared.rmiinterfaces.Authentication;
import util.exception.AccountAlreadyLoggedIn;
import util.exception.InvalidCredentialsException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;

/*This model represents the model for login. The model will hold all the processes for the application*/
public class LoginPageModel {
    private String username;
    private String password;
    private Object[] serverResponse;
    private Registry registry;

    public LoginPageModel(Registry registry){
        this.registry = registry;
    }

    /**This method tries to make connection to the server
     * @throws ClassNotFoundException
     *
     * Exceptions will be handled by the LoginPage Controller*/
    public void authenticate(String username, String password) throws RuntimeException, IOException, ClassNotFoundException, NotBoundException, InvalidCredentialsException, AccountAlreadyLoggedIn {
        Authentication authentication = (Authentication) registry.lookup("authentication");
        serverResponse = authentication.login(username, password);
    }

    public Object[] getServerResponse() {
        return serverResponse;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}