/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Threads;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.Data.ServerInfo;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCP_Thread extends Thread{
    private final int SIZE = 256; 
    private TCPCommunication tcpC;
    private ServerInfo iS;
    private MulticastCommunication mcC;
    private DBCommuncation dbC;
    private ArrayList<ClientData> clientsConnections;
    
    public TCP_Thread(TCPCommunication tcpC,ServerInfo iS,MulticastCommunication mcC, DBCommuncation dbC, ArrayList<ClientData> clientsConnections){
        this.tcpC = tcpC;
        this.dbC = dbC;
        this.clientsConnections = clientsConnections;
        this.iS = iS;
        this.mcC = mcC;
    }

    @Override
    public void run() {
        try {
            while(true){              
                tcpC.acceptConnection();
                ClientData cD = new ClientData(tcpC.getSocketClient());
                clientsConnections.add(cD);
                Thread t1 = new Thread(new TCPClient_Thread(cD,iS,mcC, dbC, tcpC));
                t1.start();
            }
        } catch (IOException ex) {
            System.out.println("[TCP THREAD]: Closed.");
        }
    }
}
