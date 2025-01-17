package client.view;

import client.view.modal.UploadSong;
import common.CWT.Token;
import common.controller.Controller;
import common.model.Music;
import common.model.MusicListItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Lobby extends View {

    private List<MusicListItem> musicList = List.of();

    private ObservableList<MusicListItem> observableMusicList = FXCollections.observableArrayList(musicList);

    public Lobby(Controller controller, Stage window, Token token) {
        super(controller, window, token);
        SetTitle("Lobby");
        window.setResizable(false);
    }

    private MenuBar CreateMainMenu(double width){
        //Creates our main menu to hold our Sub-Menus.
        MenuBar mainMenu = new MenuBar();
        //Stylizes mainMenu
        mainMenu.setPrefWidth(width);
        //Create and add the "Account" sub-menu options.
        Menu menuAccount = new Menu("Account");
        MenuItem menuItemLogout = new MenuItem("Logout");
        MenuItem menuItemExit = new MenuItem("Exit");
        menuAccount.getItems().addAll(menuItemLogout,menuItemExit);
        //Create and add the "Music" sub-menu options.
        Menu menuMusic = new Menu("Music");
        MenuItem menuItemUploadSong = new MenuItem("Upload music");
        menuMusic.getItems().add(menuItemUploadSong);
        //Create and add the "Playlists" sub-menu options.
        Menu menuPlaylists = new Menu("Playlists");
        MenuItem menuItemCreatePlaylist = new MenuItem("Create playlist");
        menuPlaylists.getItems().add(menuItemCreatePlaylist);
        //Add menus to mainMenu
        mainMenu.getMenus().addAll(menuAccount, menuMusic, menuPlaylists);

        return mainMenu;
    }

    /* ----------------------------------- // -----------------------------------
     * Every time an object is added/removed to the observableMusicList,the observers are notified.
     * In this case the table object is notified and it updates its columns
     * ----------------------------------- // -----------------------------------
     * cellValueFactory -> Used to populate individual cells in the column
     * ----------------------------------- // -----------------------------------
     * new PropertyValueFactory<MusicListItem, String>("name") -> Used to:
     *  -> Invoke nameProperty() in MusicListItem class;
     *  -> Get the property;
     *  -> Register the tableView as an observer of that property;
     * ----------------------------------- // ----------------------------------- */
    private TableView<MusicListItem> CreateTableView(){
        TableView<MusicListItem> table = new TableView<>();
        table.setItems(observableMusicList);        //Sets table as observer of observableMusicList

        TableColumn<MusicListItem, String> cellName = new TableColumn<>("Name");
        cellName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cellName.setPrefWidth(458);

        TableColumn<MusicListItem, String> cellAuthor = new TableColumn<>("Author");
        cellAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<MusicListItem, String> cellAlbum = new TableColumn<>("Album");
        cellAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn<MusicListItem, String> cellYear = new TableColumn<>("Year");
        cellYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        table.getColumns().setAll(cellName, cellAuthor, cellAlbum, cellYear);

        return table;
    }

    @Override
    public Scene GetScene() {
        //Create root
        FlowPane root = new FlowPane();

        //Create scene
        Scene scene = new Scene(root, 720, 500);
        scene.setFill(Color.AZURE);

        //Create Main Menu
        MenuBar mainMenu = CreateMainMenu(scene.getWidth());

        //Create root elements
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);

        //Stylizes vBox
        vBox.setPrefWidth(scene.getWidth());
        vBox.setPrefHeight(scene.getHeight());
        vBox.setPadding(new Insets(10,10,10,10));

        //Align hBox's content
        hBox.setAlignment(Pos.CENTER_RIGHT);

        //Button - Refresh
        Button btnRefresh = new Button("Refresh");
        btnRefresh.setOnAction(actionEvent -> {
            controller.GetSongs(null, token.toString());
        });

        //Button - Add Song
        Button btnAddSong = new Button("Add Song");
        btnAddSong.setOnAction(actionEvent -> {
            UploadSong uploadSong = new UploadSong(controller, new Stage(), this.token);
            uploadSong.SetOwner(window);
            uploadSong.Show();
        });

        //Add btnAddSong to hBox
        hBox.getChildren().addAll(btnRefresh, btnAddSong);

        //Create Table View
        TableView<MusicListItem> table = CreateTableView();

        //Add children to vBox
        vBox.getChildren().addAll(hBox, table);

        //Add children to flowpane
        root.getChildren().addAll(mainMenu,vBox);

        //Requests for the list of songs
        controller.GetSongs(null, token.toString());

        return scene;
    }

    @Override
    public void OnAddSongSuccess(Object ref, Music music) {
        Platform.runLater(()->{
            observableMusicList.add(new MusicListItem(music.getName(), music.getAuthor(), music.getAlbum(), music.getYear()));
        });
    }

    @Override
    public void OnGetSongsSuccess(Object ref, List<Music> musics) {
        //Warning: NOT OPTIMIZED!!!
        Platform.runLater(()-> {
            //Clear list
            observableMusicList.clear();
            //Adds new songs
            for(Music music : musics)
                observableMusicList.add(new MusicListItem(music.getName(), music.getAuthor(), music.getAlbum(), music.getYear()));
        });
    }
}
