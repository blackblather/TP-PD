package client.view;

import common.controller.Controller;
import common.observer.IObserver;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View implements IObserver {
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

    @Override
    public void Update(Object o) { }

    @Override
    public void OnSuccessfulLogin(Object ref) { }

    @Override
    public void OnInvalidCredentials(Object ref) { }
}
