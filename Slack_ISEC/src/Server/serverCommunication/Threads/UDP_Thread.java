package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.UDPCommunication;
import Server.serverCommunication.Data.ServerData;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
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
                    if (verifyCap(iS, udpC)){
                    iS.addClient(udpC.getServerPort());
                    udpC.sendUDP("SUCCESS"); 
                    } else
                        sendServersList(iS, udpC);
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
    
    
   

   
     public boolean verifyCap(ServerInfo sI, UDPCommunication updC) {
        
       for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {
                synchronized (iS.getAllServersData()) {
                    if (obj.getValue().getServerDetails().getnClients() < sI.getServerInfo(udpC.getServerPort()).getNClientsServer() / 2) {
                        System.out.println("Este Servidor tem: " + sI.getServerInfo(udpC.getServerPort()).getNClientsServer() + " Clientes.");
                        System.out.println("O Servidor: " + obj.getValue().getServerDetails().getPortServer() + " tem " + obj.getValue().getServerDetails().getnClients() + " Clientes");
                        return false;
                    }
                }
            }
       return true;
    }
      public void sendServersList(ServerInfo iS, UDPCommunication udpC) {
        
        ArrayList<Server> serversList = new ArrayList<>();

        for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {
                    synchronized (iS.getAllServersData()) {
                        serversList.add(new Server(obj.getValue().getServerDetails().getIpServer(),
                                obj.getValue().getServerDetails().getPortServer(),
                                obj.getValue().getServerDetails().getnClients()));
                    obj.getValue().getServerDetails().getIpServer();
                    Collections.sort(serversList, new sortByClients()); 
                }
            }
        
         for (Server s : serversList){
             
             System.out.println(s.toString());
         
         }
        
    }
    
}

class Server {
    String ip;
    int port;
    int nClients;

    public Server(String ip, int port, int nClients) {
        this.ip = ip;
        this.port = port;
        this.nClients = nClients;
    }
    
    @Override
    public String toString(){
        return "Server: " + ip +":"+ port + " Clientes " + nClients;
    }
}
class sortByClients implements Comparator<Server> 
{ 
    // Used for sorting in ascending order of 
    // roll number 
    public int compare(Server a, Server b) 
    { 
        return a.nClients - b.nClients; 
    } 
} 
    
    

