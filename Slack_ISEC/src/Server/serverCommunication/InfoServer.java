
package Server.serverCommunication;

import java.util.HashMap;

public class InfoServer {
    private HashMap<Integer,Integer> usesrRegistrations;

    public InfoServer() {
        this.usesrRegistrations = new HashMap<>();
    }

    public HashMap<Integer, Integer> getUsesrRegistrations() {
        return usesrRegistrations;
    } 
 
}
