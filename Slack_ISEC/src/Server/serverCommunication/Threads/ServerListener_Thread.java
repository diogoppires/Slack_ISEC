package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.Data.ServerData;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * This thread will listen all the information that is going to be spread from a multicast group.
 */

public class ServerListener_Thread extends Thread {
    
    private MulticastSocket mSocket;
    private String username;        //What is this 'username'?
    private ServerInfo infoServer;
    
    public ServerListener_Thread(MulticastSocket mSocket,ServerInfo infoServer, String username) {
        this.mSocket = mSocket;
        this.infoServer = infoServer;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            while(true){
                DatagramPacket dP = new DatagramPacket(new byte[512],512);
                mSocket.receive(dP); 
                ObjectInputStream oIN = new ObjectInputStream(new ByteArrayInputStream(dP.getData()));
                Toolkit.getDefaultToolkit().beep();    
                ServerData buffer = (ServerData) oIN.readObject();

                System.out.println("(server" + buffer.getPortServer() + ") IP: " +
                        buffer.getIPServer() + " Port: " + buffer.getPortServer() +
                        " Clients: " + buffer.getNClientsServer()+ "\n");

                synchronized(infoServer.getAllServersData()){
                    infoServer.getAllServersData().put(buffer.getPortServer(), buffer);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("[LISTENER THREAD]: Closed.");
        }
    }
}
