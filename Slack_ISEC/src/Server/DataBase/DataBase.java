package Server.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public boolean connectDB(String ip, String dbName) {
        try {
            Class.forName(JDBC_DRIVER);
            String dbAddress = ip;
            String dbUser = "root";
            String dbPass = "root";
            String dbTable = "ServerData";
            // Connect to Server
            String dbURL = "jdbc:mysql://" + dbAddress + "/?useTimezone=true&serverTimezone=UTC";
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
            stmt = conn.createStatement();
            System.out.println(dbName);
            // Create DataBase and Table
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            stmt.executeUpdate("USE " + dbName);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + dbTable + "("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "address TEXT NOT NULL, "
                    + "port INT NOT NULL, "
                    + "PRIMARY KEY (id))");

            // Users  Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "name VARCHAR(100), "
                    + "username VARCHAR(20) NOT NULL PRIMARY KEY, "
                    + "password TEXT NOT NULL, "
                    + "photopath VARCHAR(250))");
            // Channels Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS channels ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(30) NOT NULL, "
                    + "description VARCHAR(100) , "
                    + "password TEXT NOT NULL, "
                    + "creator VARCHAR(20) NOT NULL)");
            // Messages Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS messages ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "idchannel INT, "
                    + "senduser VARCHAR (20) , "
                    + "originuser VARCHAR (20)  ,"
                    + "message TEXT NOT NULL, "
                    + "dateMsg DATETIME DEFAULT NOW(),"
                    + "FOREIGN KEY(senduser) REFERENCES users(username), "
                    + "FOREIGN KEY (idchannel) REFERENCES channels(id), "
                    + "FOREIGN KEY(originuser) REFERENCES users(username))");
            // Files Table
           /* stmt.executeUpdate("CREATE TABLE IF NOT EXISTS files ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "idChannel INT,"
                    + "sendUser VARCHAR (20),"
                    + "originUser VARCHAR (20),"
                    + "pathDirectory VARCHAR (50))"
                    + "FOREIGN KEY(senduser) REFERENCES users(username), "
                    + "FOREIGN KEY (idchannel) REFERENCES channels(id), "
                    + "FOREIGN KEY(originuser) REFERENCES users(username))");*/

        } catch (ClassNotFoundException | SQLException sqlEx) {
            System.out.println("Create DB: " + sqlEx);
            closeConnections();
        }
        return true;
    }

    public boolean newUser(String name, String username, String password, String photopath) {
        try {                                       // ( 'name', 'username' , 'password' , 'photopath')    
            String query = "INSERT INTO users (name, username, password, photopath) VALUES ('" + name + "', '" + username + "', '" + password + "', '" + photopath + "')";
            stmt.executeUpdate(query);
            /*
            System.out.println(query);
            rs = stmt.executeQuery("SELECT * FROM users");
            
            while (rs.next()){
                System.out.print(" Pass: "+rs.getString("username"));
                System.out.print(" Pass: "+rs.getString("password"));
               System.out.println("");
            }
             */

            //Enviar Registo com Sucesso
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Já existe um utilizador com este nome!");
            System.out.println("ERRO: " + ex);
            // Enviar Resposta Ao utilizador 
            return false;
        } catch (SQLException ex) {
            System.out.println("DB ERROR - " + ex);
        }
        return true;

    }

    private void closeConnections() {

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se2) {
        }// nothing we can do
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }//end finally try

    }

    public boolean loginUser(String username, String password) {
        String query = "select * from users where username = '" + username + "'";

        boolean confirm = false;

        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                //System.out.print(" Nome: "+rs.getString("name"));
                if (username.equals(rs.getString("username"))) {
                    confirm = true;
                }
                System.out.print(" User: " + rs.getString("username"));
                System.out.print(" pass: " + rs.getString("password"));
                //System.out.print(" photo: "+rs.getString("photopath"));
            }
        } catch (SQLException ex) {
            System.out.println("ERRO LOGIN: " + ex);
        }

        return confirm;
    }

    public boolean newChannel(String name, String description, String password, String creator) {
        try {
            String query = "INSERT INTO channels (name, description, password, creator )VALUES ('" + name + "', '" + description + "', '" + password + "', '" + creator + "')";
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("ERRO CHANNEL: " + ex);
            return false;
        }
        return true;
    }

    public boolean deleteChannel(String name,String username){
        try {
            String query = "SELECT creator FROM channels where name='" + name + "'";
            rs = stmt.executeQuery(query);
            rs.next();
            if(username.equals(rs.getString(1))) {
                query = "DELETE FROM channels WHERE name='" + name + "'";
                stmt.executeUpdate(query);
            }else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("ERROR CHANNEL: " + ex);
            return false;
        }
        return true;
    }

    public boolean editChannel(String name, String newName, String description, String password, String username) {
        try {
            String query = "select * from channels where creator = '" + username + "' AND name = '" + name + "'" ;
            rs = stmt.executeQuery(query);
            while(rs.next()){
                if (name.equals(rs.getString("name"))){
                query = "update channels SET name = '" + newName + "', description = '"
                        + description + "', password =  '" + password + "' where id = '" + rs.getInt(1) +"'";
                stmt.executeUpdate(query);
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERRO EDIT CHANNEL: " + ex);
            return false;
        }
        return true;
    }

    public boolean conversation(String sender, String receiver, String msg) {
        try{
            String query = "INSERT INTO messages (senduser, originuser, message)" +
                    "VALUES ('" + sender + "', '" + receiver + "', '" + msg + "')";
            stmt.executeUpdate(query);
        } catch (SQLException ex){
            System.out.println("ERROR ON CONVERSATION: " + ex);
            return false;
        }
        return true;
    }

    public String searchUserAndChannel(String text) {
        StringBuilder channels = new StringBuilder();
        StringBuilder users = new StringBuilder();
        try {
            String query = "select * from channels where name = '" + text + "'" ;
            rs = stmt.executeQuery(query);
            while(rs.next()){
                channels.append("[Channel:] " + rs.getString("name") + "\n");
            }

            query = "select * from users where username = '" + text + "'" ;
            rs = stmt.executeQuery(query);
            while(rs.next()){
                users.append("[User:] " + rs.getString("username") + "\n");
            }
        } catch (SQLException ex) {
            System.out.println("ERRO EDIT CHANNEL: " + ex);
            return "Erro Pesquisa" + ex;
        }
        System.out.println("TEXT" + channels + users);
        return channels.toString() + users.toString();
    }

    public String searchMessages(String nameOrg, String nameDest,String n){
        StringBuilder output = new StringBuilder();
        try {
            int valor = Integer.parseInt(n);
            String query = "select * from messages where (senduser = '" + nameOrg
                    + "' OR senduser = '" + nameDest + "') AND (originUser = '" + nameOrg
                    + "' OR originUser = '" + nameDest + "') order by dateMsg asc limit " + valor;
            rs = stmt.executeQuery(query);
            while(rs.next()){
                output
                        .append("["+rs.getString("sendUser") +"] ")
                        .append(rs.getString("message"))
                        .append("\n");
            }

        }catch (NumberFormatException ex){
            System.out.println("Error Parse value" + ex);
            return "ERROR" + ex ;
        }catch (SQLException ex){
            System.out.println("ERRO EDIT CHANNEL: " + ex);
            return "Erro Pesquisa" + ex;
        }
        return output.toString();
    }

    public String showAllUsersAndChannels() {
        StringBuilder output =  new StringBuilder();
         try {
            String query = "select * from users";
            rs = stmt.executeQuery(query);
            output.append("[Users:]\n");
            while(rs.next()){
                output
                        .append("["+rs.getString("username") +"] ")
                        .append("Name: ")
                        .append(rs.getString("name"))
                        .append(" Photopath: ")
                        .append(rs.getString("photopath"))
                        .append("\n");
            }
            query = "select * from channels";
            rs = stmt.executeQuery(query);
            output.append("[Channels:]\n");
            while(rs.next()){
                output
                        .append("["+rs.getString("name") +"] ")
                        .append("Creator: ")
                        .append(rs.getString("creator"))
                        .append(" Description: ")
                        .append(rs.getString("description"))
                        .append("\n");
            }
        }catch (NumberFormatException ex){
            System.out.println("Error Parse value" + ex);
            return "ERROR" + ex ;
        }catch (SQLException ex){
            System.out.println("ERRO EDIT CHANNEL: " + ex);
            return "Erro Pesquisa" + ex;
        }
        return output.toString();
    }
}
