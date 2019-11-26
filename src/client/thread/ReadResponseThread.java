package client.thread;

import common.network.TCPService;

public class ReadResponseThread extends Thread {
    private TCPService tcpService;

    public ReadResponseThread(TCPService tcpService){
        this.tcpService = tcpService;
    }

    @Override
    public void run() {
        tcpService.ReceiveMsg();
    }
}
