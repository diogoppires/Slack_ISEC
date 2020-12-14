package Server.DataBase;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private int serverID;
    private String dbName;

    public boolean connectDB(String ip, int updPort) {
        dbName = ip + updPort;
        serverID = updPort;
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

            // Users  Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "name VARCHAR(100), "
                    + "username VARCHAR(20) NOT NULL PRIMARY KEY, "
                    + "password TEXT NOT NULL, "
                    + "photofilecode VARCHAR(250),"
                    + "created DATETIME NOT NULL DEFAULT NOW())");
            // Channels Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS channels ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(30) NOT NULL, "
                    + "description VARCHAR(100) , "
                    + "password TEXT NOT NULL, "
                    + "creator VARCHAR(20) NOT NULL,"
                    + "created DATETIME NOT NULL DEFAULT NOW())");
            // Channels_Users Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS channels_users("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "idChannel INT, "
                    + "idUsername VARCHAR(20),"
                    + "created DATETIME NOT NULL DEFAULT NOW(),"
                    + "FOREIGN KEY (idChannel) REFERENCES channels(id),"
                    + "FOREIGN KEY (idUsername) REFERENCES users(username))");
            // Messages Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS messages ("
                    + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + "idchannel INT, "
                    + "senduser VARCHAR (20) , "
                    + "originuser VARCHAR (20)  ,"
                    + "message TEXT NOT NULL, "
                    + "created DATETIME NOT NULL DEFAULT NOW(),"
                    + "FOREIGN KEY(senduser) REFERENCES users(username), "
                    + "FOREIGN KEY (idchannel) REFERENCES channels(id), "
                    + "FOREIGN KEY(originuser) REFERENCES users(username))");
            // Files Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS files ("
                    + "id INT NOT NULL PRIMARY KEY,"
                    + "destination VARCHAR (20),"
                    + "originUser VARCHAR (20),"
                    + "pathDirectory VARCHAR (500),"
                    + "created DATETIME NOT NULL DEFAULT NOW())"
                   /* + "FOREIGN KEY(originuser) REFERENCES users(username))"*/);

        } catch (ClassNotFoundException | SQLException sqlEx) {
            System.out.println("Create DB: " + sqlEx);
            closeConnections();
        }
        return true;
    }

    public boolean newUser(String name, String username, String password, String photofilecode) {
        try {                                       // ( 'name', 'username' , 'password' , 'photofilecode')
            String query = "INSERT INTO users (name, username, password, photofilecode) VALUES ('" + name + "', '" + username + "', '" + password + "', '" + photofilecode + "')";
            stmt.executeUpdate(query);

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
                System.out.print("User: " + rs.getString("username"));
                System.out.println(" Pass: " + rs.getString("password"));
            }
        } catch (SQLException ex) {
            System.out.println("ERRO LOGIN: " + ex);
        }

        return confirm;
    }

    public boolean newChannel(String name, String description, String password, String creator) {
        try {
            StringBuilder sb = new StringBuilder();
            int id = serverID,i;
            String query;

            for (i = 0; i < 10000; i++) {
                sb.append(serverID).append(i);
                id = Integer.parseInt(sb.toString());
                query = "select id from channels where id =" + id;
                rs = stmt.executeQuery(query);
                if(!rs.next())
                    break;
                sb.setLength(0);
            }

            query = "select name from channels where name = '" + name + "'";
            rs = stmt.executeQuery(query);
            boolean confirmation = false;

            if(rs.next() && name.equals(rs.getString("name")))
                return false;
            else
                confirmation = true;

            if (confirmation) {
                query = "INSERT INTO channels (id, name, description, password, creator )VALUES ('" + id + "', '" + name + "', '" + description + "', '" + password + "', '" + creator + "')";
                stmt.executeUpdate(query);
                query = "INSERT INTO channels_users(idChannel,idUsername) VALUES (" + id + ",'" + creator + "')";
                stmt.executeUpdate(query);
            }

        } catch (SQLException ex) {
            System.out.println("ERRO CHANNEL: " + ex);
            return false;
        }
        return true;
    }

    public boolean deleteChannel(String name, String username) {
        try {
            String query = "SELECT creator FROM channels where name ='" + name + "'";
            rs = stmt.executeQuery(query);
            if (rs.next() && username.equals(rs.getString(1))) {
                query = "SELECT id FROM channels where name ='" + name + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    int id = rs.getInt(1);
                    query = "DELETE FROM channels_users WHERE idChannel = " + id;
                    stmt.executeUpdate(query);
                    query = "DELETE FROM channels WHERE id = " + id;
                    stmt.executeUpdate(query);
                    return true;
                }
            }

        } catch (SQLException ex) {
            System.out.println("ERROR CHANNEL: " + ex);
        }
        return false;
    }

    public boolean editChannel(String name, String newName, String description, String password, String username) {
        try {
            String query = "select * from channels where creator = '" + username + "' AND name = '" + name + "'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                if (name.equals(rs.getString("name"))) {
                    query = "update channels SET name = '" + newName + "', description = '"
                            + description + "', password =  '" + password + "' where id = '" + rs.getInt(1) + "'";
                    stmt.executeUpdate(query);
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERRO EDIT CHANNEL: " + ex);
        }
        return false;
    }

    public boolean joinChannel(String nameC, String password, String name) {
        try {
            String query = "select * from channels where name = '" + nameC + "' AND password = '" + password + "'";
            rs = stmt.executeQuery(query);
            if (rs != null) {
                rs.next();
                int idChannel = rs.getInt("id");
                query = "select count(id) total from channels_users where idChannel = " + idChannel + " AND idUsername = '" + name + "'";
                rs = stmt.executeQuery(query);
                if (rs != null) {
                    rs.next();
                    if (rs.getInt("total") == 0) {
                        query = "INSERT INTO channels_users(idChannel,idUsername) VALUES (" + idChannel + ",'" + name + "')";
                        stmt.executeUpdate(query);
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERRO JOIN CHANNEL: " + ex);
        }
        return false;
    }

    public boolean conversation(String sender, String receiver, String msg) {
        try {
            String query = "select count(id) as total from messages";
            rs = stmt.executeQuery(query);
            if (rs != null) {
                rs.next();
            }
            StringBuilder s = new StringBuilder();
            s.append(serverID).append(rs.getInt("total"));
            int id = Integer.parseInt(s.toString());
            System.out.println("ID : " + id + "USERSENDER " + sender  + " RECEIVER  " + receiver + "MSG: "+ msg );
            query = "INSERT INTO messages (id, senduser, originuser, message)"
                    + "VALUES ('" + id + "', '" + sender + "', '" + receiver + "', '" + msg + "')";
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("ERROR ON CONVERSATION: " + ex);
            return false;
        }
        return true;
    }

    public String searchUserAndChannel(String text) {
        StringBuilder channels = new StringBuilder();
        StringBuilder users = new StringBuilder();
        try {
            String query = "select * from channels where name = '" + text + "'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                channels.append("[Channel:] " + rs.getString("name") + "\n");
            }

            query = "select * from users where username = '" + text + "'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.append("[User:] " + rs.getString("username") + "\n");
            }
        } catch (SQLException ex) {
            System.out.println("ERRO EDIT CHANNEL: " + ex);
            return "Erro Pesquisa" + ex;
        }
        System.out.println("TEXT" + channels + users);
        return channels.toString() + users.toString();
    }

    public String searchMessages(String nameOrg, String nameDest, String n) {
        StringBuilder output = new StringBuilder();
        try {
            int valor = Integer.parseInt(n);
            String query = "select * from messages where (senduser = '" + nameOrg
                    + "' AND originUser = '" + nameDest + "') OR ( originUser = '" + nameOrg
                    + "' AND sendUser = '" + nameDest + "') order by created asc limit " + valor;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                output
                        .append("[" + rs.getString("sendUser") + "] ")
                        .append(rs.getString("message"))
                        .append("\n");
            }

        } catch (NumberFormatException ex) {
            return "ERROR" + ex;
        } catch (SQLException ex) {
            return "ERROR" + ex;
        }
        return output.toString();
    }

    public String showAllUsersAndChannels() {
        StringBuilder output = new StringBuilder();
        try {
            String query = "select * from users";
            rs = stmt.executeQuery(query);
            output.append("[Users]\n");
            while (rs.next()) {
                output
                        .append("\t[" + rs.getString("username") + "] ")
                        .append("Name: ")
                        .append(rs.getString("name"))
                        .append("\n");
            }
            query = "select * from channels";
            rs = stmt.executeQuery(query);
            output.append("[Channels]\n");
            while (rs.next()) {
                output
                        .append("\t[" + rs.getInt("id") + "]")
                        .append("Name: " + rs.getString("name"))
                        .append("\tCreator: ")
                        .append(rs.getString("creator"))
                        .append("\tDescription: ")
                        .append(rs.getString("description"))
                        .append("\n");
            }
        } catch (SQLException ex) {
            return "Erro Pesquisa" + ex;
        }
        return output.toString();
    }

    public int insertFile(String destination, String username, String localFilePath) {

        try {
            String query = "select count(id) as total from files";
            rs = stmt.executeQuery(query);
            if (rs != null) {
                rs.next();
            }
            StringBuilder s = new StringBuilder();
            s.append(serverID).append(rs.getInt("total"));
            int id = Integer.parseInt(s.toString());

            localFilePath = localFilePath.replace("\\", "\\\\");
            query = "INSERT INTO files (id, destination, originUser, pathDirectory)"
                    + "VALUES ('" + id + "', '" + destination + "', '" + username + "', '" + localFilePath + "')";
            stmt.executeUpdate(query);
            query = "select * from files where pathDirectory = '" + localFilePath + "'";
            rs = stmt.executeQuery(query);
            if (rs != null) {
                rs.next();
                System.err.println("[DATABASE] -> Enviado Id de Ficheiro: " + rs.getInt("id"));
            } else {
                return 0;
            }

            return rs.getInt("id");
        } catch (SQLException ex) {
            System.err.println("[DB InsertFile] Erro: " + ex);
            return 0;
        }
    }

    public String getFilePath(String fileCode) {
        try {
            String query = "select * from files where id = '" + fileCode + "'";
            rs = stmt.executeQuery(query);
            if (rs != null) {
                rs.next();
                return rs.getString("pathDirectory");
            }
            return "0";
        } catch (SQLException ex) {
            System.err.println("[DB GetFilePath] -> Erro: " + ex);
            return "0";
        }
    }

    public String getChannelInfo() {
        StringBuilder output = new StringBuilder();
        List<Integer> ids = new ArrayList<>();
        try {
            String query = "select id from channels";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                ids.add(id);
            }
            for (int i = 0; i < ids.size(); i++) {
                output.append("[Channel (id: " + ids.get(i) + ")]\n");

                query = "select count(id) total from channels_users where idchannel = " + ids.get(i);
                rs = stmt.executeQuery(query);
                rs.next();
                output.append("\tNo. users: " + rs.getInt("total") + "\n");

                query = "select count(id) total from messages where idchannel = " + ids.get(i);
                rs = stmt.executeQuery(query);
                rs.next();
                output.append("\tNo. messages: " + rs.getInt("total") + "\n");
            }

                /*

                */
                //Adicionar após o merge
                /*
                query = "select count(id) total from files where destination = '" + id + "'";
                rs = stmt.executeQuery(query);
                output.append("\tNo. files: " + rs.getInt("total") + "\n");
                 */

        } catch (NumberFormatException ex) {
            System.out.println("ERROR PARSE VALUE" + ex);
            return "ERROR" + ex;
        } catch (SQLException ex) {
            System.out.println("ERROR INFO CHANNEL SEARCH:" + ex);
            return "ERROR SEARCHING" + ex;
        }
        return output.toString();
    }

    public void setUserPhotoID(String username, int fileID) {
        String query = "update users SET photofilecode ='" + fileID + "' where username ='" + username + "'";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.err.println("[DB setUserPhotoID] -> Erro: " + ex);
        }

    }

    public Timestamp getLastTimeStamp() {
        try {
            //String query = "select update_time from information_schema.tables where table_schema = '" + dbName + "'";
            // String query ="select created as update_time from users limit 1";
            String query = "select created from users\n"
                    + "union\n"
                    + "select created from channels\n"
                    + "union\n"
                    + "select created from files\n"
                    + "union\n"
                    + "select created from messages order by created DESC limit 1";
            LocalDateTime lastDate = null;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getTimestamp("created"));
                LocalDateTime date = rs.getTimestamp("created").toLocalDateTime();
                if (lastDate == null) {
                    lastDate = date;
                } else if (date.isAfter(lastDate)) {
                    lastDate = date;
                }
            }
            System.err.println("[DB LastTimeStamp] -> " + lastDate);
            if (lastDate == null) {
                return Timestamp.valueOf("1970-01-01 00:00:00");

            }
            return Timestamp.valueOf(lastDate);
        } catch (SQLException ex) {
            System.err.println("[DB LastTimeStamp] -> " + ex);
        } catch (NullPointerException ex) {
            System.err.println("[DB LastTimeStamp] -> No valid Timestamp" + ex);
        }
        return null;

    }

    public void getDatatoUpdate(Timestamp timestamp, String dbNameToSend) {
        try {
            System.out.println("dbNameToSend: " + dbNameToSend + " dbName: " + dbName);
            if (timestamp == null) {
                timestamp = Timestamp.valueOf("1970-01-01 00:00:00");
            }
            if (!dbNameToSend.equals(dbName)) {
                System.out.println(timestamp);
                String query = "INSERT IGNORE INTO " + dbNameToSend + ".users SELECT * FROM " + dbName + ".users where created > '" + timestamp + "'";
                stmt.executeUpdate(query);
                query = "INSERT IGNORE INTO " + dbNameToSend + ".channels SELECT * FROM " + dbName + ".channels where created > '" + timestamp + "'";
                stmt.executeUpdate(query);
                query = "INSERT IGNORE INTO " + dbNameToSend + ".files SELECT * FROM " + dbName + ".files where created > '" + timestamp + "'";
                stmt.executeUpdate(query);
                query = "INSERT IGNORE INTO " + dbNameToSend + ".messages SELECT * FROM " + dbName + ".messages where created > '" + timestamp + "'";
                stmt.executeUpdate(query);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getAllUsersAndChannels() {
        StringBuilder output = new StringBuilder();
        try {
            String query = "select * from users";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                output.append(rs.getString("name"))
                        .append("-");

            }
            query = "select * from channels";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                output.append(rs.getString("name"))
                        .append("-");
            }
            output.deleteCharAt(output.length()-1);
        } catch (SQLException ex) {
            return "Erro Pesquisa 2: " + ex;
        }
        return output.toString();
    }

}
