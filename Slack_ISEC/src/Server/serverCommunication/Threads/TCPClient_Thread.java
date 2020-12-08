package Server.serverCommunication.Threads;

import Server.Utils.Channels;
import Server.Utils.Register;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.Data.ServerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {
    
    private static final int SIZE =  5000;
    private ClientData cD;
    private ServerInfo iS;
    private MulticastCommunication mcC;
    private DBCommuncation dbC;
    private InputStream inS; 
    private OutputStream outS; 
    
    
    public TCPClient_Thread(ClientData cD,ServerInfo iS,MulticastCommunication mcC, DBCommuncation dbC) {
        this.iS = iS;
        this.cD = cD;
        this.mcC = mcC;
        this.dbC = dbC;
    }
    public void sendTCP(String msg) throws IOException{
        outS.write(msg.getBytes());
        outS.flush();
    }
    
    public String receiveTCP() throws IOException{
        byte[] bufStr = new byte[SIZE];
        int nBytes = inS.read(bufStr);
        String tempStr = new String(bufStr, 0, nBytes);
        return tempStr;
    }

    @Override
    public void run() {
        try {
            
            inS = cD.getSocket().getInputStream();
            outS = cD.getSocket().getOutputStream();
            
            while (true) {
                String ansClient = receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG
                StringTokenizer tokenizer = new StringTokenizer(ansClient, "+");
                int count = tokenizer.countTokens();
                try {
                    if (count > 1) {
                        switch (Integer.parseInt(tokenizer.nextToken())) {
                            
                            case 1:{
                                System.out.println("Recebi um registo");
                                String name = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String photopath = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.userRegister(name, username, password, photopath)){
                                    sendTCP("REGISTERED");
                                    Register r = new Register(name, username, password, photopath);
                                    mcC.spreadInfo(r);
                                }
                                else sendTCP("UNREGISTERED");
                                break;
                            }
                            case 2:{
                                System.out.println("Recebi um Login");
                                String username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.userLogin(username, password)){
                                    cD.setUsername(username);
                                    sendTCP("Logged");
                                }
                                else sendTCP("UNLOGGED");
                                break;
                            }
                            
                            case 3: {
                                System.out.println("Recebi um Canal Novo");
                                String name = tokenizer.nextToken();
                                String description = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);

                                if (dbC.newChannel(name, description, password, username)) {
                                   sendTCP("CREATED");
                                    Channels c = new Channels(name, description, password, username, 1);
                                    mcC.spreadInfo(c);
                                }
                                else sendTCP("UNCREATED");
                                break;
                            }
                            case 4:{
                                System.out.println("Recebi uma Ediçao de canal");
                                String name = tokenizer.nextToken();
                                String newName = tokenizer.nextToken();
                                String description = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                System.out.println(username);
                                System.out.println(password);
                                
                                if(dbC.editChannel(name, newName, description, password, username))
                                    sendTCP("EDITED");
                                else sendTCP("PLEASE VERIFY lOGIN AND PASSWORD");
                                break;
                            }
                            case 8:{
                                System.out.println("Recebi uma Consulta");
                                String text = tokenizer.nextToken();
                                String teste = dbC.searchUserAndChannel(text);
                                System.out.println(teste);
                                sendTCP(teste);
                                break;
                            }
                            
                            case 5: {
                                System.out.println("Recebi um Pedido para Apagar Canal");
                                String name = tokenizer.nextToken();
                                String username = tokenizer.nextToken();
                                if(dbC.deleteChannel(name,username)){
                                    sendTCP("Channel Deleted");
                                }else{
                                    sendTCP("Failure Deleting channel");
                                }
                            }


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
//                tcpC.sendTCP("CONNECTED_TCP");
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
