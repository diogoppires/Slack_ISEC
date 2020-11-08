
package Server.serverCommunication;

import java.io.Serializable;
import java.util.HashMap;

public class InfoServer implements Serializable{
    private HashMap<Integer,ServerInfoToClients> usersRegistrations;

    public InfoServer() {
        this.usersRegistrations = new HashMap<>();
    }

    public HashMap<Integer, ServerInfoToClients> getUsersRegistrations() {
        return usersRegistrations;
    }
}
