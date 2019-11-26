package common.network;

import common.controller.Controller;

import java.io.IOException;

public interface INetworkService{
    void SendMsg(String jsonStr) throws IOException, IllegalArgumentException;
    void ReceiveMsg();
}
