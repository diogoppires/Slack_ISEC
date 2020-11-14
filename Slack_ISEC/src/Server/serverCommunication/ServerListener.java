package Server.serverCommunication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener extends Thread {
    
    private MulticastSocket mSocket;
    private String username;
    private InfoServer infoServer;
    
    public ServerListener(MulticastSocket mSocket,InfoServer infoServer,String username) {
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
            
            
            infoServer.getUsersRegistrations().put(buffer.getPortServer(), buffer);

        
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
