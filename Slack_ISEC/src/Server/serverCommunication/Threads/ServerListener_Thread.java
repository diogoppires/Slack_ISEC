package Server.serverCommunication.Threads;

import Server.Utils.*;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.Data.ServerData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * This thread will listen all the information that is going to be spread from a
 * multicast group.
 */
public class ServerListener_Thread extends Thread {
    private static int MAX_DATA = 10000;
    private MulticastSocket mSocket;
    private ServerInfo infoServer;
    private DBCommuncation dbC;
    private ArrayList<ClientData> clientsConnections;
    private ArrayList<ReceiveFiles> rFiles;

    public ServerListener_Thread(MulticastSocket mSocket, ServerInfo infoServer, DBCommuncation dbC, ArrayList<ClientData> clientsConnections) {
        this.mSocket = mSocket;
        this.infoServer = infoServer;
        this.dbC = dbC;
        this.clientsConnections = clientsConnections;
        rFiles = new ArrayList<>();
    }

int i = 0;
    @Override
    public void run() {
        try {
            while (true) {
                DatagramPacket dP = new DatagramPacket(new byte[MAX_DATA], MAX_DATA);
                mSocket.receive(dP);
                ObjectInputStream oIN = new ObjectInputStream(new ByteArrayInputStream(dP.getData()));
                Object receivedObj = oIN.readObject();

                if (receivedObj.getClass() == ServerData.class) {
                    ServerData sdReceived = (ServerData) receivedObj;
                    /*System.out.println("(server" + sdReceived.getPortServer() + ") IP: "
                            + sdReceived.getIPServer() + " Port: " + sdReceived.getPortServer()
                            + " Clients: " + sdReceived.getNClientsServer() + "\n");
                    */
                    synchronized (infoServer) {
                        infoServer.getAllServersData().put(sdReceived.getPortServer(), sdReceived);
                    }
                } else if (receivedObj.getClass() == Register.class) {
                    Register rReceived = (Register) receivedObj;
                    dbC.userRegister(rReceived.getName(),
                            rReceived.getUsername(),
                            rReceived.getPassword(),
                            rReceived.getPhotoPath());
                } else if (receivedObj.getClass() == Authentication.class) {
                    Authentication aReceived = (Authentication) receivedObj;
                    dbC.userLogin(aReceived.getUsername(),
                            aReceived.getPassword());

                } else if (receivedObj.getClass() == Channels.class) {
                    
                    //############################# DEBUG
                    Channels cReceived = (Channels) receivedObj;
                    for (ClientData clientsConnection : clientsConnections) {
                        OutputStream out = clientsConnection.getSocket().getOutputStream();

                        if (cReceived.getChName().equals(clientsConnection.getUsername())) {
                            String str = "O user " + cReceived.getChUserAdmin() + " enviou uma mensagem";
                            out.write(str.getBytes());
                            out.flush();
                            System.out.println(clientsConnection.getSocket().getPort());

                        }

                    }
                     //############################# DEBUG

                } else if (receivedObj.getClass() == Conversation.class) {
                    Conversation convReceived = (Conversation) receivedObj;
                    for(ClientData clientsConnection : clientsConnections){
                        OutputStream out = clientsConnection.getSocket().getOutputStream();
                        if(convReceived.getUserReceiver().equals(clientsConnection.getUsername())){
                            StringBuilder sb = new StringBuilder();
                            sb.append("0+NEW - [").append(convReceived.getUserSender()).append("]:");
                            sb.append(convReceived.getMessage());
                            out.write(sb.toString().getBytes());
                            out.flush();
                        }
                    }

                } else if (receivedObj.getClass() == Chunk.class) {
                    Chunk ck = (Chunk) receivedObj;
                    if (infoServer.getServerId() != ck.getServerId()) {
                        for (ReceiveFiles rF : rFiles) {
                            if (rF.getServerId() == ck.getServerId() &&
                                    rF.getFileName().equals(ck.getFileName())) {
                                rF.addChunk(ck, dbC);
                                if (ck.isEnd()) {
                                    for (ClientData clientsConnection : clientsConnections) {
                                        OutputStream out = clientsConnection.getSocket().getOutputStream();
                                        if (ck.getDestination().equals(clientsConnection.getUsername())) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("0+NEW FILE AVAILABLE - [").append(ck.getSender()).append("] - CODE ");
                                            sb.append(ck.getFileId());
                                            out.write(sb.toString().getBytes());
                                            out.flush();
                                        }
                                    }
                                }
                            }
                        }

                        if (ck.getPos() == 0) {
                            ReceiveFiles rf = new ReceiveFiles(ck.getFileName(),
                                    ck.getServerId(),
                                    infoServer.getServerId(),
                                    ck.getDestination());
                            rf.buildFile();
                            rf.addChunk(ck, dbC);
                            rFiles.add(rf);
                        }
                    }
                }
                else if (receivedObj.getClass() == DataRequest.class) {
                    DataRequest datarequest = (DataRequest)receivedObj;
                    dbC.getDatatoUpdate(datarequest.getTime(), datarequest.dbName());
                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
//            System.out.println("[LISTENER THREAD]: Closed.");
        }
    }
}
