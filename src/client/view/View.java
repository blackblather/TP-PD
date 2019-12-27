package client.view;

import common.CWT.Token;
import common.controller.Controller;
import common.observer.IObserver;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class View implements IObserver {
    protected Token token;
    protected Controller controller;
    protected Stage window;


    public View(Controller controller, Stage window, Token token){
        this.controller = controller;
        this.window = window;
        this.window.getIcons().add(new Image("client/res/icon.png"));

        this.token = token;

        IObserver selfObserver = this;
        controller.AddObserver(selfObserver);
        this.window.setOnCloseRequest(windowEvent -> controller.RemoveObserver(selfObserver));
    }

    public abstract Scene GetScene();

    protected void SetOwner(Window owner){
        window.initOwner(owner);
    }

    public void Show(){
        SetScene(GetScene());
        if(!window.isShowing())
            window.show();
    }

    protected void DisplayErrorAlert(String text, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred");
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    protected void Close(){
        window.close();
    }

    private void SetScene(Scene scene){
        window.setScene(scene);
    }

    protected void SetTitle(String title){
        window.setTitle("Musify - " + title);
    }

    @Override
    public void Update(Object o) { }

    @Override
    public void OnLoginSuccess(Object ref, String token) { }

    @Override
    public void OnInvalidCredentials(Object ref) { }

    @Override
    public void OnRegisterSuccess(Object ref, String token) { }

    @Override
    public void OnPasswordsNotMatching(Object ref) { }

    @Override
    public void OnUsernameNotUnique(Object ref) { }

    @Override
    public void OnAddSongSuccess(Object ref) { }

    @Override
    public void OnReadyForUpload(Object ref, String hostname, Integer port) { }

    @Override
    public void OnReadyForDownload(Object ref, String hostname, Integer port) { }

    @Override
    public void OnExceptionOccurred(Object ref, String exceptionName) { }
}
