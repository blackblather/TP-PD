package client;

import client.controller.ClientController;
import client.view.Login;
import common.observable.ObservableController;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.ServerController;

public class Launcher extends Application {
    private ObservableController controller = new ServerController();

    public static void main(String [] args){
        //TODO: Conexão
        launch(args);
    }

    @Override
    public void start(Stage window) {
        //Set starting point here
        Login login = new Login(controller, window);
        login.Show();
    }
}
