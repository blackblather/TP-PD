package client.view;

import common.controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends View{
    public Login(Controller controller, Stage window) {
        super(controller, window);
        SetTitle("Login");
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
            Register register = new Register(controller, new Stage());
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
    public void OnSuccessfulLogin(Object ref) {
        Platform.runLater(
            () -> {
                Close();
                Lobby lobby = new Lobby(controller, new Stage());
                lobby.Show();
            }
        );
    }

    @Override
    public void OnInvalidCredentials(Object ref) {
        Platform.runLater(
            () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred");
                alert.setHeaderText("Invalid credentials.");
                alert.setContentText("Username and/or password are invalid");

                alert.showAndWait();
            }
        );
    }

    @Override
    public void OnRegisterSuccess(Object ref) {
        Platform.runLater(
            () -> {
                Close();
            }
        );
    }
}
