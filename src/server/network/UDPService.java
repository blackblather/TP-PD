package server.network;

import common.model.Response;
import common.observer.IObserver;

import java.io.IOException;

public class UDPService implements INetworkService, IObserver {
    @Override
    public void Update(Response resp) {

    }

    @Override
    public void SendMsg(String jsonStr) throws IOException, IllegalArgumentException {

    }

    @Override
    public void ReceiveMsg() {

    }
}
