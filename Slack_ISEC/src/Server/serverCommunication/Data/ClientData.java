/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Data;

import java.net.Socket;

/**
 *
 * @author hugoferreira
 */
public class ClientData {
    
    private Socket socket;
    private String username;

    public ClientData(Socket socket) {
        this.socket = socket;
    }
            
    public ClientData(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
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
    
    
    
    
    
    
    
    
}
