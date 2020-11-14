package Server.serverCommunication;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCommunication {
    private final static int MULTICAST_PORT = 5432;
    private final static String MULTICAST_IP = "239.3.2.1";
    private UDP_Communication udpC;
    private Multicast_Communication mcC;
    private InfoServer infoSv;
    
    //Threads
    private ServerListener svL;
    private UDP_Thread udpT;
    

    public ServerCommunication(int udpPort, int tcpPort, String ip) {
        udpC = new UDP_Communication(udpPort);
        mcC = new Multicast_Communication(MULTICAST_PORT, MULTICAST_IP);
        infoSv = new InfoServer();
    }
    
    public void startThreads(){
        svL = new ServerListener(mcC.getmSocket(), infoSv, "rc1");
        udpT = new UDP_Thread(udpC, mcC, infoSv);
        svL.start();
        udpT.start();
    }
    
    public void finishThreads(){
        try {
            mcC.closeMulticast();
        } catch (IOException ex) {
            Logger.getLogger(ServerCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        udpC.closeUDP();
    }
    
    public void initializeComms(){
        
        infoSv.getUsersRegistrations().put(udpC.getServerPort(), new InfoServerActive(new ServerInfoToClients("localhost", udpC.getServerPort(), 0)));
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
