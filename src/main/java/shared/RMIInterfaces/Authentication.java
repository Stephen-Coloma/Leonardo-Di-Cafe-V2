package shared.RMIInterfaces;
import shared.Customer;
import util.exception.AccountAlreadyLoggedIn;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Authentication extends Remote{
    boolean signUp(Customer customerSignUp) throws RemoteException, AccountExistsException;
    Object[] login(String username, String password) throws RemoteException, AccountAlreadyLoggedIn, InvalidCredentialsException;
    void logout(String clientID) throws RemoteException;
}
