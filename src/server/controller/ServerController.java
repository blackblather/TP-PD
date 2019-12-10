package server.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import common.controller.Controller;
import org.json.JSONException;
import org.json.JSONObject;

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

    private boolean IsValidJSONRRequest(JSONObject jsonObject){
        return true;
    }

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
            String query = "SELECT COUNT(*) as 'total' from users where username = '" + username + "' AND password = '" + password + "'";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next() && rs.getInt("total") == 1)
                Notify(ref, NotificationType.exception);
                //Notify(ref, NotificationType.loginSuccess);
            else
                Notify(ref, NotificationType.loginInvalidCredentials);
        } catch (SQLException e ) {
            Notify(ref, NotificationType.exception);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Notify(ref, NotificationType.exception);
                }
            }
        }
    }

    @Override
    public synchronized void Register(Object ref, String username, String password, String passwordConf) {
        System.out.println("GOT REGISTER REQUEST:\nUsername: " + username + "\nPassword: " + password + "\nPassword Confirmation: " + passwordConf);
        if(password.equals(passwordConf)){
            String query = "INSERT INTO users (id_users, username, password) VALUES (NULL, '" + username + "', '" + password + "')";
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                Notify(ref, NotificationType.registerSuccess);
            } catch (MySQLIntegrityConstraintViolationException e){
                Notify(ref, NotificationType.registerUsernameNotUnique);
            } catch (SQLException e) {
                Notify(ref, NotificationType.exception);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        Notify(ref, NotificationType.exception);
                    }
                }
            }
        } else
            Notify(ref, NotificationType.registerPasswordsNotMatching);
    }

    @Override
    public synchronized void AddMusic(Object ref, String username, String name, String author, String album, String year, String path) {

    }

    @Override
    public synchronized void AddPlaylist(Object ref, String username, String name) {

    }

    @Override
    public synchronized void RemoveMusic(Object ref, String name) {

    }

    @Override
    public synchronized void RemovePlaylist(Object ref, String name) {

    }

    @Override
    public synchronized void GetMusics(Object ref) {

    }

    @Override
    public synchronized void GetMusic(Object ref, String name) {

    }

    @Override
    public synchronized void GetPlaylists(Object ref) {

    }

    @Override
    public synchronized void GetPlaylist(Object ref, String name) {

    }

}