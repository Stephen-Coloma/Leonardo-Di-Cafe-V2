package client.model.fxmlmodel;

import shared.Customer;
import shared.rmiinterfaces.Authentication;
import util.exception.AccountExistsException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;

/*This model represents the model for signup. The model will hold all the processes for the application*/
public class SignUpPageModel {
    private String username;
    private String address;
    private String email;
    private String password;
    private boolean serverResponse;
    private Registry registry;

    public SignUpPageModel(Registry registry) {
        this.registry = registry;
    }

    /**This method tries to make connection to the server
     * @throws ClassNotFoundException
     *
     * Exceptions will be handled by the LoginPage Controller*/
    public void authenticate(String fullName, String username, String address, String email, String password) throws RuntimeException, IOException, ClassNotFoundException, NotBoundException, AccountExistsException {
        Customer signingUpCustomer= new Customer(fullName, username, address, email, password);
        Authentication authentication = (Authentication) registry.lookup("authentication");
        serverResponse = authentication.signUp(signingUpCustomer);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getServerResponse() {
        return serverResponse;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}