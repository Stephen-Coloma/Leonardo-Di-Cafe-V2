module LeonardoDiCafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires java.xml;
    requires com.google.gson;

    opens client to javafx.fxml;
    opens server to javafx.fxml;
    opens client.controller to javafx.fxml;
    opens server.view to javafx.fxml;
    opens client.view to javafx.fxml;
    opens shared to com.google.gson;

    exports client;
    exports server;
    exports client.controller;
    exports client.view;
    exports server.view;
    exports client.view.fxmlview;
    opens client.view.fxmlview to javafx.fxml;
    exports server.view.inventory to javafx.fxml;
    opens server.view.inventory to javafx.fxml;
    exports server.view.accounts;
    opens server.view.accounts to javafx.fxml;
    exports server.view.orders;
    opens server.view.orders to javafx.fxml;
    exports server.view.addproducts;
    opens server.view.addproducts to javafx.fxml;
    exports server.view.misc;
    opens server.view.misc to javafx.fxml;
    exports server.view.analytics;
    opens server.view.analytics to javafx.fxml;
    exports client.controller.orderhistory;
    opens client.controller.orderhistory to javafx.fxml;
}