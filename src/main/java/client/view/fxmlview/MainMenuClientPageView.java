package client.view.fxmlview;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.UITransitionBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class MainMenuClientPageView {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button logoutButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label accountNameLabel;
    @FXML
    private ImageView cartImage;
    @FXML
    private Button clearCartButton;
    @FXML
    private Label cartLabel1;
    @FXML
    private Label cartLabel2;
    @FXML
    private Button checkoutButton;
    @FXML
    private GridPane gridPaneCart;
    @FXML
    private ScrollPane scrollPaneCart;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ScrollPane scrollPaneMenu;
    @FXML
    private Button mainMenuAllButton;
    @FXML
    private Button mainMenuBeveragesButton;
    @FXML
    private Button mainMenuFoodButton;
    @FXML
    private Button orderHistoryMenuButton;
    @FXML
    private Label priceLabel;
    @FXML
    private Label productTypeLabel;
    @FXML
    private Text logoutLB;
    @FXML
    private TextField productSearchBar;
    @FXML
    private Pane loadingIndicatorPanel;
    @FXML
    private ImageView accountDetailsImageView;
    @FXML
    private Rectangle allMenuRectangle;
    @FXML
    private Rectangle foodMenuRectangle;
    @FXML
    private Rectangle beverageMenuRectangle;
    @FXML
    private Pane allMenuPane;
    @FXML
    private Pane foodMenuPane;
    @FXML
    private Pane beverageMenuPane;
    @FXML
    private ImageView allImageView;
    @FXML
    private ImageView foodImageView;
    @FXML
    private ImageView beverageImageView;
    @FXML
    private ImageView logoutImageView;
    private HashMap<String, Boolean> menuItems = new HashMap<>();
    private HashMap<String, FillTransition> fillTransitionHashMap = new HashMap<>();
    private HashMap<String, ScaleTransition> scaleTransitionHashMap = new HashMap<>();
    private final Image allCategoryImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/hamburgerSoda.png")));
    private final Image allCategoryAltImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/hamburgerSodaAlt.png")));
    private final Image foodCategoryImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/sandwich.png")));
    private final Image foodCategoryAltImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/sandwichAlt.png")));
    private final Image beverageCategoryImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/coffee.png")));
    private final Image beverageCategoryAltImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/coffeeAlt.png")));
    private final Image logoutImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/power.png")));
    private final Image logoutAltImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/client/mainmenu/powerAlt.png")));

    @FXML
    private void initialize() {
        try {
            try {
                setUpMenuItems();
                createBTTransitions();
                setUpMenuBTBehaviour();
                setUpAccountDetailsBTBehaviour();
            } catch (Exception exception) {
                exception.getCause().printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setActionClearCartButton(EventHandler<ActionEvent> event){
        clearCartButton.setOnAction(event);
    }

    public void setUpActionCheckoutButton(EventHandler<ActionEvent> event){
        checkoutButton.setOnAction(event);
    }

    public void setUpActionOrderHistoryButton(EventHandler<ActionEvent> event){
        orderHistoryMenuButton.setOnAction(event);
    }

    private void setUpMenuItems() {
        menuItems.put("All Category", true);
        menuItems.put("Food Category", false);
        menuItems.put("Beverage Category", false);
    } // end of setUpMenuItems

    private void createBTTransitions() {
        scaleTransitionHashMap.put("allBTScaleIn", UITransitionBuilder.createScaleTransition(allMenuPane, 300, 1, 1.1));
        scaleTransitionHashMap.put("allBTScaleOut", UITransitionBuilder.createScaleTransition(allMenuPane, 100, 1.1, 1));
        scaleTransitionHashMap.put("foodBTScaleIn", UITransitionBuilder.createScaleTransition(foodMenuPane, 300, 1, 1.1));
        scaleTransitionHashMap.put("foodBTScaleOut", UITransitionBuilder.createScaleTransition(foodMenuPane, 100, 1.1, 1));
        scaleTransitionHashMap.put("beverageBTScaleIn", UITransitionBuilder.createScaleTransition(beverageMenuPane, 300, 1, 1.1));
        scaleTransitionHashMap.put("beverageBTScaleOut", UITransitionBuilder.createScaleTransition(beverageMenuPane, 100, 1.1, 1));

        fillTransitionHashMap.put("allBTFillIn", UITransitionBuilder.createFillTransition(allMenuRectangle, 600, "#ece9e3", "#b59c7a"));
        fillTransitionHashMap.put("allBTFillOut", UITransitionBuilder.createFillTransition(allMenuRectangle, 300, "#b59c7a", "#ece9e3"));
        fillTransitionHashMap.put("foodBTFillIn", UITransitionBuilder.createFillTransition(foodMenuRectangle, 600, "#ece9e3", "#b59c7a"));
        fillTransitionHashMap.put("foodBTFillOut", UITransitionBuilder.createFillTransition(foodMenuRectangle, 300, "#b59c7a", "#ece9e3"));
        fillTransitionHashMap.put("beverageBTFillIn", UITransitionBuilder.createFillTransition(beverageMenuRectangle, 600, "#ece9e3", "#b59c7a"));
        fillTransitionHashMap.put("beverageBTFillOut", UITransitionBuilder.createFillTransition(beverageMenuRectangle, 300, "#b59c7a", "#ece9e3"));
    } // end of createBTTransitions

    private void setUpMenuBTBehaviour() {
        // initial transition when the main view is loaded
        allImageView.setImage(allCategoryAltImg);
        fillTransitionHashMap.get("allBTFillIn").play();

        mainMenuAllButton.setOnMouseClicked(event -> {
            allImageView.setImage(allCategoryAltImg);
            fillTransitionHashMap.get("allBTFillIn").play();
            menuItems.put("All Category", true);
            revertPreviousMenuBTStyle("All Category");
        });

        mainMenuFoodButton.setOnMouseClicked(event -> {
            foodImageView.setImage(foodCategoryAltImg);
            fillTransitionHashMap.get("foodBTFillIn").play();
            menuItems.put("Food Category", true);
            revertPreviousMenuBTStyle("Food Category");
        });

        mainMenuBeveragesButton.setOnMouseClicked(event -> {
            beverageImageView.setImage(beverageCategoryAltImg);
            fillTransitionHashMap.get("beverageBTFillIn").play();
            menuItems.put("Beverage Category", true);
            revertPreviousMenuBTStyle("Beverage Category");
        });

        logoutButton.setOnMouseEntered(event -> {
            logoutLB.setStyle("-fx-fill: #ae5d5d;");
            logoutImageView.setImage(logoutAltImg);
        });
        logoutButton.setOnMouseExited(event -> {
            logoutLB.setStyle("-fx-fill: #b59c7a;");
            logoutImageView.setImage(logoutImg);
        });

        setUpMenuBTHoverBehaviour(mainMenuAllButton, scaleTransitionHashMap.get("allBTScaleIn"), scaleTransitionHashMap.get("allBTScaleOut"));
        setUpMenuBTHoverBehaviour(mainMenuFoodButton, scaleTransitionHashMap.get("foodBTScaleIn"), scaleTransitionHashMap.get("foodBTScaleOut"));
        setUpMenuBTHoverBehaviour(mainMenuBeveragesButton, scaleTransitionHashMap.get("beverageBTScaleIn"), scaleTransitionHashMap.get("beverageBTScaleOut"));
    } // end of setUpMenuBTBehaviour

    private void revertPreviousMenuBTStyle(String currentBT) {
        if (menuItems.get("All Category") && !"All Category".equalsIgnoreCase(currentBT)) {
            allImageView.setImage(allCategoryImg);
            fillTransitionHashMap.get("allBTFillOut").play();
            menuItems.put("All Category", false);
            return;
        }
        if (menuItems.get("Food Category") && !"Food Category".equalsIgnoreCase(currentBT)) {
            foodImageView.setImage(foodCategoryImg);
            fillTransitionHashMap.get("foodBTFillOut").play();
            menuItems.put("Food Category", false);
            return;
        }
        if (menuItems.get("Beverage Category") && !"Beverage Category".equalsIgnoreCase(currentBT)) {
            beverageImageView.setImage(beverageCategoryImg);
            fillTransitionHashMap.get("beverageBTFillOut").play();
            menuItems.put("Beverage Category", false);
        }
    } // end of revertPreviousMenuBTStyle

    private void setUpAccountDetailsBTBehaviour() {
        FadeTransition fadeIn = UITransitionBuilder.createFadeTransition(accountDetailsImageView, 300, 1, 0.6);
        FadeTransition fadeOut = UITransitionBuilder.createFadeTransition(accountDetailsImageView, 100, 0.6, 1);

        accountDetailsImageView.setOnMouseEntered(event -> {
            fadeOut.stop();
            fadeIn.play();
        });

        accountDetailsImageView.setOnMouseExited(event -> {
            fadeIn.stop();
            fadeOut.play();
        });
    } // end of setUpAccountDetailsBTBehaviour

    private void setUpMenuBTHoverBehaviour(Button button, ScaleTransition scaleIn, ScaleTransition scaleOut) {
        button.setOnMouseEntered(event -> {
            scaleOut.stop();
            scaleIn.play();
        });

        button.setOnMouseExited(event -> {
            scaleIn.stop();
            scaleOut.play();
        });
    } // end of setOnMenuBTHovered

    public void showOrderHistoryUI () throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/order_history_page.fxml"));
        root = loader.load();

        Stage selectVariationStage = new Stage();
        Scene selectVariationScene = new Scene(root);
        selectVariationStage.getIcons().add(new Image(getClass().getResource("/images/client/client_app_logo.png").toExternalForm()));

        selectVariationStage.initOwner(stage);

        selectVariationStage.initModality(Modality.APPLICATION_MODAL);

        selectVariationStage.initStyle(StageStyle.DECORATED);

        selectVariationStage.setScene(selectVariationScene);
        selectVariationStage.show();
    }

    public void showCheckoutUI (ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/checkout_page.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/images/client/client_app_logo.png").toExternalForm()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void checkoutButtonEntered(){
        checkoutButton.setStyle("-fx-background-color: lightgray;");
        checkoutButton.setTextFill(Paint.valueOf("Black"));
    }
    public void checkoutButtonExited(){
        checkoutButton.setStyle("-fx-background-color:  #A38157;");
        checkoutButton.setTextFill(Paint.valueOf("White"));
    }

    public void orderHistoryMenuButtonEntered(){
        orderHistoryMenuButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 3;");
        orderHistoryMenuButton.setTextFill(Paint.valueOf("black"));
    }
    public void orderHistoryMenuButtonExited(){
        orderHistoryMenuButton.setStyle("-fx-background-color:  #FFFFFF; -fx-border-color: #A38157; -fx-border-radius: 3;");
        orderHistoryMenuButton.setTextFill(Paint.valueOf("#A38157"));
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public ImageView getCartImage() {
        return cartImage;
    }

    public void setCartImage(ImageView cartImage) {
        this.cartImage = cartImage;
    }

    public Label getCartLabel1() {
        return cartLabel1;
    }

    public void setCartLabel1(Label cartLabel1) {
        this.cartLabel1 = cartLabel1;
    }

    public Label getCartLabel2() {
        return cartLabel2;
    }

    public GridPane getGridPaneCart() {
        return gridPaneCart;
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public Button getMainMenuAllButton(){return  mainMenuAllButton;}

    public Button getMainMenuBeveragesButton() {
        return mainMenuBeveragesButton;
    }

    public Button getMainMenuFoodButton() {
        return mainMenuFoodButton;
    }

    public Label getPriceLabel() {
        return priceLabel;
    }

    public Label getProductTypeLabel() {
        return productTypeLabel;
    }


    public Label getAccountNameLabel() {
        return accountNameLabel;
    }

    public void setDateLabel(String value) {
        dateLabel.setText(value);
    }

    public void setTimeLabel(String value) {
        timeLabel.setText(value);
    }

    public TextField getProductSearchBar() {
        return productSearchBar;
    }

    public Pane getLoadingIndicatorPanel() {
        return loadingIndicatorPanel;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public ImageView getAccountDetailsImageView() {
        return accountDetailsImageView;
    }
}
