package Server.serverCommunication.Threads;

import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import Server.serverCommunication.Data.ServerInfo;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {

    private TCPCommunication tcpC;
    private ServerInfo iS;
    private MulticastCommunication mcC;
    private DBCommuncation dbC;
    
    public TCPClient_Thread(TCPCommunication tcpC,ServerInfo iS,MulticastCommunication mcC, DBCommuncation dbC) {
        this.iS = iS;
        this.tcpC = tcpC;
        this.mcC = mcC;
        this.dbC = dbC;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ansClient = tcpC.receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG
                StringTokenizer tokenizer = new StringTokenizer(ansClient, "+");
                int count = tokenizer.countTokens();
                try {
                    if (count > 1) {
                        switch (Integer.parseInt(tokenizer.nextToken())) {
                            
                            case 1:
                            {
                                System.out.println("Recebi um registo");
                                String name = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String photopath = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.userRegister(name, username, password, photopath))
                                    tcpC.sendTCP("REGISTERED");
                                else tcpC.sendTCP("UNREGISTERD");
                                
                                break;
                            }
                            case 2:
                            {
                                System.out.println("Recebi um Login");
                                String username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.userLogin(username, password))
                                    tcpC.sendTCP("Logged");
                                else tcpC.sendTCP("UNLOGGED");
                                break;
                            }
                            
                            case 3:
                                System.out.println("Recebi um Canal Novo");
                                String name = tokenizer.nextToken();
                                String description = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.newChannel(name, description, password, username))
                                    tcpC.sendTCP("CREATED");
                                else tcpC.sendTCP("UNCREATED");
                                break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid! " + e);
                    continue;
                }

                //switch((int) tokenizer.nextToken()){
                //  case 1: System.out.println(tokenizer.nextElement().toString(), tokenizer.nextElement().toString());
                // default: continue;
                //}
                tcpC.sendTCP("CONNECTED_TCP");
            }
        } catch (SocketException ex) {
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
