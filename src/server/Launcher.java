package server;

import server.controller.ServerController;
import server.thread.NewClientsThread;

import java.io.IOException;
import java.net.ServerSocket;

public class Launcher {
    //private final String multicastAddr = "224.0.0.120";
    //private final Integer multicastPort = 6001;
    public static void main(String [] args){
        //Syntax: java server.communication.Launcher PORT-TCP IP-DS PORT-DS IP-DB PORT-DB
        if(args.length != 5){
            System.out.println("Invalid syntax.\nExpected syntax: java server.communication.Launcher PORT-TCP IP-DS PORT-DS IP-DB PORT-DB");
            System.exit(0);
        }

        try{
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            ServerController serverController = new ServerController();

            NewClientsThread newClientsThread = new NewClientsThread(serverSocket, serverController);     //Creates thread for accepting new client TCP connections
            newClientsThread.start();

        } catch (IOException e){
            System.out.println("Error creating tcp server socket.\nError message: " + e.getMessage());
        }
    }
}
