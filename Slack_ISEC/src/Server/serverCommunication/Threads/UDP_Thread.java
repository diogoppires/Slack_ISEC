package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCP_Communication;
import Server.serverCommunication.CommsTypes.UDPCommunication;
import Server.serverCommunication.Data.ServerData;
import Server.serverCommunication.Data.ServerDetails;
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
    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static boolean exit;
    private Integer tcpPort;
    private UDPCommunication udpC;
    private MulticastCommunication mcC;
    private ServerInfo iS;
    

    public UDP_Thread(int tcpC,
            UDPCommunication udpC,
            MulticastCommunication mcC, 
            ServerInfo infoS) {
        this.udpC = udpC;
        this.tcpPort = tcpC;
        this.mcC = mcC;
        this.iS = infoS;
        this.exit = true;
    }

    @Override
    public void run() {
        try {
            while (exit) {
                String msg = udpC.receiveUDP();
                if (msg.equals(TCP_CONNECTION)) {
                    if (verifyCap(iS, udpC)) {
                        iS.addClient(udpC.getServerPort());
                        udpC.sendUDP(tcpPort.toString());
                    } else {
                        udpC.sendUDP("FAIL");
                        udpC.sendUDP(getServersList(iS, udpC));
                    }
                    
                    synchronized (iS.getAllServersData()) {
                        iS.getAllServersData().get(udpC.getServerPort()).setPing(true);
                    }
                    mcC.spreadInfo(iS, udpC.getServerPort());
                }
            }
        } catch (SocketException ex) {
            System.out.println("[UDP_THREAD]: Closed.");
        } catch (IOException ex) {
            Logger.getLogger(UDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean verifyCap(ServerInfo sI, UDPCommunication updC) {

        for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {
            synchronized (iS.getAllServersData()) {
                double count = (double)sI.getServerInfo(udpC.getServerPort()).getNClientsServer() / 2;
                //System.out.println("DOUBLE: " +  count); 
                if (obj.getValue().getServerDetails().getnClients() < count) {
                   //System.out.println("Este Servidor tem: " + sI.getServerInfo(udpC.getServerPort()).getNClientsServer() + " Clientes.");
                   //System.out.println("O Servidor: " + obj.getValue().getServerDetails().getPortServer() + " tem " + obj.getValue().getServerDetails().getnClients() + " Clientes");
                    return false;
                }
            }
        }
        return true;
    }

    private String getServersList(ServerInfo iS, UDPCommunication udpC) {

        ArrayList<ServerDetails> serversList = new ArrayList<>();

        for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {
            synchronized (iS.getAllServersData()) {
                //System.out.println("TESTE NULL: " + obj.getValue().getServerDetails().getIpServer());
                serversList.add(new ServerDetails(obj.getValue().getServerDetails().getIpServer(),
                        obj.getValue().getServerDetails().getPortServer(),
                        obj.getValue().getServerDetails().getnClients()));
                obj.getValue().getServerDetails().getIpServer();

                Collections.sort(serversList, new sortByClients());
            }
        }
        String bufStr = "";
        for (ServerDetails s : serversList) {
            //System.out.println(s.toString());
            bufStr += s.getIpServer() + " " + s.getPortServer() + " " + s.getnClients() + " ";
        }
        System.out.println(bufStr);
        return bufStr;

    }
}

class sortByClients implements Comparator<ServerDetails> {

    // Used for sorting in ascending order of 
    // nClients
    @Override
    public int compare(ServerDetails a, ServerDetails b) {
        return a.getnClients() - b.getnClients();
    }
}
