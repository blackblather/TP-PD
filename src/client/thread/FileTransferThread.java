package client.thread;

import common.controller.Controller;

public class FileTransferThread extends Thread {
    private Controller controller;

    public FileTransferThread(Controller controller){
        this.controller = controller;
    }

    @Override
    public void run() {

    }
}
