package client.communication;

import java.net.InetAddress;

public class Communication implements ICommunication {
    private InetAddress addrDS;
    public Communication(InetAddress addrDS){
        this.addrDS = addrDS;
    }

    //TODO: SendCommand(String cmd)
    public void SendCommand(String cmd){

    }
}
