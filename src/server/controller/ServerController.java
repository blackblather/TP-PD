package server.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import common.CWT.Payload;
import common.CWT.Token;
import common.CWT.Tokenizer;
import common.CWT.exceptions.InvalidTokenException;
import common.controller.Controller;
import common.model.Music;
import common.thread.FileTransferThread;
import org.json.JSONException;
import org.json.JSONObject;
import server.controller.exceptions.InvalidCredentialsException;

import java.io.File;
import java.net.ServerSocket;
import java.security.InvalidKeyException;
import java.sql.*;

/*NOTA: A implementação desta aplicação não tem em consideração questões de segurança, tais como:
    -> SQLInjection
    -> Hash & Salt das passwords dos utilizadores
    -> Envio de informação encriptada
    -> Caracteres especiais obrigatórios e número mínimo de caracteres na password
*/
public class ServerController extends Controller {
    //JDBC driver name and database URL
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_NAME = "tp-pd";
    private final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;

    //Database credentials
    private final String USER = "root";
    private final String PASS = "";

    //Database vars
    Connection conn = null;
    Statement stmt = null;

    //Tokens
    private final String key = "501eaafea5db8a40f3b889709db44bea";
    private Tokenizer tokenizer;

    public ServerController(){
        try{
            //Initializes tokenizer
            tokenizer = new Tokenizer(key);

            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(Exception e){
            e.printStackTrace();
        }
/*        finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ignored){}// nothing we can do

            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try*/
    }

    //SQL Query Functions
    private Token SqlLogin(String username, String password) throws SQLException, InvalidKeyException, InvalidCredentialsException {
        String query = "SELECT COUNT(*) as 'total' from users where username = '" + username + "' AND password = '" + password + "'";
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(!rs.next() || rs.getInt("total") != 1)
            throw new InvalidCredentialsException();
        return tokenizer.GetToken(new Payload(username));
    }

    private Token SqlRegister(String username, String password) throws SQLException, InvalidKeyException {
        String query = "INSERT INTO users (id_users, username, password) VALUES (NULL, '" + username + "', '" + password + "')";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
        return tokenizer.GetToken(new Payload(username));
    }

    private void SqlAddMusic(String tokenStr, String name, String author, String album, Integer year) throws SQLException, InvalidTokenException {
        Payload payload = tokenizer.GetPayload(new Token(tokenStr));
        String path = "SOME RANDOM FILE NAME";  //TODO
        String query = "INSERT INTO musicas (id_users, id_generos, nome, autor, album, ano) VALUES ((SELECT id_users FROM users WHERE username = \"" + payload.GetUsername() + "\"),1,\"" + name + "\", \"" + author + "\", \"" + album + "\", " + year + ", \"" + path + "\")";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }

    //IController implementation
    @Override
    public synchronized void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonContent = jsonObject.getJSONObject("Content");
        switch (jsonObject.getString("Type")) {
            case "Login": {
                Login(ref, jsonContent.getString("username"), jsonContent.getString("password"));
            } break;
            case "Register": {
                Register(ref, jsonContent.getString("username"), jsonContent.getString("password"), jsonContent.getString("passwordConf"));
            } break;
            case "AddSong": {
                AddSong(ref, jsonContent.getString("token"), new Music(jsonContent.getString("name"), jsonContent.getString("author"), jsonContent.getString("album"), jsonContent.getInt("year"), new File(jsonContent.getString("filePath"))));
            } break;
            case "AddPlaylist": {/*TODO*/} break;
            case "RemoveMusic": {/*TODO*/} break;
            case "RemovePlaylist": {/*TODO*/} break;
            case "GetMusics": {/*TODO*/} break;
            case "GetMusic": {/*TODO*/} break;
            case "GetPlaylists": {/*TODO*/} break;
            case "GetPlaylist": {/*TODO*/} break;
        }
    }

    @Override
    public synchronized void Login(Object ref, String username, String password) {
        System.out.println("GOT LOGIN REQUEST:\nUsername: " + username + "\nPassword: " + password);
        try {
            Token token = SqlLogin(username, password);
            Notify(ref, NotificationType.loginSuccess, token.toString());
        } catch (InvalidCredentialsException e){
            Notify(ref, NotificationType.loginInvalidCredentials);
        } catch (Exception e) {
            Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
                }
            }
        }
    }

    @Override
    public synchronized void Register(Object ref, String username, String password, String passwordConf) {
        System.out.println("GOT REGISTER REQUEST:\nUsername: " + username + "\nPassword: " + password + "\nPassword Confirmation: " + passwordConf);
        if(password.equals(passwordConf)){
            try {
                Token token = SqlRegister(username, password);
                Notify(ref, NotificationType.registerSuccess, token.toString());
            } catch (MySQLIntegrityConstraintViolationException e){
                //No contexto desta função, a unica constraint violation possivel, é quando se tenta inserir usernames identicos
                Notify(ref, NotificationType.registerUsernameNotUnique);
            } catch (Exception e) {
                Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
                    }
                }
            }
        } else
            Notify(ref, NotificationType.registerPasswordsNotMatching);
    }

    private boolean FileNameHasValidExtension(String fileName){
        //Checks if filename terminates with ".mp3"
        return fileName.matches("^.+\\.mp3$");
    }

    //Generate newfileName, if "chosenFilePath" corresponds to existing file
    private File GetFinalFile(String originalName, String destinationFolder) throws IllegalArgumentException{
        File finalFile = new File(destinationFolder + originalName);

        if(FileNameHasValidExtension(originalName)) {
            for(int i = 1; finalFile.exists() && finalFile.isFile(); i++)
                finalFile = new File(destinationFolder + "(" + i + ") " + originalName);
            return finalFile;
        } else
            throw new IllegalArgumentException("Invalid file extension.");
    }

    @Override
    public synchronized void AddSong(Object ref, String token, Music music) {
        try {
            //Validate token before file transfer begins
            tokenizer.GetPayload(new Token(token));
            //Create server socket to accept tcp connection for file transfer
            ServerSocket fileTransferServerSocket = new ServerSocket(0);
            //Notify observers before blocking in accept(). Sends hostname and port
            Notify(ref, NotificationType.readyForUpload, fileTransferServerSocket.getInetAddress().getHostName(), fileTransferServerSocket.getLocalPort());
            //Initialize "file transfer thread"
            FileTransferThread fileTransferThread = new FileTransferThread(this, ref, fileTransferServerSocket);
            //MKDir "musicLibrary" if it doesn't exist already
            String destinationFolder = "musicLibrary\\";
            File destinationFolderFile = new File(destinationFolder);
            if(!destinationFolderFile.exists())
                destinationFolderFile.mkdir();
            //Get final file name
            File finalFile = GetFinalFile(music.getFile().getName(), destinationFolder);
            //Start "file transfer thread" for receiving file
            fileTransferThread.ReceiveFile(finalFile.getCanonicalPath());

            //SqlAddMusic(token, name, author, album, year);
            //Notify(ref, NotificationType.addSongSuccess);
        } catch (Exception e) {
            Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
        }
    }

    @Override
    public synchronized void AddPlaylist(Object ref, String token, String name) {

    }

    @Override
    public synchronized void RemoveSong(Object ref, String token, String name) {

    }

    @Override
    public synchronized void RemovePlaylist(Object ref, String token,  String name) {

    }

    @Override
    public synchronized void GetSongs(Object ref, String token) {

    }

    @Override
    public synchronized void GetSong(Object ref, String token, String name) {

    }

    @Override
    public synchronized void GetPlaylists(Object ref, String token) {

    }

    @Override
    public synchronized void GetPlaylist(Object ref, String token,  String name) {

    }

    @Override
    public void ThrowException(Object ref, Exception e) {
        Notify(ref, NotificationType.exception, e.getClass().getSimpleName());
    }

}