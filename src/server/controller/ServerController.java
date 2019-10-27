package server.controller;

import server.network.INetworkService;

import java.util.ArrayList;
import java.util.List;

public class ServerController implements IController {

    private List<INetworkService> observers = new ArrayList<>();

    public ServerController(){

    }

    private void Notify(Object o){

    }

    public void AddObserver(INetworkService obs){
        if(observers.contains(obs))
            throw new IllegalArgumentException("Observer is already registered.");
        observers.add(obs);
    }

    /********************** INTERFACE IMPLEMENTATION **********************/
    @Override
    public void Login(String username, String password) {
        System.out.println("GOT LOGIN REQUEST:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    @Override
    public void AddMusic(String username, String name, String author, String album, String year, String path) {

    }

    @Override
    public void AddPlaylist(String username, String name) {

    }

    @Override
    public void AddUser(String username, String password) {

    }

    @Override
    public void RemoveMusic(String name) {

    }

    @Override
    public void RemovePlaylist(String name) {

    }

    @Override
    public void GetMusics() {

    }

    @Override
    public void GetMusic(String name) {

    }

    @Override
    public void GetPlaylists() {

    }

    @Override
    public void GetPlaylist(String name) {

    }
}