package server.controller;

import common.controller.Controller;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerController extends Controller {

    public ServerController(){

    }

    private boolean IsValidJSONRRequest(JSONObject jsonObject){
        return true;
    }

    @Override
    public synchronized void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        if (IsValidJSONRRequest(jsonObject)) {
            JSONObject jsonContent = jsonObject.getJSONObject("Content");
            switch (jsonObject.getString("Type")) {
                case "Login": {
                    Login(ref, jsonContent.getString("username"), jsonContent.getString("password"));
                } break;
                case "AddMusic": {/*TODO*/} break;
                case "AddPlaylist": {/*TODO*/} break;
                case "AddUser": {/*TODO*/} break;
                case "RemoveMusic": {/*TODO*/} break;
                case "RemovePlaylist": {/*TODO*/} break;
                case "GetMusics": {/*TODO*/} break;
                case "GetMusic": {/*TODO*/} break;
                case "GetPlaylists": {/*TODO*/} break;
                case "GetPlaylist": {/*TODO*/} break;
            }
        }
    }

    @Override
    public synchronized void Login(Object ref, String username, String password) {
        System.out.println("GOT LOGIN REQUEST:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        Notify(ref, NotificationType.successfulLogin);
    }

    @Override
    public synchronized void AddMusic(Object ref, String username, String name, String author, String album, String year, String path) {

    }

    @Override
    public synchronized void AddPlaylist(Object ref, String username, String name) {

    }

    @Override
    public synchronized void AddUser(Object ref, String username, String password) {

    }

    @Override
    public synchronized void RemoveMusic(Object ref, String name) {

    }

    @Override
    public synchronized void RemovePlaylist(Object ref, String name) {

    }

    @Override
    public synchronized void GetMusics(Object ref) {

    }

    @Override
    public synchronized void GetMusic(Object ref, String name) {

    }

    @Override
    public synchronized void GetPlaylists(Object ref) {

    }

    @Override
    public synchronized void GetPlaylist(Object ref, String name) {

    }

}