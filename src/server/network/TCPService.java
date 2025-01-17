package server.network;

import common.controller.Controller;
import common.model.Music;
import common.observer.IObserver;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

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
    public void OnLoginSuccess(Object ref, String token) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Login\", \"Success\":true, \"token\":\"" + token + "\"}");
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
    public void OnRegisterSuccess(Object ref, String token) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Register\", \"Success\":true, \"token\":\"" + token + "\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnPasswordsNotMatching(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Register\", \"Success\":false, \"ErrorType\":\"PasswordsNotMatching\"}");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnUsernameNotUnique(Object ref) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Register\", \"Success\":false, \"ErrorType\":\"UsernameNotUnique\"}");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnAddSongSuccess(Object ref, Music music) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"AddSong\", \"Success\":true, \"Content\":{\"name\":\"" + music.getName() + "\", \"author\":\"" + music.getAuthor() + "\", \"album\":\"" + music.getAlbum() + "\", \"year\":" + music.getYear() + "}}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnReadyForUpload(Object ref, String hostname, Integer port) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"ReadyForUpload\", \"hostname\":\"" + hostname + "\", \"port\":" + port + "}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnReadyForDownload(Object ref, String hostname, Integer port) {

    }

    @Override
    public void OnGetSongsSuccess(Object ref, List<Music> musics) {
        if(ref == this){
            try {
                StringBuilder JSONString = new StringBuilder("{\"Type\":\"GetSongs\", \"Success\":true, \"Songs\":[");
                for(int i = 0; i < musics.size(); i++){
                    Music music = musics.get(i);
                    JSONString.append("{\"name\":\"").append(music.getName()).append("\", \"author\":\"").append(music.getAuthor()).append("\", \"album\":\"").append(music.getAlbum()).append("\", \"year\":").append(music.getYear()).append("}");
                    if(i < (musics.size() - 1))
                        JSONString.append(",");
                }
                JSONString.append("]}");
                SendMsg(JSONString.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnExceptionOccurred(Object ref, String exceptionName) {
        if (ref == this) {
            try {
                SendMsg("{\"Type\":\"Exception\", \"ExceptionName\":\"" + exceptionName + "\"}");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
