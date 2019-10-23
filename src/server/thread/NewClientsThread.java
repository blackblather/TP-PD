package server.thread;

import server.communication.TCPCommunication;
import server.controller.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class NewClientsThread extends Thread {
    private ServerSocket serverSocket;
    private ServerController controller;
    private List<Thread> clientThreads = new ArrayList<>();

    public NewClientsThread(ServerSocket serverSocket, ServerController controller){
        this.serverSocket = serverSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        //Created thread for accepting new clients
        while (true){
            try {
                TCPCommunication tcpCommunication = new TCPCommunication(serverSocket.accept(), controller);
                clientThreads.add(tcpCommunication);
                tcpCommunication.start();
            } catch (IOException e){ }
        }
    }
}