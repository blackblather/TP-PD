package client.view;

import common.controller.Controller;
import common.observer.IObserver;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class View implements IObserver {

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

    public void Close(){
        window.close();
    }

    private void SetScene(Scene scene){
        window.setScene(scene);
    }

    void SetTitle(String title){
        window.setTitle("Musify - " + title);
    }

    @Override
    public void Update(Object o) { }

    @Override
    public void OnSuccessfulLogin(Object ref) { }

    @Override
    public void OnInvalidCredentials(Object ref) { }

    @Override
    public void OnRegisterSuccess(Object ref) { }

    @Override
    public void OnRegisterError(Object ref) { }
}
