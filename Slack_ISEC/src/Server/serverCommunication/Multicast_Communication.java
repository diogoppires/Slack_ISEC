/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rui
 */
public class Multicast_Communication {
    private final int multicastPort;
    private final String multicastIp;
    private InetAddress mGroup;
    private MulticastSocket mSocket;
    
    public Multicast_Communication(int multicastPort, String multicastIp) {
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
    }
    
    public void initializeMulticast() throws IOException{
        mSocket = new MulticastSocket(multicastPort);
        mGroup = InetAddress.getByName(multicastIp);
        mSocket.joinGroup(mGroup);
    }
    
    public void closeMulticast() throws IOException{
        mSocket.leaveGroup(mGroup);
        mSocket.close();
    }
    
    /**
     * Method that will be 
     * @param info 
     */
    public void spreadInfo(InfoServer info){
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bOut);
            
            out.writeUnshared(info);
            out.flush();
            DatagramPacket dP = new DatagramPacket(bOut.toByteArray(), bOut.size(), mGroup, multicastPort);
            mSocket.send(dP);
        } catch (IOException ex) {
            Logger.getLogger(Multicast_Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
