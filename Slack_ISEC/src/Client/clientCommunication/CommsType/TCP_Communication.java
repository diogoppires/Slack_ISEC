/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.clientCommunication.CommsType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCP_Communication {
    private final static int SIZE = 256;
    private final static String ERROR_RECEIVE = "ERROR";
    private final String serverIP;
    private final int serverPort;
    private Socket s;
    private InputStream iS;
    private OutputStream oS;
    
    public TCP_Communication(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    
    public void initializeTCP(){
        try {
            s = new Socket(serverIP, serverPort);
            iS = s.getInputStream();
            oS = s.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(TCP_Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeTCP() throws SocketException, IOException{
        iS.close();
        oS.close();
        s.close();
    }
    
    public void sendTCP(String msg) throws IOException {
      
            oS.write(msg.getBytes());
            oS.flush();
    
    }
    
    public String receiveTCP() throws IOException{
         byte[] bufStr = new byte[SIZE];
            int nBytes = iS.read(bufStr);
            String tempStr = new String(bufStr, 0, nBytes);
            return tempStr;
        /*
        try {
            byte[] bufStr = new byte[SIZE];
            int nBytes = iS.read(bufStr);
            String tempStr = new String(bufStr, 0, nBytes);
            return tempStr;
        } catch (IOException ex) {
            Logger.getLogger(TCP_Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ERROR_RECEIVE;
*/
    }
    
    //We probably have to develop a method that can send a serializable object
    //through a TCP connection.

    public int getSocketPort() {
        return s.getLocalPort();
    }
}
