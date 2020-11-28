package Server.serverCommunication.Threads;

import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import Server.serverCommunication.Data.ServerInfo;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {
    private TCPCommunication tcpC;
    private ServerInfo iS;
    private MulticastCommunication mcC;
    
    public TCPClient_Thread(TCPCommunication tcpC,ServerInfo iS,MulticastCommunication mcC) {
        this.iS = iS;
        this.tcpC = tcpC;
        this.mcC = mcC;
    }

    @Override
    public void run() {
        try {
            while(true){
                String ansClient = tcpC.receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG]
                tcpC.sendTCP("CONNECTED_TCP");
            }
        } catch (SocketException ex){
            System.out.println("[CLOSED]: TCPClient_Thread");
        } catch (IOException ex) {
            Logger.getLogger(TCP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }

        synchronized (iS){
            iS.subClient();
        }
        try {
            mcC.spreadInfo(iS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
