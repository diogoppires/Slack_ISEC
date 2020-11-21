package Server.serverCommunication.CommsTypes;

import Server.serverCommunication.Data.ServerInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * This class will be responsible by the whole Multicast process.
 * This class was made with the purpose of make the Multicast communication much more easier to use.  
 */
public class MulticastCommunication {
    private final int multicastPort;
    private final String multicastIp;
    private InetAddress mGroup;
    private MulticastSocket mSocket;
    
    public MulticastCommunication(int multicastPort, String multicastIp) {
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
    }
    
    public void initializeMulticast() throws IOException{
        mSocket = new MulticastSocket(multicastPort);
        mGroup = InetAddress.getByName(multicastIp);
        mSocket.joinGroup(mGroup);
    }
    
    public void closeMulticast() throws IOException{
        mSocket.leaveGroup(mGroup);
        mSocket.close();
    }

    public MulticastSocket getmSocket() {
        return mSocket;
    }
    
    /**
     * Method that will spread all the information that was recently updated to all
     * the servers on the network.
     * @param info the information that was updated.
     * @param serverPort the server index that is supposed to be shared. 
     */
    public void spreadInfo(ServerInfo info,int serverPort ) throws SocketException, IOException{
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);


        out.writeUnshared(info.getServerInfo(serverPort));
        out.flush();
        DatagramPacket dP = new DatagramPacket(bOut.toByteArray(), bOut.size(), mGroup, multicastPort);
        mSocket.send(dP);
    }
}
