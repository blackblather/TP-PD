package server.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import common.controller.Controller;
import org.json.JSONException;
import org.json.JSONObject;
import server.controller.exceptions.InvalidCredentialsException;
import server.controller.exceptions.InvalidToken;

import java.sql.*;
import java.util.Base64;

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

    public ServerController(){
        try{
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

    //Encoder / Decoder
    private String GetEncodedCredentials(String username, String password){
        String decodedCredentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(decodedCredentials.getBytes());
    }

    private String[] GetDecodedCredentials(String encodedCredentials) throws InvalidToken {
        String decodedCredentialsStr = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] decodedCredentials = decodedCredentialsStr.split(":");

        if(decodedCredentials.length != 2)
            throw new InvalidToken();

        return decodedCredentials;
    }

    //SQL Query Functions
    private String SqlLogin(String username, String password) throws SQLException, InvalidCredentialsException {
        String query = "SELECT COUNT(*) as 'total' from users where username = '" + username + "' AND password = '" + password + "'";
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(!rs.next() || rs.getInt("total") != 1)
            throw new InvalidCredentialsException();
        return GetEncodedCredentials(username, password);
    }

    private String SqlRegister(String username, String password) throws SQLException {
        String query = "INSERT INTO users (id_users, username, password) VALUES (NULL, '" + username + "', '" + password + "')";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
        return GetEncodedCredentials(username, password);
    }

    private void SqlAddMusic(String token, String name, String author, String album, Integer year, String path) throws SQLException, InvalidToken {
        String[] decodedCredentials = GetDecodedCredentials(token);
        String query = "INSERT INTO musicas (id_users, id_generos, nome, autor, album, ano, path) VALUES ((SELECT id_users FROM users WHERE username = \"" + decodedCredentials[0] + "\" AND password = \"" + decodedCredentials[1] + "\"),1,\"" + name + "\", \"" + author + "\", \"" + album + "\", " + year + ", \"" + path + "\")";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }

    private boolean IsValidJSONRRequest(JSONObject jsonObject){
        return true;
    }

    //IController implementation
    @Override
    public synchronized void RouteJSONStr(Object ref, String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        if (IsValidJSONRRequest(jsonObject)) {
            JSONObject jsonContent = jsonObject.getJSONObject("Content");
            switch (jsonObject.getString("Type")) {
                case "Login": {
                    Login(ref, jsonContent.getString("username"), jsonContent.getString("password"));
                } break;
                case "Register": {
                    Register(ref, jsonContent.getString("username"), jsonContent.getString("password"), jsonContent.getString("passwordConf"));
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
    public synchronized void Login(Object ref, String username, String password) {
        System.out.println("GOT LOGIN REQUEST:\nUsername: " + username + "\nPassword: " + password);
        try {
            String token = SqlLogin(username, password);
            Notify(ref, NotificationType.loginSuccess, token);
        } catch (InvalidCredentialsException e){
            Notify(ref, NotificationType.loginInvalidCredentials);
        } catch (SQLException e) {
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
                String token = SqlRegister(username, password);
                Notify(ref, NotificationType.registerSuccess, token);
            } catch (MySQLIntegrityConstraintViolationException e){
                //No contexto desta função, a unica constraint violation possivel, é quando se tenta inserir usernames identicos
                Notify(ref, NotificationType.registerUsernameNotUnique);
            } catch (SQLException e) {
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

    @Override
    public synchronized void AddSong(Object ref, String token, String name, String author, String album, Integer year, String path) {
        try {
            SqlAddMusic(token, name, author, album, year, path);
        } catch (InvalidToken invalidToken) {
            invalidToken.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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

}