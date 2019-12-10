package client.controller;

import client.thread.ReadResponseThread;
import common.controller.Controller;
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

    private boolean IsValidJSONResponse(JSONObject jsonObject){
        //TODO
        return true;
    }

    @Override
    public void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        //Example: {"Type":"Login","Content":{"username":"blackBladder","password":"blueNails"}}
        JSONObject jsonObject = new JSONObject(jsonStr);
        if (IsValidJSONResponse(jsonObject)) {
            switch (jsonObject.getString("Type")) {
                case "Exception": {
                    Notify(NotificationType.exception, jsonObject.getInt("ErrorCode"), jsonObject.getString("Message"));
                } break;
                case "ServerStatus": {/*TODO*/} break;
                case "Login": {
                    if(jsonObject.getBoolean("Success"))
                        Notify(NotificationType.loginSuccess);
                    else
                        Notify(NotificationType.loginInvalidCredentials);
                } break;
                case "Register": {
                    if(jsonObject.getBoolean("Success"))
                        Notify(NotificationType.registerSuccess);
                    else
                        switch (jsonObject.getString("ErrorType")){
                            case "PasswordsNotMatching": Notify(NotificationType.registerPasswordsNotMatching); break;
                            case "UsernameNotUnique": Notify(NotificationType.registerUsernameNotUnique); break;
                        }

                } break;
                case "AddMusic": {/*TODO*/} break;
                case "AddPlaylist": {/*TODO*/} break;
                case "RemoveMusic": {/*TODO*/} break;
                case "RemovePlaylist": {/*TODO*/} break;
                case "GetMusics": {/*TODO*/} break;
                case "GetMusic": {/*TODO*/} break;
                case "GetPlaylists": {/*TODO*/} break;
                case "GetPlaylist": {/*TODO*/} break;
            }
        }
    }

    @Override
    public void Login(Object ref, String username, String password) {
        //Convert to JSONObject
        String jsonStr = "{\"Type\":\"Login\",\"Content\":{\"username\":\""+username+"\",\"password\":\""+password+"\"}}";
        try{
            tcpService = new TCPService(new Socket("localhost", 6002), this);
            tcpService.SendMsg(jsonStr);
            ReadResponseThread readResponseThread = new ReadResponseThread(tcpService);
            readResponseThread.start();
        } catch (IOException | IllegalArgumentException e) {
            Notify(NotificationType.loginInvalidCredentials);
        }
    }

    @Override
    public synchronized void Register(Object ref, String username, String password, String passwordConf) {
        //Convert to JSONObject
        String jsonStr = "{\"Type\":\"Register\",\"Content\":{\"username\":\""+username+"\",\"password\":\""+password+"\", \"passwordConf\":\""+passwordConf+"\"}}";
        try{
            tcpService = new TCPService(new Socket("localhost", 6002), this);
            tcpService.SendMsg(jsonStr);
            ReadResponseThread readResponseThread = new ReadResponseThread(tcpService);
            readResponseThread.start();
        } catch (IOException | IllegalArgumentException e) {
            Notify(NotificationType.loginInvalidCredentials);
        }
    }

    @Override
    public void AddMusic(Object ref, String username, String name, String author, String album, String year, String path) {

    }

    @Override
    public void AddPlaylist(Object ref, String username, String name) {

    }

    @Override
    public void RemoveMusic(Object ref, String name) {

    }

    @Override
    public void RemovePlaylist(Object ref, String name) {

    }

    @Override
    public void GetMusics(Object ref) {

    }

    @Override
    public void GetMusic(Object ref, String name) {

    }

    @Override
    public void GetPlaylists(Object ref) {

    }

    @Override
    public void GetPlaylist(Object ref, String name) {

    }

}