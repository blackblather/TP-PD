package common.controller;

import org.json.JSONException;

public interface IController {
    void RouteJSONStr(Object ref, String jsonStr) throws JSONException;
    void Login(Object ref, String username, String password);
    void Register(Object ref, String username, String password);
    //INSERT OPERATIONS
    void AddMusic(Object ref, String username, String name, String author, String album, String year, String path);
    void AddPlaylist(Object ref, String username, String name);
    //DELETE OPERATIONS
    void RemoveMusic(Object ref, String name);
    void RemovePlaylist(Object ref, String name);
    //SELECT OPERATIONS
    void GetMusics(Object ref);
    void GetMusic(Object ref, String name);
    void GetPlaylists(Object ref);
    void GetPlaylist(Object ref, String name);
}
