package Server.serverCommunication.CommsTypes;

import Server.DataBase.DataBase;

public class DBCommuncation {

    private String ip;
    private int udpPort;
    private DataBase dataBase;

    public DBCommuncation(String ip, int udpPort) {
        this.ip = ip;
        this.udpPort = udpPort;
        dataBase = new DataBase();
        // Create DB Connection and DB if not exists
        dataBase.connectDB(ip, ip + udpPort);
    }

    public boolean userRegister(String name, String username, String password, String photopath){
       return dataBase.newUser(name, username, password, photopath);
    }

    public boolean userLogin(String username, String password) {
         return dataBase.loginUser(username, password);
    }

    public boolean newChannel(String name, String description, String password, String username) {
        return dataBase.newChannel(name, description, password, username);
    }

    public boolean deleteChannel(String name,String username){
        return dataBase.deleteChannel(name,username);
    }
}

