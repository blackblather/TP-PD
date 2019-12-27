package client.view.modal;

import client.view.View;
import common.CWT.Token;
import common.controller.Controller;
import common.thread.FileTransferThread;
import javafx.application.Platform;
import javafx.geometry.Insets;
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
    private TextField txtFilePath;

    public UploadSong(Controller controller, Stage window, Token token) {
        super(controller, window, token);
        window.initModality(Modality.APPLICATION_MODAL);
        SetTitle("Add Song");
        window.setResizable(false);
    }

    private void ModifyBtnVisibility(Button btnAddSong, TextField txtSongName, TextField txtFilePath){
        if(txtSongName.getText().length() > 0 & txtFilePath.getText().length() > 0)
            btnAddSong.setDisable(false);
        else
            btnAddSong.setDisable(true);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane();

        //Create scene
        Scene scene = new Scene(root, 270, 400);
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

        //Create HBox to host txtFilePath and btnFindFile side-by-side
        HBox hBoxTxtBtn = new HBox(10);
        hBoxTxtBtn.setPrefWidth(scene.getWidth());

        //Textbox - File Path
        txtFilePath = new TextField();

        //Button - Find File
        Button btnFindFile = new Button("Open");
        btnFindFile.setPrefWidth(75);

        //Set btnFindFile onAction event
        btnFindFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select to open");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null)
                txtFilePath.setText(selectedFile.getAbsolutePath());
        });

        //Stylizes txtFilePath
        txtFilePath.setPrefWidth(hBoxTxtBtn.getPrefWidth()-btnFindFile.getPrefWidth());

        //Add children to hBoxTxtBtn
        hBoxTxtBtn.getChildren().addAll(txtFilePath, btnFindFile);

        //Button - Add Song
        Button btnAddSong = new Button("Add Song");
        btnAddSong.setDisable(true);

        btnAddSong.setOnAction(event -> {
                               //Object ref, String token, String name, String author, String album, String year
            controller.AddSong(null, token.toString(), txtSongName.getText(), null, null, 1995);
            //new thread (timeout 10s)
        });

        //Textbox listeners
        txtSongName.textProperty().addListener((observable, oldValue, newValue) -> {
            ModifyBtnVisibility(btnAddSong, txtSongName, txtFilePath);
        });
        txtFilePath.textProperty().addListener((observable, oldValue, newValue) -> {
            ModifyBtnVisibility(btnAddSong, txtSongName, txtFilePath);
        });

        //Add children to hBox
        hBox.getChildren().addAll(btnAddSong);

        //Add children to vBox
        vBox.getChildren().addAll(lblSongName,
                txtSongName,
                lblFilePath,
                hBoxTxtBtn,
                hBox);

        root.getChildren().add(vBox);

        return scene;
    }

    @Override
    public void OnAddSongSuccess(Object ref) {
        Platform.runLater(
            () -> DisplayErrorAlert("GREAT SUCCESS","YE BOYE")
        );
    }

    @Override
    public void OnReadyForUpload(Object ref, String hostname, Integer port) {
        FileTransferThread fileTransferThread = new FileTransferThread(controller, hostname, port);
        fileTransferThread.SendFile(txtFilePath.getText());
    }
}
