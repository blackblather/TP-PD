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
import java.io.IOException;
import java.net.ServerSocket;
import java.security.InvalidKeyException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private void SqlAddMusic(Payload payload, Music music) throws SQLException, IOException {
        String query = "INSERT INTO musicas (id_users, id_generos, nome, autor, album, ano, path) VALUES ((SELECT id_users FROM users WHERE username = \"" + payload.GetUsername() + "\"),1,\"" + music.getName() + "\", \"" + music.getAuthor() + "\", \"" + music.getAlbum() + "\", " + music.getYear() + ", \"" + music.getFile().getCanonicalPath().replace("\\", "\\\\") + "\")";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }

    private List<Music> SqlGetSongs() throws SQLException {
        ArrayList<Music> musics = new ArrayList<>();

        String query = "SELECT * from musicas";
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next())
            musics.add(new Music(rs.getString("nome"), rs.getString("autor"), rs.getString("album"), rs.getInt("ano")));

        return musics;
    }

    //IController implementation
    @Override
    public synchronized void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonContent = jsonObject.getJSONObject("Content");
        switch (jsonObject.getString("Type")) {
            case "Login": Login(ref, jsonContent.getString("username"), jsonContent.getString("password")); break;
            case "Register": Register(ref, jsonContent.getString("username"), jsonContent.getString("password"), jsonContent.getString("passwordConf")); break;
            case "UploadSongRequest": UploadSongRequest(ref, jsonContent.getString("token"), new Music(jsonContent.getString("name"), jsonContent.getString("author"), jsonContent.getString("album"), jsonContent.getInt("year"), new File(jsonContent.getString("filePath")))); break;
            case "AddPlaylist": {/*TODO*/} break;
            case "RemoveMusic": {/*TODO*/} break;
            case "RemovePlaylist": {/*TODO*/} break;
            case "GetSongs": {
                GetSongs(ref, jsonContent.getString("token"));
            } break;
            case "GetSong": {/*TODO*/} break;
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
            ThrowException(ref, e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    ThrowException(ref, e);
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
                ThrowException(ref, e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        ThrowException(ref, e);
                    }
                }
            }
        } else
            Notify(ref, NotificationType.registerPasswordsNotMatching);
    }

    //Generate newfileName, if "chosenFilePath" corresponds to existing file
    private File GetFinalFile(String originalName, String destinationFolder) throws IllegalArgumentException{
        File finalFile = new File(destinationFolder + originalName);

        //Validate file extension
        if(originalName.matches("^.+\\.mp3$")) {
            //Iterate over files with the same name, until a unique name is found
            for(int i = 1; finalFile.exists() && finalFile.isFile(); i++)
                finalFile = new File(destinationFolder + "(" + i + ") " + originalName);
            return finalFile;
        } else
            throw new IllegalArgumentException("Invalid file extension.");
    }

    @Override
    public void UploadSongRequest(Object ref, String token, Music music) {
        try {
            //Validate token before file transfer begins
            Payload payload = tokenizer.GetPayload(new Token(token));
            //Create folder "musicLibrary" if it doesn't exist already
            String destinationFolder = "musicLibrary\\";
            File destinationFolderFile = new File(destinationFolder);
            if(!destinationFolderFile.exists())
                destinationFolderFile.mkdir();
            //Update model file info (old file = client file info; new file = server file info)
            music.setFile(GetFinalFile(music.getFile().getName(), destinationFolder));
            //Create server socket to accept tcp connection for file transfer
            ServerSocket fileTransferServerSocket = new ServerSocket(0);
            //Notify observers before blocking in accept(). Sends hostname and port
            Notify(ref, NotificationType.readyForUpload, fileTransferServerSocket.getInetAddress().getHostName(), fileTransferServerSocket.getLocalPort());
            //Initialize "file transfer thread"
            FileTransferThread fileTransferThread = new FileTransferThread(this, ref, payload, music, fileTransferServerSocket);
            //Start "file transfer thread" for receiving file
            fileTransferThread.ReceiveFile(music.getFile().getCanonicalPath());
        } catch (Exception e) {
            ThrowException(ref, e);
        }
    }

    @Override
    public void DownloadSong(Object ref, String token, Music music) { }

    //Called after file transfer is complete
    public synchronized void AddSong(Object ref, Payload payload, Music music) {
        try {
            //Token was previously validated, and payload extracted
            SqlAddMusic(payload, music);
            Notify(ref, NotificationType.addSongSuccess, music);
        } catch (Exception e) {
            ThrowException(ref, e);
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
        try {
            //Validate token
            tokenizer.GetPayload(new Token(token));
            List<Music> musics = SqlGetSongs();
            Notify(ref, NotificationType.getSongsSuccess, musics);
        } catch (InvalidTokenException | SQLException e) {
            ThrowException(ref, e);
        }
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