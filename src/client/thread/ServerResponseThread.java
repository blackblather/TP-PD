package client.thread;

import common.network.TCPService;

public class ServerResponseThread extends Thread {
    private TCPService tcpService;

    public ServerResponseThread(TCPService tcpService){
        this.tcpService = tcpService;
    }

    @Override
    public void run() {
        tcpService.ReceiveMsg();
    }
}
