package Server.serverCommunication;

import Server.serverCommunication.Threads.*;
import Server.serverCommunication.CommsTypes.*;
import Server.serverCommunication.Data.*;
import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
    private TCPCommunication tcpC;
    private MulticastCommunication mcC;
    private ServerInfo infoSv; 
    private DBCommuncation dbC;
    private ArrayList<ClientData> clientsConnections;
    
    //Threads
    private ServerListener_Thread svL;
    private UDP_Thread udpT;
    private TCP_Thread tcpT;
    private VerifyPing_Thread pingVerify;
    private Ping_Thread sendPing;
    private AtomicBoolean end;
    

    public ServerCommunication(int udpPort, int tcpPort, String ip) {
        clientsConnections = new ArrayList<>();
        udpC = new UDPCommunication(udpPort);
        try {
            udpC.initializeUDP();
        } catch (SocketException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        tcpC = new TCPCommunication(tcpPort);
        mcC = new MulticastCommunication(MULTICAST_PORT, MULTICAST_IP,udpC.getServerPort());
        dbC =  new DBCommuncation(ip, udpC.getServerPort());
        infoSv = new ServerInfo(udpC.getServerPort());
        end = new AtomicBoolean();
        
    }
    
    public void startThreads(){
        svL = new ServerListener_Thread(mcC.getmSocket(), infoSv, dbC, clientsConnections);
        udpT = new UDP_Thread(tcpC.getServerPort(), udpC, mcC, infoSv);
        tcpT = new TCP_Thread(tcpC,infoSv,mcC, dbC, clientsConnections);
        pingVerify = new VerifyPing_Thread(infoSv, end, clientsConnections);
        sendPing = new Ping_Thread(udpC, mcC, infoSv, end);
        
        svL.start();
        udpT.start();
        tcpT.start();
        pingVerify.start();
        sendPing.start();      
    }
    
    
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
            mcC.initializeMulticast();
            tcpC.initializeTCP();
        } catch (IOException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
