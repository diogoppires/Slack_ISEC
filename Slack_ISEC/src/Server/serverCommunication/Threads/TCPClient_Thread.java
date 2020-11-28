package Server.serverCommunication.Threads;

import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {

    private TCPCommunication tcpC;
    private DBCommuncation dbC;

    public TCPClient_Thread(TCPCommunication tcpC, DBCommuncation dbC) {
        this.tcpC = tcpC;
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
                                System.out.println("Recebi um registo");
                                String username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                dbC.userRegister(username, password);
                                break;
                            case 2:
                                break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid!");
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
    }
}
