package common.thread;

import common.CWT.Payload;
import common.controller.Controller;
import common.model.Music;
import server.controller.ServerController;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/*
 * Class to encapsulate file transfer operations in the context of this project
 * NOT built for general use purpose
 */
public class FileTransferThread {
    private enum RequestType{
        serverRequest,
        clientRequest
    }
    private final RequestType requestType;
    //File control vars
    private final int byteArrayChunkSize = 4096;    //4Mb
    private final int timeout = 10*1000;            //10s
    private final byte[] chunk = new byte[byteArrayChunkSize];
    //Socket control vars
    private Socket fileTransferSocket;
    private final ServerSocket fileTransferServerSocket;
    private final String hostname;
    private final Integer port;
    //Thread control vars
    private Thread fileTransferThread;
    //Model control vars
    private final Music music;
    //Notification control vars
    private final Controller controller;
    private final Payload payload;
    private final Object ref;

    public FileTransferThread(Controller controller, Object ref, Payload payload, Music music, ServerSocket fileTransferServerSocket) throws IllegalArgumentException {
        if(controller == null || ref == null || payload == null || music == null || fileTransferServerSocket == null)
            throw new IllegalArgumentException("Arguments must not be null.");

        this.requestType = RequestType.serverRequest;

        this.controller = controller;
        this.payload = payload;
        this.ref = ref;
        this.music = music;
        this.fileTransferServerSocket = fileTransferServerSocket;
        this.hostname = null;
        this.port = null;
    }

    public FileTransferThread(Controller controller, String hostname, Integer port) throws IllegalArgumentException {
        if(controller == null || hostname == null || port == null)
            throw new IllegalArgumentException("Arguments must not be null.");

        this.requestType = RequestType.clientRequest;

        this.controller = controller;
        this.payload = null;
        this.ref = null;
        this.music = null;
        this.fileTransferServerSocket = null;
        this.hostname = hostname;
        this.port = port;
    }

    //Initializes tcp socket differently, depending on who's using this class, client or server
    private void InitSocket() throws IOException {
        try{
            if(requestType == RequestType.clientRequest){ //Client
                fileTransferSocket = new Socket();
                fileTransferSocket.connect(new InetSocketAddress(hostname, port), timeout);
            } else {    //Server
                fileTransferServerSocket.setSoTimeout(timeout);
                fileTransferSocket = fileTransferServerSocket.accept();
            }
        } catch (SocketTimeoutException e){
            controller.ThrowException(ref, e);
        }
    }

    public void SendFile(String path){
        fileTransferThread = new Thread(() -> {
            try {
                //Initializes file transfer socket
                InitSocket();

                //Control vars
                FileInputStream fileInputStream = new FileInputStream(path);
                OutputStream outputStream = fileTransferSocket.getOutputStream();

                //Write chunks from file to outputStream
                while(fileInputStream.read(chunk) > 0) {
                    outputStream.write(chunk);
                    outputStream.flush();
                }

                //Close handles
                outputStream.close();
                fileInputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileTransferThread.start();
    }

    public void ReceiveFile(String path){
        fileTransferThread = new Thread(() -> {
            try {
                //Initializes file transfer socket
                InitSocket();

                //Control vars
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                InputStream inputStream = fileTransferSocket.getInputStream();

                //Write chunks from inputStream to file
                while(inputStream.read(chunk) > 0) {
                    fileOutputStream.write(chunk);
                    fileOutputStream.flush();
                }

                //Close handles
                inputStream.close();
                fileOutputStream.close();

                if(controller instanceof ServerController)
                    ((ServerController)controller).AddSong(ref, payload, music);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileTransferThread.start();
    }
}
