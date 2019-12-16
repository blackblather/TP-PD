package common.controller;

import org.json.JSONException;

public interface IController {
    void RouteJSONStr(Object ref, String jsonStr) throws JSONException;
    void Login(Object ref, String username, String password);
    void Register(Object ref, String username, String password, String passwordConf);
    //INSERT OPERATIONS
    void AddMusic(Object ref, String token, String name, String author, String album, String year, String path);
    void AddPlaylist(Object ref, String token, String name);
    //DELETE OPERATIONS
    void RemoveMusic(Object ref, String token, String name);
    void RemovePlaylist(Object ref, String token,  String name);
    //SELECT OPERATIONS
    void GetMusics(Object ref, String token);
    void GetMusic(Object ref, String token,  String name);
    void GetPlaylists(Object ref, String token);
    void GetPlaylist(Object ref, String token,  String name);
}
