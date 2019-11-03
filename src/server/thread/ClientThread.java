package server.thread;

import server.controller.ServerController;
import server.network.TCPService;

import java.net.Socket;

public class ClientThread implements Runnable {

    private TCPService tcpService;

    //Synchronization vars
    private int[] connectedClients;
    private final Object connectedClientsLock;

    ClientThread(Socket socket, ServerController controller, final Object connectedClientsLock, int[] connectedClients){
        this.connectedClients = connectedClients;
        this.connectedClientsLock = connectedClientsLock;
        this.tcpService = new TCPService(socket, controller);
    }

    @Override
    public void run() {
        tcpService.ReceiveMsg();
        DecrementConnectedClients();
    }

    private void DecrementConnectedClients(){
        synchronized (connectedClientsLock) {
            connectedClients[0]--;
        }
    }
}
