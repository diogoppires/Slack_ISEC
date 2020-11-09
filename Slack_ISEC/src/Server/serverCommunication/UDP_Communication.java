/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author DiogoPires
 */
public class UDP_Communication {
    private final static int SIZE = 256;
    private final int serverPort;
    private int lastClientPort;
    private InetAddress lastClientIp;
    private DatagramSocket dS;

    public UDP_Communication(int serverPort) {
        this.serverPort = serverPort;
    }
    
    /**
     * Method to initialize the UDP socket
     * @throws SocketException will be thrown if the port is already being used
     */
    public void initializeUDP() throws SocketException{
        dS = new DatagramSocket(serverPort);
    }
    
    /**
     * Method to close the UDP socket
     */
    public void closeUDP(){
        dS.close();
    }
    
    /**
     * Method used to obtain information through a UDP connection
     * @return the message received
     * @throws IOException 
     */
    public String receiveUDP() throws IOException{
        DatagramPacket dP = new DatagramPacket(new byte[SIZE], SIZE);
        dS.receive(dP);
        lastClientPort = dP.getPort();
        lastClientIp = dP.getAddress();
        return new String(dP.getData(), 0, dP.getLength());
    }
    
    /**
     * Method used to send a message through a UDP connection
     * @param msg the message that will be sent.
     * @throws IOException 
     */
    public void sendUDP(String msg) throws IOException{
        byte[] bufStr = msg.getBytes();
        DatagramPacket dP = new DatagramPacket(bufStr
                                            , bufStr.length
                                            , lastClientIp
                                            , lastClientPort);
        dS.send(dP);
    }

    public int getServerPort() {
        return serverPort;
    }
    
}
