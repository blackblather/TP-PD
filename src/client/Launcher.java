package client;

import client.controller.ClientController;
import client.view.Login;
import common.CWT.Token;
import common.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    //java -cp "..\..\..\json-20190722.jar;..\..\..\javafx-sdk-11.0.2;." client.Launcher
    private Controller controller = new ClientController();
    private Token token = new Token();

    @Override
    public void start(Stage window) {
        //Set starting point here
        Login login = new Login(controller, window, token);
        login.Show();
    }

    public static void main(String [] args){
        //TODO: Conexão com DS
        launch(args);
    }
}
