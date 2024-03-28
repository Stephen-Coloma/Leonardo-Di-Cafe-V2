package client.controller.accountdetails;

import client.model.fxmlmodel.MainMenuClientPageModel;
import client.view.fxmlview.AccountDetailsView;
import client.view.fxmlview.MainMenuClientPageView;
import shared.Customer;
import util.PushNotification;
import util.exception.AccountExistsException;
import util.exception.InvalidCredentialsException;

import java.rmi.RemoteException;

public class AccountDetailsController {
    private MainMenuClientPageModel model;
    private AccountDetailsView view;
    private MainMenuClientPageView mainMenuView;

    public AccountDetailsController(MainMenuClientPageModel model, AccountDetailsView view, MainMenuClientPageView mainMenuView) {
        this.model = model;
        this.view = view;
        this.mainMenuView = mainMenuView;
    }

    public void start() {
        setupAccountInformation();
        setupButtons();
    }

    public void setupAccountInformation() {
        Customer currentAccount = model.getClientModel().getCustomer();

        view.setUsernameTA(currentAccount.getUsername());
        view.setNameTA(currentAccount.getName());
        view.setAddressTA(currentAccount.getAddress());
        view.setEmailTA(currentAccount.getEmail());
        view.setPasswordTA(currentAccount.getPassword());
    } // end of setupAccountInformation

    public void setupButtons() {
        view.getSaveBT().setOnAction(event -> {
            String previousUsername = model.getClientModel().getCustomer().getUsername();
            String username = view.getUsernameTA().getText();
            String name = view.getNameTA().getText();
            String address = view.getAddressTA().getText();
            String email = view.getEmailTA().getText();
            String password = view.getPasswordTA().getText();

            Customer updatedAccount = new Customer(name, username, address, email, password);

            try {
                model.changeAccountDetails(previousUsername, updatedAccount);
                PushNotification.toastSuccess("Account Change", "Account details has been updated");
                mainMenuView.getAccountNameLabel().setText(updatedAccount.getName());
            } catch (InvalidCredentialsException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (AccountExistsException e) {
                PushNotification.toastError("Account Change", "Failed to update account details, username already exists");
            }
        });

        view.getDeleteAccountBT().setOnAction(event -> {
            try {
                // TODO: after the delete button is pressed, close the popup and logout the account first before invoking the deleteAccount
                model.deleteAccount(model.getClientModel().getCustomer());
                view.getPopupStage().close();
                mainMenuView.getLogoutButton().fire();
            } catch (RemoteException remoteException) {
                throw new RuntimeException();
            }
        });

        view.getCloseBT().setOnAction(event -> {
            view.getPopupStage().close();
        });
    } // end of setupButtons
}
