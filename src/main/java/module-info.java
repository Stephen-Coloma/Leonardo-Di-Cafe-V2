module LeonardoDiCafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires java.xml;
    requires java.rmi;
    requires com.google.gson;

    opens client to javafx.fxml;
    opens server to javafx.fxml;
    opens client.controller to javafx.fxml;
    opens server.view to javafx.fxml;
    opens client.view to javafx.fxml;
    opens client.view.fxmlview to javafx.fxml;
    opens server.view.inventory to javafx.fxml;
    opens server.view.accounts to javafx.fxml;
    opens server.view.orders to javafx.fxml;
    opens server.view.addproducts to javafx.fxml;
    opens server.view.misc to javafx.fxml;
    opens server.view.analytics to javafx.fxml;
    opens client.controller.orderhistory to javafx.fxml;
    opens shared to com.google.gson;

    exports client;
    exports server;
    exports client.controller;
    exports client.view;
    exports server.view;
    exports client.view.fxmlview;
    exports server.view.inventory;
    exports server.view.accounts;
    exports server.view.orders;
    exports server.view.addproducts;
    exports server.view.misc;
    exports server.view.analytics;
    exports client.controller.orderhistory;
    exports shared.rmiinterfaces;
    exports shared.callback;
    exports shared;
}