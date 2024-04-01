package client.controller.mainmenu;

import client.controller.accountdetails.AccountDetailsController;
import client.controller.checkout.CartItemCardController;
import client.controller.checkout.CheckoutPageController;
import client.controller.login.LoginPageController;
import client.controller.orderhistory.OrderHistoryPageController;
import client.model.ClientCallback;
import client.model.fxmlmodel.*;
import client.view.fxmlview.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.*;
import util.ImageUtility;
import util.LoadingScreenUtility;
import util.PushNotification;
import util.exception.OutOfStockException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainMenuClientPageController {
    private Stage primaryStage;
    private List<Node> allProductCardNodes = new ArrayList<>();
    private List<Node> foodCardNodes = new ArrayList<>();
    private List<Node> beverageCardNodes = new ArrayList<>();
    private final MainMenuClientPageView mainMenuView;
    private char currentLoadedMenu = 'f';
    private final MainMenuClientPageModel mainMenuModel;
    private LoginPageController loginPageController;
    private FXMLLoader loader;
    private Parent root;
    private int cartColumn = 0; //for cart scrollPane
    private int cartRow = 1; //for cart scrollPane
    private double cartTotalPrice = 0; //for cart purposes
    private static final long DEBOUNCE_DELAY = 500;
    private Timer debounceTimer;

    public MainMenuClientPageController(MainMenuClientPageModel mainMenuModel, MainMenuClientPageView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.mainMenuModel = mainMenuModel;

        registerClientCallback();

        // setting up the time and date labels
        setupClock();
        setupDate();

        //initialize the account name
        String accountName = this.mainMenuModel.getClientModel().getCustomer().getName();
        this.mainMenuView.getAccountNameLabel().setText(accountName);

        // start a task to update the latest product changes of the FlowPane from the server
        listenToProductUpdates();

        // initial menu
        currentLoadedMenu = 'a';
        loadMenu(1, "All Products");

        setComponentActions();

        debounceTimer = new Timer();
    }

    private void listenToProductUpdates() {
        for (Food food: mainMenuModel.getClientModel().getFoodMenu().values()) {
            foodCardNodes.add(createProductCard(food));
        }

        for (Beverage beverage: mainMenuModel.getClientModel().getBeverageMenu().values()) {
            beverageCardNodes.add(createProductCard(beverage));
        }

        allProductCardNodes.addAll(foodCardNodes);
        allProductCardNodes.addAll(beverageCardNodes);

        Task<Void> productUpdateTask = new Task<>() {
            @Override
            protected Void call() {
                while (true) {
                    try {
                        Thread.sleep(1000);

                        if (mainMenuModel.getClientModel().getMenuUpdate()) {
                            System.out.println("Updating FlowPane to the Latest Product Changes");

                            foodCardNodes = new ArrayList<>();
                            beverageCardNodes = new ArrayList<>();
                            allProductCardNodes = new ArrayList<>();

                            for (Food food : mainMenuModel.getClientModel().getFoodMenu().values()) {
                                foodCardNodes.add(createProductCard(food));
                            }

                            for (Beverage beverage : mainMenuModel.getClientModel().getBeverageMenu().values()) {
                                beverageCardNodes.add(createProductCard(beverage));
                            }

                            allProductCardNodes.addAll(foodCardNodes);
                            allProductCardNodes.addAll(beverageCardNodes);
                            mainMenuModel.getClientModel().setMenuUpdate(false);
                        }
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        Thread updateThread = new Thread(productUpdateTask);
        updateThread.setDaemon(true);
        updateThread.start();
    } // end of listenToProductUpdates

    private void registerClientCallback() {
        try {
            ClientCallback clientCallback = new ClientCallback(mainMenuModel.getClientModel());
            mainMenuModel.registerCallback(String.valueOf(mainMenuModel.getClientModel().getCustomer().getUsername().hashCode()), clientCallback);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    } // end of registerClientCallback

    private void setComponentActions() {
        mainMenuView.getMainMenuAllButton().setOnAction(actionEvent -> {
            mainMenuView.getFlowPane().getChildren().clear(); // remove existing menu from the grid before switching menus
            currentLoadedMenu = 'a';
            loadMenu(1, "All Products");
        });

        // set up action listener for food category button
        mainMenuView.getMainMenuFoodButton().setOnAction(actionEvent -> {
            mainMenuView.getFlowPane().getChildren().clear(); // remove existing menu from the grid before switching menus
            currentLoadedMenu = 'f';
            loadMenu(2, "Food Category");
        });

        // set up action listener for beverages category button
        mainMenuView.getMainMenuBeveragesButton().setOnAction(actionEvent -> {
            mainMenuView.getFlowPane().getChildren().clear(); // remove existing menu from the grid before switching menus
            currentLoadedMenu = 'b';
            loadMenu(3, "Beverage Category");
        });

        // set up action listener for account details button
        mainMenuView.getAccountDetailsImageView().setOnMouseClicked(event -> {
            AccountDetailsView accountDetailsView = AccountDetailsView.loadAccountDetailsPopup();
            AccountDetailsController accountDetailsController = new AccountDetailsController(mainMenuModel, accountDetailsView, mainMenuView);
            accountDetailsController.start();
        });

        //setting up the action for setUpActionClearCartButtonButton
        setUpActionClearCartButton();

        //set up the action for checking out
        setUpActionCheckoutButton();

        mainMenuView.getProductSearchBar().textProperty().addListener((observable, oldValue, newValue) -> filterMenuItems(newValue));

        //set up action for logout button
        setUpActionLogoutButton();

        setUpActionOrderHistoryButton();
    } // end of setComponentActions

    /**This method implements the order history button*/
    private void setUpActionOrderHistoryButton() {
        this.mainMenuView.setUpActionOrderHistoryButton((ActionEvent event) ->{
            if (this.mainMenuModel.getClientModel().getCustomer().getOrderHistory().isEmpty()){
                PushNotification.toastSuccess("Order History Empty", "Your history is empty, try ordering first!");
            }else{
                try {
                    List<Product> distinctProductsOnOrderHistory = getProductsOnOrderHistory();
                    OrderHistoryPageModel orderHistoryPageModel = new OrderHistoryPageModel(distinctProductsOnOrderHistory);
                    OrderHistoryPageView orderHistoryPageView = OrderHistoryPageView.loadCheckoutPage();
                    OrderHistoryPageController orderHistoryPageController = new OrderHistoryPageController(orderHistoryPageModel, orderHistoryPageView);

                    orderHistoryPageView.getSubmitReviewButton().setOnAction(actionEvent ->{
                        orderHistoryPageController.submitRatedProducts();
                        List<Product> ratedProducts = orderHistoryPageController.getRatedProducts();
                        orderHistoryPageController.getView().closeCheckoutView();
                        if (!ratedProducts.isEmpty()){
                            String clientId = String.valueOf(this.mainMenuModel.getClientModel().getCustomer().getName().hashCode());

                            try {
                                mainMenuModel.processReview(clientId, ratedProducts);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            PushNotification.toastSuccess("Review Sent", "Thank You for your reviews!");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                //sendData shit make requests to the server and handle the response properly.
            }
        });
    }

    /**This method gets all the distinct product from the customer's order history*/
    private List<Product> getProductsOnOrderHistory() {
        Set<Product> uniqueProducts = new HashSet<>();

        List<Order> orderHistory = this.mainMenuModel.getClientModel().getCustomer().getOrderHistory();
        for (Order order: orderHistory) {
            List<Product> products= order.getOrders();
            // Add each product to the HashSet
            uniqueProducts.addAll(products);
        }
        return new ArrayList<>(uniqueProducts);
    }

    public void setupClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    } // end of setDateAndTime

    public void updateClock() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = currentTime.format(formatter);
        mainMenuView.setTimeLabel(formattedTime);
    } // end of updateClock

    private void setupDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        mainMenuView.setDateLabel(formattedDate);
    } // end of setupDate

    private void loadMenu(int category, String heading) {
        LoadingScreenUtility.showLoadingIndicator(mainMenuView.getLoadingIndicatorPanel());
        mainMenuView.getProductTypeLabel().setText(heading);

        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(2000);

                Platform.runLater(() -> {
                    switch (category) {
                        case 1 -> mainMenuView.getFlowPane().getChildren().setAll(allProductCardNodes);
                        case 2 -> mainMenuView.getFlowPane().getChildren().setAll(foodCardNodes);
                        case 3 -> mainMenuView.getFlowPane().getChildren().setAll(beverageCardNodes);
                    }
                    LoadingScreenUtility.hideLoadingIndicator(mainMenuView.getLoadingIndicatorPanel());
                });
                return null;
            }
        };
        Thread loadingThread = new Thread(loadingTask);
        loadingThread.start();
    } // end of loadMenu

    private Node createProductCard(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/menu_card.fxml"));
            Node productCard = loader.load();

            MenuCardModel menuCardModel = new MenuCardModel();
            MenuCardView menuCardView = loader.getController();
            MenuCardController menuCardController = new MenuCardController(menuCardModel, menuCardView);
            menuCardController.setProductData(product);

            menuCardView.getAddProductButton().setOnMouseEntered(event -> {
                menuCardView.getAddProductButton().setStyle("-fx-background-color: #66382a; -fx-border-color: #66382a; -fx-border-radius: 50; -fx-background-radius: 50; -fx-text-fill: #ECE9E3");
            });

            menuCardView.getAddProductButton().setOnMouseExited(event -> {
                menuCardView.getAddProductButton().setStyle("-fx-background-color: #e4d8c6; -fx-border-color: #66382a; -fx-border-radius: 50; -fx-background-radius: 50; -fx-text-fill: #634921");
            });

            //this code setups up add to cart button of each card
            menuCardView.getAddProductButton().setOnAction(actionEvent -> {
                MenuCardController updatedMenuCardController = new MenuCardController(menuCardModel, menuCardView);

                Product product1 = menuCardModel.getProduct();
                if (product1 instanceof Food food) {
                    for (Map.Entry<String, Food> entry : mainMenuModel.getClientModel().getFoodMenu().entrySet()) {
                        if (entry.getValue().getName().equals(food.getName())) {
                            updatedMenuCardController.setProductData(entry.getValue());
                            break;
                        }
                    }
                } else if (product1 instanceof Beverage beverage) {
                    for (Map.Entry<String, Beverage> entry : mainMenuModel.getClientModel().getBeverageMenu().entrySet()) {
                        if (entry.getValue().getName().equals(beverage.getName())) {
                            updatedMenuCardController.setProductData(entry.getValue());
                            break;
                        }
                    }
                }

                addToCart(menuCardModel.getProduct());
            });

            return productCard;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // end of createProductCard

    private void filterMenuItems(String searchText) {
        debounceTimer.cancel();
        debounceTimer = new Timer();

        debounceTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    LoadingScreenUtility.showLoadingIndicator(mainMenuView.getLoadingIndicatorPanel());
                    mainMenuView.getFlowPane().getChildren().clear();

                    Task<Void> filterProductsTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            Thread.sleep(2000);

                            Platform.runLater(() -> {
                                List<Node> filteredAllProductCardNodes = new ArrayList<>();
                                List<Node> filteredFoodCardNodes = new ArrayList<>();
                                List<Node> filteredBeverageCardNodes = new ArrayList<>();


                                if (!searchText.isEmpty()) {
                                    filteredFoodCardNodes = mainMenuModel.getClientModel().getFoodMenu().values().stream()
                                            .filter(food -> food.getName().toLowerCase().contains(searchText.toLowerCase()))
                                            .map(MainMenuClientPageController.this::createProductCard)
                                            .toList();

                                    filteredBeverageCardNodes = mainMenuModel.getClientModel().getBeverageMenu().values().stream()
                                            .filter(beverage -> beverage.getName().toLowerCase().contains(searchText.toLowerCase()))
                                            .map(MainMenuClientPageController.this::createProductCard)
                                            .toList();

                                    filteredAllProductCardNodes.addAll(filteredFoodCardNodes);
                                    filteredAllProductCardNodes.addAll(filteredBeverageCardNodes);
                                }

                                switch (currentLoadedMenu) {
                                    case 'a' -> {
                                        if (searchText.isEmpty()) {
                                            mainMenuView.getFlowPane().getChildren().setAll(allProductCardNodes);
                                        } else {
                                            mainMenuView.getFlowPane().getChildren().setAll(filteredAllProductCardNodes);
                                        }
                                    }
                                    case 'f' -> {
                                        if (searchText.isEmpty()) {
                                            mainMenuView.getFlowPane().getChildren().setAll(foodCardNodes);
                                        } else {
                                            mainMenuView.getFlowPane().getChildren().setAll(filteredFoodCardNodes);
                                        }
                                    }
                                    case 'b' -> {
                                        if (searchText.isEmpty()) {
                                            mainMenuView.getFlowPane().getChildren().setAll(beverageCardNodes);
                                        } else {
                                            mainMenuView.getFlowPane().getChildren().setAll(filteredBeverageCardNodes);
                                        }
                                    }
                                }
                                LoadingScreenUtility.hideLoadingIndicator(mainMenuView.getLoadingIndicatorPanel());
                            });
                            return null;
                        }
                    };
                    Thread filterThread = new Thread(filterProductsTask);
                    filterThread.start();
                });
            }
        }, DEBOUNCE_DELAY);
    } // end of debounceFilterMenuItems

    /**
     * This method clears up all the contents of the cart.
     * It implements the setUpActionClearCartButtonButton from the mainMenuClientPageView
     */
    private void setUpActionClearCartButton() {
        this.mainMenuView.setActionClearCartButton((ActionEvent event) -> clearCart(true));
    }

    /*This method sets up the logout button*/
    private void setUpActionLogoutButton() {
        this.mainMenuView.getLogoutButton().setOnAction(event -> {
            String clientID = String.valueOf(this.mainMenuModel.getClientModel().getCustomer().getUsername().hashCode());

            try {
                mainMenuModel.processLogout(clientID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            showLoginPage(event);
        });
    }

    private void clearCart(boolean isUpdateModel) {
        ObservableList<Node> cartItems = this.mainMenuView.getGridPaneCart().getChildren();

        if (cartItems.isEmpty()) {
            return;
        }

        //for each card
        for (Node cartItem : cartItems) {
            String productName = null;
            int productQuantity = 0;
            char productType = ' ';
            String productSize = null;
            double productPrice = 0;

            AnchorPane cartItemPane = (AnchorPane) cartItem;
            for (Node label : cartItemPane.getChildren()) {
                if (label instanceof Label) {
                    switch (label.getId()) {
                        case "productNameLabel":
                            productName = ((Label) label).getText();
                            break;
                        case "quantityLabel":
                            String quantityLabel = ((Label) label).getText();
                            String cleanedQuantity = quantityLabel.replaceAll("[qty:\\s]", "");
                            productQuantity = Integer.parseInt(cleanedQuantity);
                            break;
                        case "sizeLabel":
                            String sizeLabel = ((Label) label).getText();
                            productSize = sizeLabel.replaceAll("[size:\\s]", "");

                            switch (productSize) {
                                case "S" -> {
                                    productSize = "small";
                                    productType = 'b';
                                }
                                case "M" -> {
                                    productSize = "medium";
                                    productType = 'b';
                                }
                                case "L" -> {
                                    productSize = "large";
                                    productType = 'b';
                                }
                                default -> {
                                    productSize = "";
                                    productType = 'f';
                                }
                            }
                            break;
                        case "priceLabel":
                            String priceLabel = ((Label) label).getText();
                            String cleanedPrice = priceLabel.replaceAll("[₱\\s]", ""); //cleaning
                            productPrice = Double.parseDouble(cleanedPrice);
                    }
                }
            }

            try {
                if (isUpdateModel) {
                    //update the models
                    if (productType == 'f') {
                        this.mainMenuModel.getClientModel().getFoodMenu().get(productName).updateQuantity(-productQuantity); //negative because we want to add/revert back the subtracted from the menu
                    } else if (productType == 'b') {
                        this.mainMenuModel.getClientModel().getBeverageMenu().get(productName).updateQuantity(productSize, -productQuantity);
                    }

                    //clear the cart of the customer
                    this.mainMenuModel.getClientModel().getCart().clear();
                }

                //update the price label
                cartTotalPrice -= productPrice;

                //update the UI
                //set visible the labels
                mainMenuView.getCartLabel1().setVisible(true);
                mainMenuView.getCartLabel2().setVisible(true);
                mainMenuView.getCartImage().setVisible(true);

                //add to grid pane
                mainMenuView.getGridPaneCart().setVisible(false);
                mainMenuView.getPriceLabel().setText("₱ " + cartTotalPrice + "0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //after clearing, remove all the contents of the pane
        this.mainMenuView.getGridPaneCart().getChildren().clear();
    }

    /**
     * this method sets up the action for checking out button
     */
    private void setUpActionCheckoutButton() {
        this.mainMenuView.setUpActionCheckoutButton((ActionEvent event) -> {
            if (this.mainMenuModel.getClientModel().getCart().isEmpty()) {
                PushNotification.toastSuccess("Cart Empty", "No items to be checked out. Add items to cart.");
            } else {
                try {
                    CheckoutPageModel checkoutPageModel = new CheckoutPageModel(mainMenuModel.getClientModel().getCustomer(), mainMenuModel.getClientModel().getCart(), cartTotalPrice, mainMenuModel.getClientModel().placeOrder());
                    CheckoutPageView checkoutPageView = CheckoutPageView.loadCheckoutPage();
                    CheckoutPageController checkoutPageController = new CheckoutPageController(checkoutPageModel, checkoutPageView);

                    checkoutPageView.getPlaceOrderButton().setOnAction(actionEvent -> {
                        if (!checkoutPageView.getOnlinePayment().isSelected() && !checkoutPageView.getCashOnDelivery().isSelected()) {
                            checkoutPageView.setNoticeLabel("choose payment option");
                            checkoutPageView.getNoticeLabel().setVisible(true);
                        } else if (checkoutPageView.getOnlinePayment().isSelected() || checkoutPageView.getCashOnDelivery().isSelected()) {
                            String clientId = String.valueOf(checkoutPageModel.getCustomer().getName().hashCode());
                            Order order = checkoutPageModel.getOrderFromClient();

                            //this try catch handles exceptions from the server by the RMI exception throws lists from OrderManagement interface
                            try {
                                Order successfulOrder = mainMenuModel.processCheckout(clientId, order); //order returned after processing from the server

                                //if the order is successful
                                mainMenuModel.getClientModel().orderProcessSuccessful(successfulOrder);
                                PushNotification.toastSuccess("Checkout Status", "Your order has been placed");
                                Platform.runLater(() -> clearCart(false));
                            } catch (OutOfStockException e) {
                                clearCart(false);
                                PushNotification.toastError("Checkout Status", "Order unsuccessful due to stock shortage");
                                Platform.runLater(() -> clearCart(false));
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }

                            checkoutPageModel.getCart().clear();
                            checkoutPageView.closeCheckoutView();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Adds a product into a cart. It also loads the selection panels
     */
    private void addToCart(Product product) {
        SelectFoodController selectFoodController = null;
        SelectBeverageVariationController selectBeverageVariationController = null;

        //load the UIs for selection
        try {
            //set up first the select variation
            if (product.getType() == 'f') {
                FXMLLoader selectFoodLoader = new FXMLLoader(getClass().getResource("/fxml/client/select_food.fxml"));
                Parent root = selectFoodLoader.load();

                selectFoodController = new SelectFoodController(new SelectFoodModel(product), selectFoodLoader.getController());
                Scene scene = new Scene(root);
                Stage popupStage = new Stage();
                popupStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/client/client_app_logo.png")).toExternalForm()));
                popupStage.setScene(scene);
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.showAndWait(); //wait until the popup stops
            } else {
                FXMLLoader selectBeverageVariationLoader = new FXMLLoader(getClass().getResource("/fxml/client/select_beverage_variation.fxml"));
                Parent root = selectBeverageVariationLoader.load();

                selectBeverageVariationController = new SelectBeverageVariationController(new SelectBeverageVariationModel(product), selectBeverageVariationLoader.getController());
                Scene scene = new Scene(root);
                Stage popupStage = new Stage();
                popupStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/client/client_app_logo.png")).toExternalForm()));
                popupStage.setScene(scene);
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.showAndWait(); //wait until the popup stops
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //check whether what order type was produced
        if (product instanceof Food) { //means the product is food being added to cart is food
            if (selectFoodController.getFinalOrderedQuantity() != 0) {
                //update first the models
                updateModelsData(product, selectFoodController);

                //add to cart grid pane
                addToCartGridPane(product, selectFoodController.getFinalOrderedQuantity(), selectFoodController.getFinalOrderedPrice(), null);

                //update the cart totals and the view
                updateCartTotalPrice(selectFoodController.getFinalOrderedPrice());
            }
        } else { //means the product being added is food
            //process the small
            if (selectBeverageVariationController.getFinalSmallOrderedQuantity() != 0) {
                //update the models first
                updateModelsData(product, selectBeverageVariationController.getFinalSmallOrderedQuantity(), "small", selectBeverageVariationController.getFinalSmallOrderedPrice());

                //update the cart grid pane
                addToCartGridPane(product, selectBeverageVariationController.getFinalSmallOrderedQuantity(), selectBeverageVariationController.getFinalSmallOrderedPrice(), "S");

                //update the cart totals and the view
                updateCartTotalPrice(selectBeverageVariationController.getFinalSmallOrderedPrice());

            }

            //process the medium
            if (selectBeverageVariationController.getFinalMediumOrderedQuantity() != 0) {
                //update the models first
                updateModelsData(product, selectBeverageVariationController.getFinalMediumOrderedQuantity(), "medium", selectBeverageVariationController.getFinalMediumOrderedPrice());

                //update the cart grid pane
                addToCartGridPane(product, selectBeverageVariationController.getFinalMediumOrderedQuantity(), selectBeverageVariationController.getFinalMediumOrderedPrice(), "M");

                //update the cart totals and the view
                updateCartTotalPrice(selectBeverageVariationController.getFinalMediumOrderedPrice());
            }

            //process the medium
            if (selectBeverageVariationController.getFinalLargeOrderedQuantity() != 0) {
                //update the models first
                updateModelsData(product, selectBeverageVariationController.getFinalLargeOrderedQuantity(), "large", selectBeverageVariationController.getFinalLargeOrderedPrice());

                //update the cart grid pane
                addToCartGridPane(product, selectBeverageVariationController.getFinalLargeOrderedQuantity(), selectBeverageVariationController.getFinalLargeOrderedPrice(), "L");

                //update the cart totals and the view
                updateCartTotalPrice(selectBeverageVariationController.getFinalLargeOrderedPrice());
            }
        }
    }

    /**
     * This method updates the total price being shown in the cart display.
     */
    private void updateCartTotalPrice(double productPrice) {
        cartTotalPrice += productPrice;
        this.mainMenuView.getPriceLabel().setText("₱ " + cartTotalPrice + "0");
    }

    /**
     * This method updates the models for beverage
     */
    private void updateModelsData(Product product, int count, String size, double totalPrice) {
        try {
            //update the quantity of the main menu food
            mainMenuModel.getClientModel().getBeverageMenu().get(product.getName()).updateQuantity(size, count);

            int sQuantity = 0;
            int mQuantity = 0;
            int lQuantity = 0;
            double sPrice = 0;
            double mPrice = 0;
            double lPrice = 0;
            switch (size) {
                case "small" -> {
                    sQuantity = count;
                    sPrice = totalPrice;
                }
                case "medium" -> {
                    mQuantity = count;
                    mPrice = totalPrice;
                }
                case "large" -> {
                    lQuantity = count;
                    lPrice = totalPrice;
                }
            }

            Beverage beverage = new Beverage(product.getName(), product.getType(), product.getReview(), product.getReviewCount(), product.getImagePackage(), product.getDescription(), sQuantity, mQuantity, lQuantity, sPrice, mPrice, lPrice);
            //update first the cart of the client model which resides in MainMenuModel.getClientModel()
            mainMenuModel.getClientModel().getCart().add(beverage);

            //set visible the labels
            mainMenuView.getCartLabel1().setVisible(false);
            mainMenuView.getCartLabel2().setVisible(false);
            mainMenuView.getCartImage().setVisible(false);

            //add to grid pane
            mainMenuView.getGridPaneCart().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates the models for food
     */
    private void updateModelsData(Product product, SelectFoodController selectFoodController) {
        try {
            //update the quantity of the main menu food
            mainMenuModel.getClientModel().getFoodMenu().get(product.getName()).updateQuantity(selectFoodController.getFinalOrderedQuantity());

            //cast to create a new Food object to be passed on the cart
            Food food = new Food(product.getName(), product.getType(), product.getReview(), product.getReviewCount(), product.getImagePackage(), product.getDescription(), selectFoodController.getFinalOrderedQuantity(), selectFoodController.getFinalOrderedPrice());
            //update first the cart of the client model which resides in MainMenuModel.getClientModel()
            mainMenuModel.getClientModel().getCart().add(food);


            //set visible the labels
            mainMenuView.getCartLabel1().setVisible(false);
            mainMenuView.getCartLabel2().setVisible(false);
            mainMenuView.getCartImage().setVisible(false);

            //add to grid pane
            mainMenuView.getGridPaneCart().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper method that creates a new cards in the cart grid view
     */
    private void addToCartGridPane(Product product, int finalOrderedQuantity, double finalOrderedPrice, String size) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/client/cart_item_card.fxml"));

            //putting the card on the anchorPane
            AnchorPane card = loader.load();

            CartItemCardController cardCart = new CartItemCardController(new CartItemCardModel(product, finalOrderedQuantity, finalOrderedPrice, size), loader.getController());
            cardCart.setData();

            if (cartColumn == 1) {
                cartColumn = 0;
                cartRow++;
            }
            mainMenuView.getGridPaneCart().add(card, cartColumn++, cartRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void showLoginPage(ActionEvent event) {
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            root = loader.load(); //load

            loginPageController = new LoginPageController(loader.getController(), new LoginPageModel(mainMenuModel.getRegistry())); //get controller

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
} // end of MainMenuClientPageController class