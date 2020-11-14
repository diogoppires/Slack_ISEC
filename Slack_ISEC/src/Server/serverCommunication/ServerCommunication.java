package Server.serverCommunication;

import Server.serverCommunication.Threads.*;
import Server.serverCommunication.CommsTypes.*;
import Server.serverCommunication.Data.*;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * The class that will have the control of all server communication.
 */

public class ServerCommunication {
    private final static int MULTICAST_PORT = 5432;
    private final static String MULTICAST_IP = "239.3.2.1";
    private UDPCommunication udpC;
    private MulticastCommunication mcC;
    private ServerInfo infoSv; 
    
    //Threads
    private ServerListener_Thread svL;
    private UDP_Thread udpT;
    private VerifyPing_Thread pingVerify;
    private Ping_Thread sendPing;
    

    public ServerCommunication(int udpPort, int tcpPort, String ip) {
        udpC = new UDPCommunication(udpPort);
        mcC = new MulticastCommunication(MULTICAST_PORT, MULTICAST_IP);
        infoSv = new ServerInfo();
    }
    
    public void startThreads(){
        svL = new ServerListener_Thread(mcC.getmSocket(), infoSv, "rc1");
        udpT = new UDP_Thread(udpC, mcC, infoSv);
        pingVerify = new VerifyPing_Thread(infoSv);
        sendPing = new Ping_Thread(udpC, mcC, infoSv);
        svL.start();
        udpT.start();
        pingVerify.start();
        sendPing.start();      
    }
    
    // close pingVerify and sendPing correctly.
    
    public void finishThreads(){
        try {
            mcC.closeMulticast();
        } catch (IOException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        udpC.closeUDP();
    }
    
    /**
     * This method will initialize the sockets from UDP and Multicast communication.
     */
    public void initializeComms(){
        
        infoSv.getAllServersData().put(udpC.getServerPort(), new ServerData(new ServerDetails("localhost", udpC.getServerPort(), 0)));
        try {
            udpC.initializeUDP();
        } catch (SocketException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            mcC.initializeMulticast();
        } catch (IOException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
