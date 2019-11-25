package common.network;

import common.controller.Controller;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPService implements INetworkService {
    private Socket socket;
    protected Controller controller;

    public TCPService(Socket socket, Controller controller) throws IllegalArgumentException{
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void SendMsg(String jsonStr) throws IllegalArgumentException, IOException {
        OutputStream outputStream = socket.getOutputStream();
        byte[] byteArr = jsonStr.getBytes();
        outputStream.write(byteArr);
        outputStream.flush();
    }

    @Override
    public void ReceiveMsg() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] byteArr = new byte[1024];
            int bytesRead;
            while((bytesRead = inputStream.read(byteArr, 0, 1024)) > 0) {
                String s = new String(byteArr, 0, bytesRead);
                try {
                    controller.RouteJSONStr(this, s);
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
