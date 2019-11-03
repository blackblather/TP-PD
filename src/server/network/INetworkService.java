package server.network;

import common.model.ServerResponse;
import org.json.JSONObject;

import java.io.IOException;

public interface INetworkService{
    void Update(ServerResponse resp);
    void SendMsg(JSONObject jsonObject) throws IOException, IllegalArgumentException;
    void ReceiveMsg();
}
