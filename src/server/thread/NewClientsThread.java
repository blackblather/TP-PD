package server.thread;

import server.controller.ServerController;
import server.network.TCPService;

import java.io.IOException;
import java.net.ServerSocket;

public class NewClientsThread extends Thread {
    private ServerSocket serverSocket;
    private ServerController controller;
    private boolean isThreadStopped = false;

    public NewClientsThread(ServerSocket serverSocket, ServerController controller){
        this.serverSocket = serverSocket;
        this.controller = controller;
    }

    //EXAMPLE JSON REQUEST: {"Type":"Login","Content":{"username":"blackBladder","password":"blueNails"}}

    @Override
    public void run() {
        //Created thread for accepting new clients
        while (!isThreadStopped()){
            try {
                new TCPService(serverSocket.accept(), controller).start();
            } catch (IOException e){
                System.out.println("Error accepting client connection.\nError message: " + e.getMessage());
            }
        }
        System.out.println("Server stopped");
    }

    private synchronized boolean isThreadStopped() {
        return this.isThreadStopped;
    }

    public synchronized void stopThread() throws IOException{
        this.serverSocket.close();
        this.isThreadStopped = true;
    }
}