package server.network;

import common.controller.Controller;
import common.observer.IObserver;

import java.io.IOException;
import java.net.Socket;

public class TCPService extends common.network.TCPService implements IObserver {
    public TCPService(Socket socket, Controller controller) throws IllegalArgumentException, IOException {
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
                SendMsg("{\"Type\":\"Login\", \"Success\":true}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnInvalidCredentials(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Login\", \"Success\":false}");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnRegisterSuccess(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Register\", \"Success\":true}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnRegisterError(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Register\", \"Success\":false}");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
