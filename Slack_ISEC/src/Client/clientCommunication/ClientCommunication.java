
package Client.clientCommunication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientCommunication {
    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static final String ANS_SUCCESS = "SUCCESS";
    private UDP_Communication udpC;
    private String serverIp;
    private int serverUdpPort;

    public ClientCommunication(int serverUdpPort, String serverIp) {
        this.serverIp = serverIp;
        this.serverUdpPort = serverUdpPort;
        this.udpC = new UDP_Communication();
    }
    
    /**
     * This method will make the client ask to a server via an UDP connection 
     * for a TCP connection.
     * @return true if the connection was made with success and false if not.
     */
    public boolean askForConnection(){
        try {
            udpC.initializeUDP();
        } catch (SocketException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            udpC.sendUDP(TCP_CONNECTION, InetAddress.getByName(serverIp), serverUdpPort);
            String ans = udpC.receiveUDP();
            if(ans.equals(ANS_SUCCESS)){
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
