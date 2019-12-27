package common.controller;

import common.model.Music;
import org.json.JSONException;

public interface IController {
    void RouteJSONStr(Object ref, String jsonStr) throws JSONException;
    void Login(Object ref, String username, String password);
    void Register(Object ref, String username, String password, String passwordConf);
    //INSERT OPERATIONS
    void AddSong(Object ref, String token, Music music);
    void AddPlaylist(Object ref, String token, String name);
    //DELETE OPERATIONS
    void RemoveSong(Object ref, String token, String name);
    void RemovePlaylist(Object ref, String token,  String name);
    //SELECT OPERATIONS
    void GetSongs(Object ref, String token);
    void GetSong(Object ref, String token, String name);
    void GetPlaylists(Object ref, String token);
    void GetPlaylist(Object ref, String token,  String name);
}
