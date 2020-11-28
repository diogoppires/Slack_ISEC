package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.UDPCommunication;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private AtomicBoolean end;

    public Ping_Thread(UDPCommunication udpC, MulticastCommunication mcC, ServerInfo infoS, AtomicBoolean end) {
        this.udpC = udpC;
        this.mcC = mcC;
        this.iS = infoS;
        this.end = end;
    }
    @Override
    public void run() {
        try {
            while(!end.get()){
                    synchronized(iS){
                        iS.getAllServersData().get(udpC.getServerPort()).setPing(true);
                        mcC.spreadInfo(iS);
                    }
                    Thread.sleep(10000);
            }
        } catch (SocketException ex){
            System.out.println("[PING THREAD]: Closed.");
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Ping_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("[PING THREAD]: Closed.");
    }
}
