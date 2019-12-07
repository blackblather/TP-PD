package client.view;

import common.controller.Controller;
import common.observer.IObserver;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class View implements IObserver {
    private final String APP_NAME = "Musify";

    Controller controller;
    private Stage window;

    View(Controller controller, Stage window){
        this.controller = controller;
        this.window = window;
        this.window.getIcons().add(new Image("client/res/icon.png"));

        IObserver selfObserver = this;
        controller.AddObserver(selfObserver);
        this.window.setOnCloseRequest(windowEvent -> controller.RemoveObserver(selfObserver));
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

    public void SetTitle(String title){
        window.setTitle(APP_NAME + " - " + title);
    }

    @Override
    public void Update(Object o) { }

    @Override
    public void OnSuccessfulLogin(Object ref) { }

    @Override
    public void OnInvalidCredentials(Object ref) { }

    @Override
    public void OnAccountCreated(Object ref) { }
}
