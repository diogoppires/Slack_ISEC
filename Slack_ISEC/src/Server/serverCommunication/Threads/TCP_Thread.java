/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Threads;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCP_Thread extends Thread{
    private final int SIZE = 256; 
    private TCPCommunication tcpC;
    private List<TCPClient_Thread> clientConnections;
    
    public TCP_Thread(TCPCommunication tcpC){
        this.tcpC = tcpC;
        clientConnections = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while(true){              
                tcpC.acceptConnection();
                Thread t1 = new Thread(new TCPClient_Thread(tcpC));
                t1.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(TCP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
