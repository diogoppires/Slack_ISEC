package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.UDPCommunication;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread is responsible for spread the information of his activity.
 * 
 */
public class Ping_Thread extends Thread{
    private UDPCommunication udpC;
    private MulticastCommunication mcC;
    private ServerInfo iS;

    public Ping_Thread(UDPCommunication udpC, MulticastCommunication mcC, ServerInfo infoS) {
        this.udpC = udpC;
        this.mcC = mcC;
        this.iS = infoS;
    }
    @Override
    public void run() {
        while(true){
            synchronized(iS.getAllServersData()){
                iS.getAllServersData().get(udpC.getServerPort()).setPing(true);
            }
            mcC.spreadInfo(iS,udpC.getServerPort());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ping_Thread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
