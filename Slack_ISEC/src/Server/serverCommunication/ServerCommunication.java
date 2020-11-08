/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication;

/**
 *
 * @author Rui
 */
public class ServerCommunication {
    private final static int MULTICAST_PORT = 5432;
    private final static String MULTICAST_IP = "239.3.2.1";
    private UDP_Communication udpC;
    private Multicast_Communication mcC;

    public ServerCommunication(int udpPort, int tcpPort, String ip) {
        udpC = new UDP_Communication(udpPort);
        mcC = new Multicast_Communication(MULTICAST_PORT, MULTICAST_IP);
    }
    
    
}
