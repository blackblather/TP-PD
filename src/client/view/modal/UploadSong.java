package client.view.modal;

import client.view.View;
import common.CWT.Token;
import common.controller.Controller;
import common.model.Music;
import common.thread.FileTransferThread;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Calendar;

public class UploadSong extends View {
    private TextField txtSongName;
    private TextField txtAuthor;
    private TextField txtAlbum;
    private TextField txtFilePath;

    public UploadSong(Controller controller, Stage window, Token token) {
        super(controller, window, token);
        window.initModality(Modality.APPLICATION_MODAL);
        SetTitle("Add Song");
        window.setResizable(false);
    }

    private void ModifyBtnVisibility(Button btnAddSong){
        if(txtSongName.getText().length() > 0 &&
           txtAuthor.getText().length() > 0 &&
           txtAlbum.getText().length() > 0 &&
           txtFilePath.getText().length() > 0)
            btnAddSong.setDisable(false);
        else
            btnAddSong.setDisable(true);
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane();

        //Create scene
        Scene scene = new Scene(root, 270, 355);
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
        txtSongName = new TextField();

        //Label - Author
        Label lblAuthor = new Label("Author:");

        //Textbox - Author
        txtAuthor = new TextField();

        //Label - Album
        Label lblAlbum = new Label("Album:");

        //Textbox - Album
        txtAlbum = new TextField();

        //Label - Year
        Label lblYear = new Label("Year:");

        //Textbox - Year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Spinner<Integer> spinnerYear = new Spinner<>(1, currentYear, currentYear, 1);
        spinnerYear.setPrefWidth(scene.getWidth());

        //Label - File Path
        Label lblFilePath = new Label("File path:");

        //Textbox - File Path
        txtFilePath = new TextField();

        //Create HBox to host txtFilePath and btnFindFile side-by-side
        HBox hBoxTxtBtn = new HBox(10);
        hBoxTxtBtn.setPrefWidth(scene.getWidth());

        //Button - Find File
        Button btnFindFile = new Button("Open");
        btnFindFile.setPrefWidth(75);

        //Set btnFindFile onAction event
        btnFindFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select to open");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));
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
            controller.UploadSongRequest(null, token.toString(), new Music(txtSongName.getText(), txtAuthor.getText(), txtAlbum.getText(), spinnerYear.getValue(), new File(txtFilePath.getText())));
            //new thread (timeout 10s)
        });

        //Textbox listeners
        txtSongName.textProperty().addListener((observable, oldValue, newValue) -> {
            ModifyBtnVisibility(btnAddSong);
        });
        txtFilePath.textProperty().addListener((observable, oldValue, newValue) -> {
            ModifyBtnVisibility(btnAddSong);
        });

        //Add children to hBox
        hBox.getChildren().addAll(btnAddSong);

        //Add children to vBox
        vBox.getChildren().addAll(lblSongName,
                txtSongName,
                lblAuthor,
                txtAuthor,
                lblAlbum,
                txtAlbum,
                lblYear,
                spinnerYear,
                lblFilePath,
                hBoxTxtBtn,
                hBox);

        root.getChildren().add(vBox);

        return scene;
    }

    @Override
    public void OnAddSongSuccess(Object ref) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Upload complete");
            alert.setContentText("Song uploaded with success");

            alert.showAndWait();

            Close();
        });
    }

    @Override
    public void OnReadyForUpload(Object ref, String hostname, Integer port) {
        FileTransferThread fileTransferThread = new FileTransferThread(controller, hostname, port);
        fileTransferThread.SendFile(txtFilePath.getText());
    }
}
