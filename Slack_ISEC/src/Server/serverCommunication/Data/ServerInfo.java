
package Server.serverCommunication.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will hold all the info from all servers.
 * The information is going to be updated through multicast.
 * 
 */
public class ServerInfo {
    private Map<Integer,ServerData> allServersData;

    public ServerInfo() {
        this.allServersData = new HashMap<>();
    }

    public Map<Integer, ServerData> getAllServersData() {
        return allServersData;
    }
    
    public ServerData getServerInfo(int index){
        return allServersData.get(index);
    }
    
    public void removeServer(Integer key){
        allServersData.remove(key);
    }
    
    /**
     * add a client from the server details
     * @param index -> server port
     * @return true -> if a client has been added 
     * false -> if not
     */
    public boolean addClient(int index){
        ServerDetails aux = allServersData.get(index).getServerDetails();
        
        if(aux != null){
            aux.setnClients((aux.getnClients()) + 1);
            return true;
        }
        return false;
    }
    
    /**
     * subtract a client from the server details
     * @param index -> server port
     * @return true -> if a client has been subtracted
     * false -> if not
     */
    public boolean subClient(int index){   
        ServerDetails aux = allServersData.get(index).getServerDetails();
        
        if(aux != null){
            aux.setnClients((aux.getnClients()) - 1);
            return true;
        }
        return false;
    }
    
}
