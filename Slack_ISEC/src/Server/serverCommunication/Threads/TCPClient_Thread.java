package Server.serverCommunication.Threads;

import Server.serverCommunication.CommsTypes.TCPCommunication;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {
    private TCPCommunication tcpC;
    
    public TCPClient_Thread(TCPCommunication tcpC) {
        this.tcpC = tcpC;
    }

    @Override
    public void run() {
        try {
            while(true){
                String ansClient = tcpC.receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG]
                tcpC.sendTCP("CONNECTED_TCP");         
                
            }
        } catch (IOException ex) {
            Logger.getLogger(TCP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
}
