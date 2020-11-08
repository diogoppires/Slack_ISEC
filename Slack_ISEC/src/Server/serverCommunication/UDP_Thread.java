package Server.serverCommunication;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDP_Thread extends Thread {
    private UDP_Communication udpC;
    private Multicast_Communication mcC;
    private InfoServer infoS;
    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static boolean exit;

    public UDP_Thread(UDP_Communication udpC, Multicast_Communication mcC, InfoServer infoS) {
        this.udpC = udpC;
        this.mcC = mcC;
        this.infoS = infoS;
        this.exit = true;
    }

    @Override
    public void run() {
        try {
            udpC.initializeUDP();
            while(exit){
                String msg = udpC.receiveUDP();
                
                //if(infoS.getUsersRegistrations().get(udpC.getServerPort()).getnClients())
            }
        } catch (SocketException ex) {
            System.out.println("Error, port alredy exist...");
            Logger.getLogger(UDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
