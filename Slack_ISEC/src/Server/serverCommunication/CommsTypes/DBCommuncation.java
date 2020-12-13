package Server.serverCommunication.CommsTypes;

import Server.DataBase.DataBase;
import Server.Utils.DataRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBCommuncation {

    private String ip;
    private int udpPort;
    private DataBase dataBase;
    private MulticastCommunication mcC;

    public DBCommuncation(String ip, int udpPort, MulticastCommunication mcC) {
        this.ip = ip;
        this.udpPort = udpPort;
        this.mcC = mcC;
        dataBase = new DataBase();
        // Create DB Connection and DB if not exists
        dataBase.connectDB(ip, udpPort);



    }
    
    public boolean initializeDBComms(){
        Timestamp time = dataBase.getLastTimeStamp();
        try {
            mcC.spreadInfo(new DataRequest(time ,ip + this.udpPort));
        } catch (IOException ex) {
            Logger.getLogger(DBCommuncation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
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

    public int insertFile(String destination, String username, String localFilePath, int fileId) {
        return dataBase.insertFile(destination, username, localFilePath, fileId);
    }

    public String getFilePath(String fileCode) {
        return dataBase.getFilePath(fileCode);
    }

    public void setUserPhotoID(String username, int fileID) {
        dataBase.setUserPhotoID(username, fileID);
    }

    public void getDatatoUpdate(Timestamp time, String dbNameToSend) {
        dataBase.getDatatoUpdate(time, dbNameToSend);
    }

    public String getChannelInfo() {
        return dataBase.getChannelInfo();
    }
}

