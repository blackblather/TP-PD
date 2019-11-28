package server.thread;

import server.controller.ServerController;
import server.network.TCPService;

import java.io.IOException;
import java.net.Socket;

public class ClientRunnable implements Runnable {
    private TCPService tcpService;

    //Synchronization vars
    private int[] connectedClients;
    private final Object connectedClientsLock;

    ClientRunnable(Socket socket, ServerController controller, final Object connectedClientsLock, int[] connectedClients) throws IOException {
        this.connectedClients = connectedClients;
        this.connectedClientsLock = connectedClientsLock;
        this.tcpService = new TCPService(socket, controller);
    }

    @Override
    public void run() {
        tcpService.ReceiveMsg();
        DecrementConnectedClients();
        System.out.println("Client connection closed.");
    }

    private void DecrementConnectedClients(){
        synchronized (connectedClientsLock) {
            connectedClients[0]--;
        }
    }
}
