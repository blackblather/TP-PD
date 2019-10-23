package server.controller;

interface IController {
    void Login(String username, String password);
    //INSERT OPERATIONS
    void AddMusic(String username, String name, String author, String album, String year, String path);
    void AddPlaylist(String username, String name);
    void AddUser(String username, String password);
    //DELETE OPERATIONS
    void RemoveMusic(String name);
    void RemovePlaylist(String name);
    //SELECT OPERATIONS
    void GetMusics();
    void GetMusic(String name);
    void GetPlaylists();
    void GetPlaylist(String name);
}
