/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DiogoPires
 */
public class ReceiveServerStatus extends Thread {
    
    private MulticastSocket mSocket;
    private String username;
    private InfoServer infoServer;
    
    public ReceiveServerStatus(MulticastSocket mSocket,InfoServer infoServer,String username) {
        this.mSocket = mSocket;
        this.infoServer = infoServer;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            DatagramPacket dP = new DatagramPacket(new byte[512],512);
            mSocket.receive(dP); 
            ObjectInputStream oIN = new ObjectInputStream(new ByteArrayInputStream(dP.getData()));
        
            ServerInfoToClients buffer = (ServerInfoToClients) oIN.readObject();
        
            System.out.println("(server" + buffer.getPortServer() + ") IP: " +
                    buffer.getIpServer() + " Port: " + buffer.getPortServer() +
                    " Clients: " + buffer.getnClients() + "\n");
            
            infoServer.getUsesrRegistrations().put(buffer.getPortServer(), buffer);
        
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ReceiveServerStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
