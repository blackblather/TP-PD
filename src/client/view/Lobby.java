package client.view;

import common.controller.Controller;
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

public class Lobby extends View {

    Lobby(Controller controller, Stage window) {
        super(controller, window);
        SetTitle("Lobby");
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane(Orientation.VERTICAL);

        //Create scene
        Scene scene = new Scene(root, 720, 500);
        scene.setFill(Color.AZURE);

        //Create root elements
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);

        //Stylizes vBox
        vBox.setPrefWidth(scene.getWidth());
        vBox.setPrefHeight(scene.getHeight());
        vBox.setPadding(new Insets(10,10,10,10));

        //Align hBox's content
        hBox.setAlignment(Pos.CENTER_RIGHT);

        //Button - Add Song
        Button btnAddSong = new Button("Add Song");

        //Add btnAddSong to hBox
        hBox.getChildren().add(btnAddSong);

        //Add children to vBox
        vBox.getChildren().addAll(hBox);

        root.getChildren().add(vBox);

        return scene;
    }

}
