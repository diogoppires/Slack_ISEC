package Server.serverCommunication.CommsTypes;

import Server.DataBase.DataBase;
import java.io.File;

public class DBCommuncation {

    private String ip;
    private int udpPort;
    private DataBase dataBase;

    public DBCommuncation(String ip, int udpPort) {
        this.ip = ip;
        this.udpPort = udpPort;
        dataBase = new DataBase();
        // Create DB Connection and DB if not exists
        dataBase.connectDB(ip, udpPort);
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
    public boolean editChannel(String name, String newName, String description, String password, String username) {
        return dataBase.editChannel(name, newName,  description, password, username); 
    }
    public boolean joinChannel(String nameC,String password,String username){
        return dataBase.joinChannel(nameC,password,username);
    }
    public boolean conversation(String sender, String receiver, String msg){
        return dataBase.conversation(sender,receiver,msg);
    }

    public String searchUserAndChannel(String text) {
        return dataBase.searchUserAndChannel(text);
    }

    public String searchMessages(String nameOrg, String nameDest,String n){
        return dataBase.searchMessages(nameOrg,nameDest,n);
    }

    public String showAllUsersAndChannels() {
        return dataBase.showAllUsersAndChannels();
    }

    public int insertFile(String destination, String username, String localFilePath) {
        return dataBase.insertFile(destination, username, localFilePath);
    }

    public String getFilePath(String fileCode) {
        return dataBase.getFilePath(fileCode);
    }

    public String getChannelInfo(String name) {
        return dataBase.getChannelInfo(name);
    }
}

