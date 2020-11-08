package Server.serverCommunication;

import java.io.Serializable;

public class ServerInfoToClients implements Serializable {
    private String ipServer;
    private int portServer;
    private int nClients;

    public ServerInfoToClients(String ipServer, int portServer, int nClients) {
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.nClients = nClients;
    }

    public String getIpServer() {
        return ipServer;
    }

    public int getPortServer() {
        return portServer;
    }

    public int getnClients() {
        return nClients;
    }
}
