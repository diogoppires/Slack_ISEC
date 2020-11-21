package Server.serverCommunication;

import Server.serverCommunication.Threads.*;
import Server.serverCommunication.CommsTypes.*;
import Server.serverCommunication.Data.*;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private TCP_Communication tcpC;
    private MulticastCommunication mcC;
    private ServerInfo infoSv; 
    
    //Threads
    private ServerListener_Thread svL;
    private UDP_Thread udpT;
    private TCP_Thread tcpT;
    private VerifyPing_Thread pingVerify;
    private Ping_Thread sendPing;
    private AtomicBoolean end;
    

    public ServerCommunication(int udpPort, int tcpPort, String ip) {
        udpC = new UDPCommunication(udpPort);
        tcpC = new TCP_Communication(tcpPort);
        mcC = new MulticastCommunication(MULTICAST_PORT, MULTICAST_IP);
        infoSv = new ServerInfo();
        end = new AtomicBoolean();
    }
    
    public void startThreads(){
        svL = new ServerListener_Thread(mcC.getmSocket(), infoSv, "rc1");
        udpT = new UDP_Thread(tcpC.getServerPort(), udpC, mcC, infoSv);
        tcpT = new TCP_Thread(tcpC);
        pingVerify = new VerifyPing_Thread(infoSv, end);
        sendPing = new Ping_Thread(udpC, mcC, infoSv, end);
        
        svL.start();
        udpT.start();
        tcpT.start();
        pingVerify.start();
        sendPing.start();      
    }
    
    // close pingVerify and sendPing correctly.
    
    public void finishThreads(){
        end.set(true);
        try {
            mcC.closeMulticast();
            tcpC.closeTCP();
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
            tcpC.initializeTCP();
        } catch (IOException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
