package server.model.rmiservices;

import server.model.ServerModel;
import shared.*;
import shared.rmiinterfaces.OrderManagement;
import util.exception.OutOfStockException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class OrderManagementService extends UnicastRemoteObject implements OrderManagement {
    private ServerModel serverModel;
    public OrderManagementService(ServerModel serverModel) throws RemoteException {
        super();
        this.serverModel = serverModel;
    }

    @Override
    public Order checkout(Order order) throws RemoteException, OutOfStockException {
        if (!checkAvailability(order)) { //order that is not successful
            throw new OutOfStockException();
        };
        updateMenu(order);

        Order successfulOrder = new Order(order); //if it reaches here, means the order is successful
        System.out.println(successfulOrder.getTotalPrice()); // debug purposes

        // therefore add to orderList for the admin
        serverModel.getOrderList().add(successfulOrder);

        //then add the order in the orderList of the customer in the customerList
        for (Customer customer: serverModel.getCustomerAccountList()) {
            if (customer.getUsername().equals(order.getCustomer().getUsername())){
                customer.getOrderHistory().add(order); //add the successful order to the customerOrderHistory
            }
        }

        System.out.println(serverModel.getFoodMenu());
        serverModel.notifyClients();
        return successfulOrder;
    }

    /**This method checks the availability of products in the menu based on the customer's order.
     * @param order, client of the order
     * @throws Exception if the product menu is out of stock. */
    private synchronized boolean checkAvailability(Order order) {
        for (Product product: order.getOrders()) {
            if (product instanceof Food food){ //check first what type of product
                //cast it

                if (food.getQuantity() > serverModel.getFoodMenu().get(food.getName()).getQuantity()){
                    //checks if the order food quantity is greater than what is on the menu
                    System.out.println("out of stock");
                    return false;
                    //throw new OutOfStockException("Out of stock");
                }
            }else if (product instanceof Beverage beverage){

                //check if all variation
                //small = 10
                //medium = 2
                // large = 3
                for (String variation: beverage.getSizeQuantity().keySet()) {
                    int variationQuantity = beverage.getVariationQuantity(variation); //small = 10;
                    int variationAvailableOnMenu = serverModel.getBeverageMenu().get(beverage.getName()).getVariationQuantity(variation);
                    if (variationQuantity > variationAvailableOnMenu){
                        //throw new OutOfStockException("Out of stock");
                        System.out.println("out of stock");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**This method update the product in the menu there are available products. Synchronization is handled here already.
     * @throws Exception if the product in the menu is out of stock */
    private void updateMenu(Order order) throws OutOfStockException {
        for (Product product: order.getOrders()) {
            if (product instanceof Food food){
                //cast it
                int orderQuantity = food.getQuantity();

                Food productListed = serverModel.getFoodMenu().get(food.getName());

                //updates the menu
                productListed.updateQuantity(orderQuantity); // this throws an exception
            }else if (product instanceof Beverage beverage){

                Beverage productListed = serverModel.getBeverageMenu().get(beverage.getName());

                //for each variation of the beverage order
                //small = 10
                //medium = 2
                // large = 3
                for (String variation: beverage.getSizeQuantity().keySet()) {
                    productListed.updateQuantity(variation, beverage.getSizeQuantity().get(variation));
                }
            }
        }
    }

    /**This method updates the reviews for the products*/
    @Override
    public void review(List<Product> ratedProducts) throws RemoteException {
        for (Product product : ratedProducts) {
            String productName = product.getName();
            double review = product.getReview();
            if (product instanceof Food){
                serverModel.getFoodMenu().get(productName).updateReview(review);
            }else{
                serverModel.getBeverageMenu().get(productName).updateReview(review);
            }
        }
        serverModel.notifyClients();
    }

    /**This removes the client from the server if the client logged out already*/
    public void logout(String clientID){
        this.serverModel.getUserLoggedIn().remove(clientID);
    }
}
