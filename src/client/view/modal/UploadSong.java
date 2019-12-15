package client.view.modal;

import client.view.View;
import common.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class UploadSong extends View {
    public UploadSong(Controller controller, Stage window) {
        super(controller, window);
        window.initModality(Modality.APPLICATION_MODAL);
        SetTitle("Add Song");
        window.setResizable(false);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane(Orientation.VERTICAL);

        //Create scene
        Scene scene = new Scene(root, 270, 170);
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

        //Label - Song Name
        Label lblSongName = new Label("Song name:");

        //Textbox - Song Name
        TextField txtSongName = new TextField();

        //Label - File Path
        Label lblFilePath = new Label("File path:");

        //Textbox - File Path
        TextField txtFilePath = new TextField();

        //Button - Find File
        Button btnFindFile = new Button("Open File");
        btnFindFile.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null)
                txtFilePath.setText(selectedFile.getAbsolutePath());
        });

        //Button - Add Song
        Button btnAddSong = new Button("Add Song");
        btnAddSong.setOnAction(event -> {

        });

        //Add children to hBox
        hBox.getChildren().addAll(btnFindFile, btnAddSong);

        //Add children to vBox
        vBox.getChildren().addAll(lblSongName,
                txtSongName,
                lblFilePath,
                txtFilePath,
                hBox);

        root.getChildren().add(vBox);

        return scene;
    }
}
