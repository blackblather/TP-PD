package server.network;

import java.io.IOException;
import java.net.*;

/***
 * @author João Monteiro
 * Description: Provides mechanisms used for multicast communication between servers
 ***/
public class MulticastService implements INetworkService<String> {
    private final Integer MAX_PACKET_SIZE = 4000; //bytes
    private InetAddress  multicastAddr;
    private MulticastSocket multicastSocket;
    private Integer multicastPort;

    public MulticastService(InetAddress multicastAddr, Integer port) throws IOException, IllegalArgumentException {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Port out of range (0 - 65535):" + port);   //Não valida se porta está ou não reservada
        //MULTICAST DOCUMENTATION
        //https://www.baeldung.com/java-broadcast-multicast
        //MULTICAST EXAMPLE
        //https://docs.oracle.com/javase/8/docs/api/java/net/MulticastSocket.html
        this.multicastAddr = multicastAddr;
        multicastPort = port;
        multicastSocket = new MulticastSocket(port);
        multicastSocket.joinGroup(multicastAddr);
    }

    @Override
    public void Update(Object o) {

    }

    @Override
    public void SendMsg(String obj) throws IOException, IllegalArgumentException {
        byte[] bytes = obj.getBytes();

        if(bytes.length > MAX_PACKET_SIZE)
            throw new IllegalArgumentException("Message exceeds size limit of " + MAX_PACKET_SIZE);

        DatagramPacket p = new DatagramPacket(bytes, bytes.length, multicastAddr, multicastPort);
        multicastSocket.send(p);
    }

    public void ReceiveMsg() throws IOException {
        DatagramPacket p = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        multicastSocket.receive(p);
        String text = new String(p.getData(), 0, p.getLength());
    }
}
