package server.thread;

import org.json.JSONObject;
import server.controller.ServerController;
import server.network.TCPService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewClientsThread extends Thread {
    private ServerSocket serverSocket;
    private ServerController controller;

    //Synchronization vars
    private boolean isThreadStopped = false;
    private final Integer maxClients = 2;
    private ExecutorService tcpService = Executors.newFixedThreadPool(maxClients);
    private int[] connectedClients = {0};
    private final Object connectedClientsLock = new Object();

    public NewClientsThread(ServerSocket serverSocket, ServerController controller){
        this.serverSocket = serverSocket;
        this.controller = controller;
    }

    //EXAMPLE JSON REQUEST: {"Type":"Login","Content":{"username":"blackBladder","password":"blueNails"}}
    //EXAMPLE JSON RESPONSE {"Response_Code":0,"Response_Description":"Server if full"}

    @Override
    public void run() {
        //Created thread for accepting new clients
        Socket tmpClientSocket;
        OutputStream outputStream;
        JSONObject JSONResponse;
        while (!isThreadStopped()){
            try {
                tmpClientSocket = serverSocket.accept();
                outputStream = tmpClientSocket.getOutputStream();
                if(ServerIsFull()){
                    JSONResponse = new JSONObject("{\"Response_Code\":0,\"Response_Description\":\"Server if full\"}");
                    outputStream.write(JSONResponse.toString().getBytes());
                } else {
                    JSONResponse = new JSONObject("{\"Response_Code\":1,\"Response_Description\":\"Client accepted\"}");
                    outputStream.write(JSONResponse.toString().getBytes());
                    tcpService.execute(new TCPService(tmpClientSocket, controller, connectedClientsLock, connectedClients));
                    IncrementConnectedClients();
                }
            } catch (IOException e){
                System.out.println("Error accepting client connection or writing server response.\nError message: " + e.getMessage());
            }
        }
        System.out.println("Server stopped");
    }

    private void IncrementConnectedClients(){
        /*Não usar método synchronized.
          Não quero evitar chamadas simultâneas a este método.
          Quero evitar alterações/leituas simultaneas à variável "connectedClients", passada por
          referência para as threads TCPService.
          Usava o método syncronized se houvessem 2+ threads a chamar a mesma função do mesmo objecto*/
        synchronized (connectedClientsLock){
            connectedClients[0]++;
        }
    }

    private boolean ServerIsFull(){
        synchronized (connectedClientsLock) {
            return connectedClients[0] == maxClients;
        }
    }

    private synchronized boolean isThreadStopped() {
        return this.isThreadStopped;
    }

    public synchronized void stopThread() throws IOException{
        this.serverSocket.close();
        this.isThreadStopped = true;
    }
}