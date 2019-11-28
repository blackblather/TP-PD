package client.view;

import common.controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login extends View{

    public Login(Controller controller, Stage window) {
        super(controller);
        this.window = window;
        controller.AddObserver(this);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane(Orientation.VERTICAL);

        //Create scene
        Scene scene = new Scene(root, 200, 170);
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
        Label lblUsername = new Label("Insert username:");

        //Textbox - Username
        TextField txtUsername = new TextField();

        //Label - Password
        Label lblPassword = new Label("Insert password:");

        //Textbox - Password
        TextField txtPassword = new TextField();

        //Button - Login
        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(event -> {
            controller.Login(null, txtUsername.getText(), txtPassword.getText());
        });

        //Button - Register
        Button btnRegister = new Button("Register");
        btnRegister.setOnAction(event -> {
        });

        //Add children to hBox
        hBox.getChildren().addAll(btnLogin,
                                  btnRegister);

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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("YE BOYEEEEEEEEEEEEEEEEEE");
                alert.setHeaderText("Look, an Information Dialog");
                alert.setContentText("LOGIN FUCKING SUCCESSFUL!");

                alert.showAndWait();
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
}
