package client.view;

import common.observable.ObservableController;
import common.observer.ControllerObserver;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View extends ControllerObserver {
    ObservableController controller;
    Stage window;

    View(ObservableController clientController){
        this.controller = clientController;
    }

    public abstract Scene GetScene();

    public void Show(){
        window.setScene(GetScene());
        if(!window.isShowing())
            window.show();
    }
}
