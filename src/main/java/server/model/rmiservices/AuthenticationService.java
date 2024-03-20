package server.model.rmiservices;

import server.model.ServerModel;
import shared.Beverage;
import shared.Customer;
import shared.Food;
import shared.rmiinterfaces.Authentication;
import util.exception.AccountAlreadyLoggedIn;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**This class implements the authentication interface which is used to communicate to the server specifically for signup, login and logout*/

public class AuthenticationService extends UnicastRemoteObject implements Authentication {
    private ServerModel serverModel;
    public AuthenticationService(ServerModel serverModel) throws RemoteException {
        super();
        this.serverModel = serverModel;
    }

    /**This method handles all the signup processes from the client.
     * @param customerSignUp new account that tries to create an account.
     *
     * Algorithm:
     * 1. Check the list of customers if the customer that that tries to sign up already exists.
     * 2. If exists, throws an exception  "Account exists"
     * 3. If not, create a new Customer out of a customer and add it to the list*/
    @Override
    public synchronized boolean signUp(Customer customerSignUp) throws RemoteException, AccountExistsException {
        String signUpUsername = customerSignUp.getUsername();

        for (Customer customerAccount: serverModel.getCustomerAccountList()) {
            if (customerAccount.getUsername().equals(signUpUsername)){
                throw new AccountExistsException("Account exists");
            }
        }

        Customer customer =  new Customer(customerSignUp); //this creates a customer that has orderHistory in it
        serverModel.getCustomerAccountList().add(customer);
        return true;
    }

    /**This method handles all the login processes from the client.
     * @param username username
     * @param password password
     * @return the objects to be loaded in client side
     * @throws Exception when login credentials is invalid
     */
    @Override
    public Object[] login(String username, String password) throws RemoteException, AccountAlreadyLoggedIn, InvalidCredentialsException{
        for (Customer customerAccount: serverModel.getCustomerAccountList()) {
            //account
            if (customerAccount.getUsername().equals(username) && customerAccount.getPassword().equals(password)){
                if (!serverModel.getUserLoggedIn().contains(String.valueOf(username.hashCode()))){
                    //adds the value to the user logged in
                    serverModel.getUserLoggedIn().add(String.valueOf(username.hashCode()));

                    HashMap<String, Food> clientFoodMenuToLoad = new HashMap<>(serverModel.getFoodMenu());
                    HashMap<String, Beverage> clientBeverageMenuToLoad = new HashMap<>(serverModel.getBeverageMenu());

                    //return to controller
                    return new Object[]{customerAccount, clientFoodMenuToLoad, clientBeverageMenuToLoad};
                }else {
                    throw new AccountAlreadyLoggedIn("Account already logged in");
                }
            }
        }
        throw new InvalidCredentialsException("Invalid credentials");
    }

    /**This removes the client from the server if the client logged out already*/
    @Override
    public void logout(String clientID) throws RemoteException {
        serverModel.getUserLoggedIn().remove(clientID);
    }

    /**GETTERS AND SETTERS*/
    public ServerModel getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }
}
