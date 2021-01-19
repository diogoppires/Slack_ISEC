/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.clientCommunication.CommsType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DiogoPires
 */
public class UDP_Communication {
    private final static int SIZE = 256;
    private DatagramSocket dS;

    public UDP_Communication(){}
    
    /**
     * Method to initialize the UDP socket
     * @throws SocketException will be thrown if the port is already being used
     */
    public void initializeUDP() throws SocketException{
        dS = new DatagramSocket();
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
        return new String(dP.getData(), 0, dP.getLength());
    }
    
    /**
     * Method used to send a message through a UDP connection
     * @param msg the message that will be sent.
     * @param serverIp the IP that will receive the message.
     * @param serverPort the port that will receive the message.
     * @throws IOException 
     */
    public void sendUDP(String msg, InetAddress serverIp, int serverPort) throws IOException{
        byte[] bufStr = msg.getBytes();
        DatagramPacket dP = new DatagramPacket(bufStr, bufStr.length, serverIp, serverPort);
        dS.send(dP);
    }
    
    public void setTimeout(int timeout) throws SocketException{
            dS.setSoTimeout(timeout);
    }
    
}
