package shared.rmiinterfaces;

import shared.Customer;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountManagement extends Remote {
    void changeDetails(String previousUsername, Customer newAccount) throws RemoteException, InvalidCredentialsException, AccountExistsException;
    void deleteAccount(Customer customer) throws RemoteException;
} // end of AccountManagement interface
