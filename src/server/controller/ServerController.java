package server.controller;

import common.model.ServerResponse;
import server.network.INetworkService;

import java.util.ArrayList;
import java.util.List;

public class ServerController implements IController {

    private List<INetworkService> observers = new ArrayList<>();

    public ServerController(){

    }

    private void Notify(ServerResponse response){
        if(response != null)
            for (INetworkService observer : observers)
                observer.Update(response);
    }

    public void AddObserver(INetworkService obs){
        if(observers.contains(obs))
            throw new IllegalArgumentException("Observer is already registered.");
        observers.add(obs);
    }

    /********************** INTERFACE IMPLEMENTATION **********************/
    @Override
    public void Login(Object ref, String username, String password) {
        System.out.println("GOT LOGIN REQUEST:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        Notify(new ServerResponse(true, "login", "Login Successful", ref));
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