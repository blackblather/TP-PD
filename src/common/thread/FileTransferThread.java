package common.thread;

import common.observable.Observable;

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
    //Control vars
    private final int byteArrayChunkSize = 4096;    //4Mb
    private final int timeout = 10*1000;            //10s
    private final byte[] chunk = new byte[byteArrayChunkSize];
    private Socket fileTransferSocket;
    private final ServerSocket fileTransferServerSocket;
    private final String hostname;
    private final Integer port;
    private Thread fileTransferThread;
    //Notification vars
    private final Observable observable;
    private final Object ref;

    public FileTransferThread(Observable observable, Object ref, ServerSocket fileTransferServerSocket) throws IOException {
        this.observable = observable;
        this.ref = ref;
        this.fileTransferServerSocket = fileTransferServerSocket;
        this.hostname = null;
        this.port = null;
    }

    public FileTransferThread(Observable observable, String hostname, Integer port) {
        this.observable = observable;
        this.ref = null;
        this.fileTransferServerSocket = null;
        this.hostname = hostname;
        this.port = port;
    }

    private void InitSocket() throws IOException {
        try{
            if(fileTransferServerSocket == null && hostname != null && port != null){
                fileTransferSocket = new Socket();
                fileTransferSocket.connect(new InetSocketAddress(hostname, port), timeout);
            } else {
                fileTransferServerSocket.setSoTimeout(timeout);
                fileTransferSocket = fileTransferServerSocket.accept();
            }
        } catch (SocketTimeoutException e){
            observable.Notify(ref, Observable.NotificationType.exception, e.getClass().getSimpleName());
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileTransferThread.start();
    }
}
