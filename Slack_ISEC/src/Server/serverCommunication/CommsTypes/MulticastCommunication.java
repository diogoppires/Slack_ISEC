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
    private int serverId;
    private InetAddress mGroup;
    private MulticastSocket mSocket;

    
    public MulticastCommunication(int multicastPort, String multicastIp,int serverId) {
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
        this.serverId = serverId;
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
     */
    public void spreadInfo(ServerInfo info) throws SocketException, IOException{
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);

        out.writeUnshared(info.getServerInfo(serverId));
        out.flush();
        DatagramPacket dP = new DatagramPacket(bOut.toByteArray(), bOut.size(), mGroup, multicastPort);
        mSocket.send(dP);
    }

    public void spreadInfo(Object info) throws SocketException, IOException{
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);

        out.writeUnshared(info);
        out.flush();
        DatagramPacket dP = new DatagramPacket(bOut.toByteArray(), bOut.size(), mGroup, multicastPort);
        mSocket.send(dP);
    }
}
