package Server.DataBase;

import java.sql.*;
import java.util.ArrayList;

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
                    + "name VARCHAR(30) NOT NULL PRIMARY KEY, "
                    + "description VARCHAR(100) , "
                    + "password TEXT NOT NULL, "
                    + "creator VARCHAR(20) NOT NULL)");
            // Messages Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS messages ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "idchannel VARCHAR(30), "
                    + "senduser VARCHAR (20) , "
                    + "originuser VARCHAR (20)  ,"
                    + " message TEXT NOT NULL, "
                    + "FOREIGN KEY(senduser) REFERENCES users(username), "
                    + "FOREIGN KEY (idchannel) REFERENCES channels(name), "
                    + "FOREIGN KEY(originuser) REFERENCES users(username))");

        } catch (ClassNotFoundException | SQLException sqlEx) {
            System.out.println(sqlEx);
            closeConnections();
        }
        return true;
    }

    public boolean newUser(String name, String username, String password, String photopath) {
        try {                                       // ( 'name', 'username' , 'password' , 'photopath')    
            String query = "INSERT INTO users VALUES ('" + name + "', '" + username + "', '" + password + "', '" + photopath + "')";
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
                //.out.print(" photo: "+rs.getString("photopath"));
                // System.out.println("");
            }
        } catch (SQLException ex) {
            System.out.println("ERRO LOGIN: " + ex);
        }

        return confirm;
    }

    public boolean newChannel(String name, String description, String password, String creator) {
        String query = "INSERT INTO channels VALUES ('" + name + "', '" + description + "', '" + password + "', '" + creator + "')";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("ERRO CHANNEL: " + ex);
            return false;
        }
        return true;
    }

    public boolean deleteChannel(String name,String username){
        String query = "SELECT creator FROM channels where name='" + name + "'";
        try {
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
}
