package Server.serverCommunication.Data;

import java.io.Serializable;

/**
 * Contains the number of clients actives on the server.
 * Also has the Ip and Port of the current server.
 * 
 */
public class ServerDetails implements Serializable {
    private String ipServer;
    private int portServer; //Is this the UDP port?
    private int nClients;

    public ServerDetails(String ipServer, int portServer, int nClients) {
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.nClients = nClients;
    }

    public String getIpServer() {
        return ipServer;
    }

    public int getPortServer() {return portServer;}

    public int getnClients() {
        return nClients;
    }

    public void setnClients(int nClients) {
        this.nClients = nClients;
    }
     @Override
    public String toString(){
        return "Server: " + ipServer +":"+ portServer + " Clientes " + nClients;
    }
    
}
