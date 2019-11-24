package client.network;

import common.model.Response;
import common.network.INetworkService;
import common.observable.Observable;
import common.observable.ObservableController;
import common.observer.IObserver;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TCPService extends Observable<IObserver> implements INetworkService {
    private Socket socket;

    public TCPService(Socket socket){
        this.socket = socket;
    }

    private Response DecodeJSONResp(JSONObject jsonObject){
        return new Response(true,"","",null);   //TODO
    }

    @Override
    public void SendMsg(String jsonStr) throws IOException, IllegalArgumentException {

        Notify(new JSONObject(jsonStr));
    }

    @Override
    public void ReceiveMsg() {
        try {
            Response resp;
            InputStream inputStream = socket.getInputStream();
            byte[] byteArr = new byte[1024];
            int bytesRead;
            while((bytesRead = inputStream.read(byteArr, 0, 1024)) > 0) {
                String msg = new String(byteArr, 0, bytesRead);
                try{
                    //Notify(DecodeJSONResp(new JSONObject(msg)));
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
