package Client.clientCommunication;

import Client.clientCommunication.CommsType.TCP_Communication;
import Client.clientCommunication.CommsType.UDP_Communication;
import Server.serverCommunication.Data.ServerDetails;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCommunication {

    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static final String ANS_SUCCESS = "SUCCESS";
    private static final String ANS_FAIL = "FAIL";
    private UDP_Communication udpC;
    private TCP_Communication tcpC;
    private String serverIp;
    private int serverUdpPort;

    public ClientCommunication(int serverUdpPort, String serverIp) {
        this.serverIp = serverIp;
        this.serverUdpPort = serverUdpPort;
        this.udpC = new UDP_Communication();
    }
    private ArrayList getServersList(String serversListBuffer) {
        ArrayList<ServerDetails> serversList = new ArrayList<>();
        Scanner sc = new Scanner(serversListBuffer);
        while (sc.hasNext()) {
            serversList.add(new ServerDetails(sc.next(), sc.nextInt(), sc.nextInt()));
        }
        return serversList;
    } 
    private void handleFail() throws IOException{
        //System.out.println("Recebi FaIl");
        String serversListBuffer = udpC.receiveUDP();
        //System.out.println(serversListBuffer);
        ArrayList<ServerDetails> serversList = getServersList(serversListBuffer);
        serverIp = serversList.get(0).getIpServer();
        serverUdpPort = serversList.get(0).getPortServer();
        System.out.println("ARRAY List");
        for (ServerDetails s : serversList) {
            System.out.println(s.toString());
        }
        System.out.println("Vou connectar ao Servidor: " + serverIp + ":" + serverUdpPort);
    }

    /**
     * This method will make the client ask to a server via an UDP connection
     * for a TCP connection.
     *
     * @return true if the connection was made with success and false if not.
     */
    public boolean askForConnection(){
        try {
            udpC.initializeUDP();
        } catch (SocketException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int attempt = 0;
            while (attempt < 5) {
                udpC.sendUDP(TCP_CONNECTION, InetAddress.getByName(serverIp), serverUdpPort);
                String ans = udpC.receiveUDP();
                if (!ans.equals(ANS_FAIL)) {
                    int serverTcpPort = Integer.parseInt(ans);
                    //
                    System.out.println("TCP PORT: " + serverTcpPort); //DEBUG
                    tcpC = new TCP_Communication(serverIp
                            ,serverTcpPort);
                    tcpC.initializeTCP();
                    tcpC.sendTCP("Sent by client");               //[DEBUG]
                    System.out.println("Received by server:" + tcpC.receiveTCP()); //[DEBUG]
                    //
                    break;
                } else{
                    handleFail();
                }
              attempt++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
