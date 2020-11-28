package Server.serverCommunication.Data;

import java.io.Serializable;

/**
 * Class that will contain the server details and the flag necessary to check if the server is alive.
 * This class will also be used in the multicast group to share info from the servers.
 */
public class ServerData implements Serializable{
    private ServerDetails sIC;
    private boolean ping;

    public ServerData(ServerDetails sIC) {
        this.sIC = sIC;
        ping = true;
    }

    public boolean getPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }
    
    public ServerDetails getServerDetails(){
        return sIC;
    }
    
    public int getPortServer() {return sIC.getPortServer();}
    
    public String getIPServer(){
        return sIC.getIpServer();
    }
    
    public int getNClientsServer(){
        return sIC.getnClients();
    }
    
    public void setServerNClients(int nClients){
        sIC.setnClients(nClients);
    }
    
    
    
    
    
    
}
