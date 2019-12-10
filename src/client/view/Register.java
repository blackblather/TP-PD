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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Register extends View {

    Register(Controller controller, Stage window) {
        super(controller, window);
        window.initModality(Modality.APPLICATION_MODAL);
        SetTitle("Register");
        window.setResizable(false);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane(Orientation.VERTICAL);

        //Create scene
        Scene scene = new Scene(root, 270, 230);
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

        //Label - Password
        Label lblPasswordConf = new Label("Confirm password:");

        //Textbox - Password
        PasswordField txtPasswordConf = new PasswordField();

        //Button - Register
        Button btnRegister = new Button("Register");
        btnRegister.setOnAction(event -> {
            controller.Register(null, txtUsername.getText(), txtPassword.getText(), txtPasswordConf.getText());
        });

        //Add children to hBox
        hBox.getChildren().addAll(btnRegister);

        //Add children to vBox
        vBox.getChildren().addAll(lblUsername,
                txtUsername,
                lblPassword,
                txtPassword,
                lblPasswordConf,
                txtPasswordConf,
                hBox);

        root.getChildren().add(vBox);

        return scene;
    }

    @Override
    public void OnRegisterSuccess(Object ref) {
        Platform.runLater(
            () -> {
                Close();
                Lobby lobby = new Lobby(controller, new Stage());
                lobby.Show();
            }
        );
    }

    @Override
    public void OnPasswordsNotMatching(Object ref) {
        Platform.runLater(
            () -> DisplayErrorAlert("Passwords do not match", "The password and password confirmation fields do not match")
        );
    }

    @Override
    public void OnUsernameNotUnique(Object ref) {
        Platform.runLater(
            () -> DisplayErrorAlert("Username not unique", "The chosen username is already registered")
        );
    }
}
