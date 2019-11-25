package client.controller;

import common.controller.Controller;
import common.network.TCPService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class ClientController extends Controller {
    private TCPService tcpService;

    public ClientController(){
        tcpService = new TCPService(new Socket(), this);
    }

    @Override
    public void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        //Example: {"Type":"Login","Content":{"username":"blackBladder","password":"blueNails"}}
        JSONObject jsonObject = new JSONObject(jsonStr);
        if (IsValidJSONFormat(jsonObject)) {
            switch (jsonObject.getString("Type")) {
                case "Login": {
                    Notify(NotificationType.successfulLogin);
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
    public void Login(Object ref, String username, String password) {
        //Convert to JSONObject
        String jsonStr = "{\"username\":\"" + username + "\"," + "\"password\":\"" + password + "\"}";
        try{
            tcpService.SendMsg(jsonStr);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AddMusic(Object ref, String username, String name, String author, String album, String year, String path) {

    }

    @Override
    public void AddPlaylist(Object ref, String username, String name) {

    }

    @Override
    public void AddUser(Object ref, String username, String password) {

    }

    @Override
    public void RemoveMusic(Object ref, String name) {

    }

    @Override
    public void RemovePlaylist(Object ref, String name) {

    }

    @Override
    public void GetMusics(Object ref) {

    }

    @Override
    public void GetMusic(Object ref, String name) {

    }

    @Override
    public void GetPlaylists(Object ref) {

    }

    @Override
    public void GetPlaylist(Object ref, String name) {

    }

}