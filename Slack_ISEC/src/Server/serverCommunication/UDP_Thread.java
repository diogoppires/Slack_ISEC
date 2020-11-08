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
    public void run(){
        try {
            while(exit){
                String msg = udpC.receiveUDP();
                if(msg.equals(TCP_CONNECTION)){
                    udpC.sendUDP("SUCCESS");
                }
            }
        } catch (SocketException ex) {
            System.out.println("[UDP_THREAD]: Socket closed.");
        } catch (IOException ex) {
            Logger.getLogger(UDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This interrupt is important because it will interrupt the thread and finish it
     * even if the thread is waiting for some message.
     */
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
        udpC.closeUDP();
    }
}
