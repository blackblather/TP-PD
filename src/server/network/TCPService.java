package server.network;

import common.network.INetworkService;
import common.observer.Observer;
import org.json.JSONException;
import server.controller.ServerController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPService extends Observer implements INetworkService {
    private Socket socket;
    private ServerController controller;

    public TCPService(Socket socket, ServerController controller) throws IllegalArgumentException{
        this.socket = socket;
        this.controller = controller;
        /*Warning: When constructing an object that will be shared between threads,
          be very careful that a reference to the object does not "leak" prematurely.
          Source: https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html*/
        this.controller.AddObserver(this);
    }

    @Override
    public void OnSuccessfulLogin(Object ref) {
        if(ref == this)
            SendMsg("{\"response_success\":true, \"response_type\":\"login\", \"response_description\":\"Very nice login\"}");
    }

    @Override
    public void SendMsg(String jsonStr) throws IllegalArgumentException {
        try{
            OutputStream outputStream = socket.getOutputStream();
            byte[] byteArr = jsonStr.getBytes();
            outputStream.write(byteArr);
            outputStream.flush();
        } catch (IOException e){
            System.out.println("IOException. Error message: " + e.getMessage());
        }
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
                    controller.RouteJSONStr(s);
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
