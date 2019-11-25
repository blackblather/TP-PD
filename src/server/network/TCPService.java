package server.network;

import common.controller.Controller;
import common.observer.IObserver;

import java.io.IOException;
import java.net.Socket;

public class TCPService extends common.network.TCPService implements IObserver {
    public TCPService(Socket socket, Controller controller) throws IllegalArgumentException {
        super(socket, controller);
        /*Warning: When constructing an object that will be shared between threads,
          be very careful that a reference to the object does not "leak" prematurely.
          Source: https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html*/
        this.controller.AddObserver(this);
    }

    @Override
    public void Update(Object o) {

    }

    @Override
    public void OnSuccessfulLogin(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"response_success\":true, \"response_type\":\"login\", \"response_description\":\"Very nice login\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnFailedLogin(Object ref) {

    }
}
