package server.network;

import java.io.IOException;

public interface INetworkService{
    void SendMsg(String jsonStr) throws IOException, IllegalArgumentException;
    void ReceiveMsg();
}
