package common.network;

import common.controller.Controller;
import org.json.JSONException;

import java.io.*;
import java.net.Socket;

public class TCPService implements INetworkService {
    protected Socket socket;
    protected Controller controller;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public TCPService(Socket socket, Controller controller) throws IllegalArgumentException, IOException {
        this.socket = socket;
        this.controller = controller;
        printWriter = new PrintWriter(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void SendMsg(String jsonStr) throws IllegalArgumentException, IOException {
        printWriter.println(jsonStr);
        printWriter.flush();
    }

    @Override
    public void ReceiveMsg() {
        try {
            String jsonString;
            while((jsonString = bufferedReader.readLine()) != null) {
                try {
                    controller.RouteJSONStr(this, jsonString);
                } catch (JSONException e){
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.println("IOException. Error message: " + e.getMessage());
        }
    }
}
