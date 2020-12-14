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

    private MulticastSocket mSocket;
    private ServerInfo infoServer;
    private DBCommuncation dbC;
    private ArrayList<ClientData> clientsConnections;

    public ServerListener_Thread(MulticastSocket mSocket, ServerInfo infoServer, DBCommuncation dbC, ArrayList<ClientData> clientsConnections) {
        this.mSocket = mSocket;
        this.infoServer = infoServer;
        this.dbC = dbC;
        this.clientsConnections = clientsConnections;
    }

    @Override
    public void run() {
        try {
            while (true) {
                DatagramPacket dP = new DatagramPacket(new byte[512], 512);
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
                            String str = "300+O user " + cReceived.getChUserAdmin() + " enviou uma mensagem";
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
                            sb.append("301+NEW - [").append(convReceived.getUserSender()).append("]:");
                            sb.append(convReceived.getMessage());
                            out.write(sb.toString().getBytes());
                            out.flush();
                        }
                    }

                } else if (receivedObj.getClass() == InfoFiles.class) {

                }
                else if (receivedObj.getClass() == DataRequest.class) {
                    DataRequest datarequest = (DataRequest)receivedObj;
                    dbC.getDatatoUpdate(datarequest.getTime(), datarequest.dbName());

                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("[LISTENER THREAD]: Closed.");
        }
    }
}
