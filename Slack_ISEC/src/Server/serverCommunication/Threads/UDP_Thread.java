package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.UDPCommunication;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This thread will be waiting for a UDP connection to be made by a client.
 * 
 */
public class UDP_Thread extends Thread {
    private UDPCommunication udpC;
    private MulticastCommunication mcC;
    private ServerInfo iS;
    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static boolean exit;

    public UDP_Thread(UDPCommunication udpC, MulticastCommunication mcC, ServerInfo infoS) {
        this.udpC = udpC;
        this.mcC = mcC;
        this.iS = infoS;
        this.exit = true;
    }

    @Override
    public void run(){
        try {
            while(exit){
                String msg = udpC.receiveUDP();
                if(msg.equals(TCP_CONNECTION)){
                    iS.addClient(udpC.getServerPort());
                    udpC.sendUDP("SUCCESS");
                    synchronized(iS.getAllServersData()){
                        iS.getAllServersData().get(udpC.getServerPort()).setPing(true);
                    }
                    mcC.spreadInfo(iS,udpC.getServerPort());
                }
            }
        } catch (SocketException ex) {
            System.out.println("[UDP_THREAD]: Socket closed.");
        } catch (IOException ex) {
            Logger.getLogger(UDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
