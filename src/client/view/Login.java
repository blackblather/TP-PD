package client.view;

import client.model.Token;
import client.view.modal.Register;
import common.controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends View{
    public Login(Controller controller, Stage window, Token token) {
        super(controller, window, token);
        SetTitle("Login");
        window.setResizable(false);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane(Orientation.VERTICAL);

        //Create scene
        Scene scene = new Scene(root, 250, 170);
        scene.setFill(Color.WHITE);

        //Create root elements
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);

        //Stylizes vBox
        vBox.setPrefWidth(scene.getWidth());
        vBox.setPrefHeight(scene.getHeight());
        vBox.setPadding(new Insets(10,10,10,10));

        //Align hBox's content
        hBox.setAlignment(Pos.CENTER_RIGHT);

        //Label - Username
        Label lblUsername = new Label("Username:");

        //Textbox - Username
        TextField txtUsername = new TextField();

        //Label - Password
        Label lblPassword = new Label("Password:");

        //Textbox - Password
        PasswordField txtPassword = new PasswordField();

        //Button - Register
        Button btnRegister = new Button("Register");
        btnRegister.setOnAction(event -> {
            Register register = new Register(controller, new Stage(), this.token);
            register.Show();
        });

        //Button - Login
        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(event -> {
            controller.Login(null, txtUsername.getText(), txtPassword.getText());
        });

        //Add children to hBox
        hBox.getChildren().addAll(btnRegister,
                                  btnLogin);

        //Add children to vBox
        vBox.getChildren().addAll(lblUsername,
                                  txtUsername,
                                  lblPassword,
                                  txtPassword,
                                  hBox);

        root.getChildren().add(vBox);

        return scene;
    }

    @Override
    public void OnLoginSuccess(Object ref, String tokenStr) {
        Platform.runLater(
            () -> {
                this.token.setValue(tokenStr);
                Close();
                Lobby lobby = new Lobby(controller, new Stage(), this.token);
                lobby.Show();
            }
        );
    }

    @Override
    public void OnInvalidCredentials(Object ref) {
        Platform.runLater(
            () -> DisplayErrorAlert("Invalid credentials", "Username and/or password are invalid")
        );
    }

    @Override
    public void OnRegisterSuccess(Object ref, String tokenStr) {
        Platform.runLater(this::Close);
    }

    @Override
    public void OnExceptionOccurred(Object ref, String exceptionName) {
        Platform.runLater(
            () -> DisplayErrorAlert(exceptionName,"The server is experiencing some problems at the moment.\nPlease try again later")
        );
    }
}
