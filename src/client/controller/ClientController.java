package client.controller;

import client.network.TCPService;
import common.observable.ObservableController;
import common.observer.IObserver;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class ClientController extends ObservableController implements IObserver {
    private TCPService tcpService;

    public ClientController(){
        tcpService = new TCPService(new Socket());
        tcpService.AddObserver(this);
    }

    @Override
    public void Login(Object ref, String username, String password) {
        //Convert to JSONObject
        StringBuilder sb = new StringBuilder();
        String jsonStr = sb.append("{\"username\":\"").append(username).append("\",")
                            .append("\"password\":\"").append(password).append("\"}").toString();
        try{
            tcpService.SendMsg(jsonStr);
        } catch (IOException e) {
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

    @Override
    public void Update(Object o) {
        if (o instanceof JSONObject) {
            Notify(null, NotificationType.successfulLogin);
        }
    }
}