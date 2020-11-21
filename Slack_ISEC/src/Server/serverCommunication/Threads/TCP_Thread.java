/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Threads;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCP_Thread extends Thread{
    private final int SIZE = 256; 
    private TCPCommunication tcpC;
    
    
    public TCP_Thread(TCPCommunication tcpC){
        this.tcpC = tcpC;
    }

    @Override
    public void run() {
        try {
            tcpC.acceptConnection();
            while(true){
                String ansClient = tcpC.receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG]
                tcpC.sendTCP("CONNECTED_TCP");                      // [DEBUG]
            }
        } catch (IOException ex) {
            Logger.getLogger(TCP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
