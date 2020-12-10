/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hugoferreira
 */
public class ClientData {
    
    private Socket socket;
    private String username;
    private OutputStream outS;
    private ServerData sDataClient;

    public ClientData(Socket socket) {
        this.socket = socket;
        
        try {
            outS = this.socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(ClientData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public ClientData(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        sDataClient = null;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void sentTcpText(String text){
        try {
            outS.write(text.getBytes());
            outS.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ServerData getServerBackup() {
        return sDataClient;
    }
    public void setServerBackup(ServerData backupServer){
        sDataClient = backupServer;
    }
    
    
    
    
    
    
    
    
}
