package server.controller;

import common.observable.ObservableController;

public class ServerController extends ObservableController {

    public ServerController(){

    }

    @Override
    public synchronized void Login(Object ref, String username, String password) {
        System.out.println("GOT LOGIN REQUEST:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        Notify(ref, NotificationType.successfulLogin);
        //Notify(new Response(true, "Login", "Login Successful", ref));
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