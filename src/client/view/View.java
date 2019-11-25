package client.view;

import common.controller.Controller;
import common.observer.Observer;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View extends Observer {
    Controller controller;
    Stage window;

    View(Controller clientController){
        this.controller = clientController;
    }

    public abstract Scene GetScene();

    public void Show(){
        window.setScene(GetScene());
        if(!window.isShowing())
            window.show();
    }
}
