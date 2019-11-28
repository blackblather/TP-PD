package client.view;

import common.controller.Controller;
import common.observer.IObserver;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View implements IObserver {
    Controller controller;
    private Stage window;

    View(Controller controller, Stage window){
        this.controller = controller;
        this.window = window;
        controller.AddObserver(this);
    }

    public abstract Scene GetScene();

    public void Show(){
        SetScene(GetScene());
        if(!window.isShowing())
            window.show();
    }

    public void SetScene(Scene scene){
        window.setScene(scene);
    }

    public void Destroy(){
        window.close();
        controller.RemoveObserver(this);
    }

    @Override
    public void Update(Object o) { }

    @Override
    public void OnSuccessfulLogin(Object ref) { }

    @Override
    public void OnInvalidCredentials(Object ref) { }
}
