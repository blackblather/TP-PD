package server.network;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPService implements INetworkService<DatagramPacket> {
    @Override
    public void Update(Object o) {

    }

    @Override
    public void SendMsg(DatagramPacket msg) throws IOException, IllegalArgumentException {

    }

    public void ReceiveMsg() throws IOException {

    }
}
