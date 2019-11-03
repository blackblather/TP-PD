package server.network;

import org.json.JSONException;
import org.json.JSONObject;
import common.model.ServerResponse;
import server.controller.ServerController;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TCPService implements INetworkService {
    private Socket socket;
    private ServerController controller;

    public TCPService(Socket socket, ServerController controller) throws IllegalArgumentException{
        this.socket = socket;
        this.controller = controller;
        this.controller.AddObserver(this);
    }

    private void RouteMsg(String msg) throws JSONException{
        JSONObject jsonMsg = new JSONObject(msg);
        if(jsonMsg.has("Type") && jsonMsg.has("Content")){
            JSONObject jsonContent = jsonMsg.getJSONObject("Content");
            switch(jsonMsg.getString("Type")){
                case "Login": {
                    if(jsonContent.has("username") && jsonContent.has("password"))
                        controller.Login(this, jsonContent.getString("username"), jsonContent.getString("password"));             //LOGIN
                    else
                        throw new JSONException("JSON message format error.\nMissing one or more fields:\n - username\n - password");
                } break;
                case "AddMusic": {
                    //TODO: Validations
                } break;

            }
        } else
            throw new JSONException("JSON message format error.\nMissing one or more fields:\n - Type\n - Content");
    }

    @Override
    public void Update(ServerResponse resp) {
    }

    @Override
    public void SendMsg(JSONObject jsonObject) throws IOException, IllegalArgumentException {

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
                    RouteMsg(s);
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
