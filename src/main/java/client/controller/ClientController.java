package client.controller;

import client.controller.landingpage.LandingPageController;
import client.view.ClientView;

public class ClientController {
    public ClientController(ClientView view){
        System.out.println("controller for landing page acquired");
        new LandingPageController(view.getLoader().getController());
    }
}
