
package Server.serverCommunication;

import java.util.HashMap;

public class InfoServer {
    private HashMap<Integer,ServerInfoToClients> usesrRegistrations;

    public InfoServer() {
        this.usesrRegistrations = new HashMap<>();
    }

    public HashMap<Integer, ServerInfoToClients> getUsesrRegistrations() {
        return usesrRegistrations;
    } 
 
}
