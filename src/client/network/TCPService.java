package client.network;

import client.controller.ClientController;
import common.network.INetworkService;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TCPService implements INetworkService {
    private Socket socket;
    private ClientController clientController;

    public TCPService(Socket socket, ClientController clientController){
        this.socket = socket;
        this.clientController = clientController;
    }

    @Override
    public void SendMsg(String jsonStr) throws IOException, IllegalArgumentException {

    }

    @Override
    public void ReceiveMsg() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] byteArr = new byte[1024];
            int bytesRead;
            while((bytesRead = inputStream.read(byteArr, 0, 1024)) > 0) {
                String msg = new String(byteArr, 0, bytesRead);
                try{
                    clientController.RouteJSONStr(msg);
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
