package client.controller;

import client.thread.ReadResponseThread;
import common.controller.Controller;
import common.model.Music;
import common.network.TCPService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

/*NOTA: A implementação desta aplicação não tem em consideração questões de segurança, tais como:
    -> SQLInjection
    -> Hash & Salt das passwords dos utilizadores
    -> Envio de informação encriptada
    -> Caracteres especiais obrigatórios e número mínimo de caracteres na password
*/
public class ClientController extends Controller {
    private TCPService tcpService;

    public ClientController(){

    }

    @Override
    public void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        //Example: {"Type":"Login","Content":{"username":"blackBladder","password":"blueNails"}}
        JSONObject jsonObject = new JSONObject(jsonStr);
        switch (jsonObject.getString("Type")) {
            case "Exception": {
                Notify(NotificationType.exception, jsonObject.getString("ExceptionName"));
            } break;
            case "ServerStatus": {/*TODO*/} break;
            case "Login": {
                if(jsonObject.getBoolean("Success"))
                    Notify(NotificationType.loginSuccess, jsonObject.getString("token"));
                else
                    Notify(NotificationType.loginInvalidCredentials);
            } break;
            case "Register": {
                if(jsonObject.getBoolean("Success"))
                    Notify(NotificationType.registerSuccess, jsonObject.getString("token"));
                else
                    switch (jsonObject.getString("ErrorType")){
                        case "PasswordsNotMatching": Notify(NotificationType.registerPasswordsNotMatching); break;
                        case "UsernameNotUnique": Notify(NotificationType.registerUsernameNotUnique); break;
                    }

            } break;
            case "AddSong": {
                if(jsonObject.getBoolean("Success"))
                    Notify(NotificationType.addSongSuccess);
            } break;
            case "ReadyForUpload":{
                Notify(NotificationType.readyForUpload, jsonObject.getString("hostname"), jsonObject.getInt("port"));
            }
            case "AddPlaylist": {/*TODO*/} break;
            case "RemoveMusic": {/*TODO*/} break;
            case "RemovePlaylist": {/*TODO*/} break;
            case "GetMusics": {/*TODO*/} break;
            case "GetMusic": {/*TODO*/} break;
            case "GetPlaylists": {/*TODO*/} break;
            case "GetPlaylist": {/*TODO*/} break;
        }
    }

    private void TryConnectingToServer(String jsonStr){
        try{
            tcpService = new TCPService(new Socket("localhost", 6002), this);
            tcpService.SendMsg(jsonStr);
            ReadResponseThread readResponseThread = new ReadResponseThread(tcpService);
            readResponseThread.start();
        } catch (IOException | IllegalArgumentException e) {
            Notify(NotificationType.exception);
        }
    }

    @Override
    public void Login(Object ref, String username, String password) {
        //Convert to JSONObject
        String jsonStr = "{\"Type\":\"Login\",\"Content\":{\"username\":\""+username+"\",\"password\":\""+password+"\"}}";
        TryConnectingToServer(jsonStr);
    }

    @Override
    public synchronized void Register(Object ref, String username, String password, String passwordConf) {
        //Convert to JSONObject
        String jsonStr = "{\"Type\":\"Register\",\"Content\":{\"username\":\""+username+"\",\"password\":\""+password+"\", \"passwordConf\":\""+passwordConf+"\"}}";
        TryConnectingToServer(jsonStr);
    }

    @Override
    public void UploadSongRequest(Object ref, String token, Music music) {
        try {
            //Convert to JSONObject
            String jsonStr = "{\"Type\":\"UploadSongRequest\",\"Content\":{\"token\":\"" + token + "\", \"name\":\"" + music.getName() + "\", \"author\":\"" + music.getAuthor() + "\", \"album\":\"" + music.getAlbum() + "\", \"year\":" + music.getYear() + ", \"filePath\":\"" + music.getFile().getCanonicalPath().replace("\\", "\\\\") + "\"}}";
            tcpService.SendMsg(jsonStr);
        } catch (IOException e){
            Notify(NotificationType.exception);
        }
    }

    @Override
    public void DownloadSong(Object ref, String token, Music music) { }

    /*@Override
    public void AddSong(Object ref, String token, Music music) { *//*EMPTY BODY*//* }*/

    @Override
    public void AddPlaylist(Object ref, String token, String name) { }

    @Override
    public void RemoveSong(Object ref, String token, String name) { }

    @Override
    public void RemovePlaylist(Object ref, String token, String name) { }

    @Override
    public void GetSongs(Object ref, String token) { }

    @Override
    public void GetSong(Object ref, String token, String name) { }

    @Override
    public void GetPlaylists(Object ref, String token) { }

    @Override
    public void GetPlaylist(Object ref, String token, String name) { }

    @Override
    public void ThrowException(Object ref, Exception e) {

    }

}