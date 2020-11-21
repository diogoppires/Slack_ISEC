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
}

