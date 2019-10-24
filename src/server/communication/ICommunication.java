package server.communication;

import java.io.IOException;

public interface ICommunication<T>{
    void Update(Object o);
    void SendMsg(T msg) throws IOException, IllegalArgumentException;
}
