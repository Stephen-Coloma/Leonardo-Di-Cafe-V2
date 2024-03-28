package server.model.rmiservices;

import server.model.ServerModel;
import shared.Customer;
import shared.rmiinterfaces.AccountManagement;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AccountManagementService extends UnicastRemoteObject implements AccountManagement {
    private ServerModel model;

    public AccountManagementService(ServerModel model) throws RemoteException {
        this.model = model;
    }

    public void changeDetails(String previousUsername, Customer updatedAccount) throws RemoteException, InvalidCredentialsException, AccountExistsException  {
        Optional<Customer> optionalCustomerAccount = model.getCustomerAccountList().stream()
                .filter(account -> account.getUsername().equals(previousUsername))
                .findFirst();

        if (optionalCustomerAccount.isPresent()) {
            boolean accountExists = model.getCustomerAccountList().stream()
                    .anyMatch(account -> account.getUsername().equals(updatedAccount.getUsername()) && !updatedAccount.getUsername().equals(previousUsername));

            if (accountExists) throw new AccountExistsException();

            Customer currentAccount = optionalCustomerAccount.get();
            currentAccount.setName(updatedAccount.getName());
            currentAccount.setUsername(updatedAccount.getUsername());
            currentAccount.setAddress(updatedAccount.getAddress());
            currentAccount.setEmail(updatedAccount.getEmail());
            currentAccount.setPassword(updatedAccount.getPassword());
        } else throw new InvalidCredentialsException();
    } // end of changeDetails

    public void deleteAccount(Customer account) throws RemoteException {
        List<Customer> accountList = model.getCustomerAccountList();

        for (Iterator<Customer> iterator = accountList.iterator(); iterator.hasNext();) {
            Customer currentAccount = iterator.next();
            if (currentAccount.getUsername().equals(account.getUsername())) {
                iterator.remove();
                break;
            }
        }
    } // end of deleteAccount
} // end of AccountManagementService class
