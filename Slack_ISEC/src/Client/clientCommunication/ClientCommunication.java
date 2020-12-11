package Client.clientCommunication;

import Client.Interface.Text.UIText;
import Client.clientCommunication.CommsType.TCP_Communication;
import Client.clientCommunication.CommsType.UDP_Communication;
import Server.serverCommunication.Data.ServerDetails;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCommunication {

    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static final String ANS_SUCCESS = "SUCCESS";
    private static final String ANS_FAIL = "FAIL";
    private UDP_Communication udpC;
    private TCP_Communication tcpC;
    private String serverIp;
    private int serverUdpPort;

    public ClientCommunication(int serverUdpPort, String serverIp) {
        this.serverIp = serverIp;
        this.serverUdpPort = serverUdpPort;
        this.udpC = new UDP_Communication();
    }

    private ArrayList getServersList(String serversListBuffer) {
        ArrayList<ServerDetails> serversList = new ArrayList<>();
        Scanner sc = new Scanner(serversListBuffer);
        while (sc.hasNext()) {
            serversList.add(new ServerDetails(sc.next(), sc.nextInt(), sc.nextInt()));
        }
        return serversList;
    }

    private void handleFail() throws IOException {
        //System.out.println("Recebi FaIl");
        String serversListBuffer = udpC.receiveUDP();
        //System.out.println(serversListBuffer);
        ArrayList<ServerDetails> serversList = getServersList(serversListBuffer);
        serverIp = serversList.get(0).getIpServer();
        serverUdpPort = serversList.get(0).getPortServer();
        System.out.println("ARRAY List");
        for (ServerDetails s : serversList) {
            System.out.println(s.toString());
        }
        System.out.println("Vou connectar ao Servidor: " + serverIp + ":" + serverUdpPort);
    }

    /**
     * This method will make the client ask to a server via an UDP connection
     * for a TCP connection.
     *
     * @return true if the connection was made with success and false if not.
     */
    public boolean askForConnection() {
        try {
            udpC.initializeUDP();
        } catch (SocketException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int attempt = 0;
            while (attempt < 5) {
                udpC.setTimeout(5000);
                udpC.sendUDP(TCP_CONNECTION, InetAddress.getByName(serverIp), serverUdpPort);
                String ans = udpC.receiveUDP();
                if (!ans.equals(ANS_FAIL)) {
                    int serverTcpPort = Integer.parseInt(ans);
                    //
                    System.out.println("TCP PORT: " + serverTcpPort); //DEBUG
                    tcpC = new TCP_Communication(serverIp,
                            serverTcpPort);
                    tcpC.initializeTCP();
                      //tcpC.sendTCP("Sent by client");               //[DEBUG]
//                    System.out.println("Received by server: " + tcpC.receiveTCP()); //[DEBUG]
                    break;
                } else {
                    handleFail();
                }
                attempt++;
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("Não Existe um Servidor Disponivel");
            return false;
        } catch (IOException ex) {
            System.err.println("askForConnection : " + ex);
            return false;
        }
        udpC.closeUDP();
        return true;
    }

    public boolean sendMessage(String s) {

        try {
            tcpC.sendTCP(s);
        } catch (IOException ex) {
            try {
                System.err.println("Nova Tentativa!");
                if (askForConnection()) {
                    tcpC.sendTCP(s);
                }
            } catch (IOException ex1) {
                System.err.println("Sem servidores Disponiveis!");
                return false;
            }
        }
        return true;
    }

    public TCP_Communication getTCP() {
        return tcpC;
    }

    public boolean createThreadTCP() {

        Runnable runnable = () -> {
            while (true) {
                try {
                    String receiveTCP = tcpC.receiveTCP();
                    System.out.println(receiveTCP);
                } catch (SocketException ex) {
                    System.err.println("O Servidor terminou inesperadamente");
                    if(!askForConnection())
                        return;
                } catch (IOException ex) {
                    System.err.println("Erro: " + ex);
                    return;
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
        return true;
    }

    public String awaitResponse() {
        String receiveTCP = "";

        while (true) {
            try {
                receiveTCP = tcpC.receiveTCP();
                System.out.println(receiveTCP);
            } catch (SocketException ex) {
                System.err.println("O SERVIDOR TERMINOU INESPERADAMENTE");
                askForConnection();
            } catch (IOException ex) {
                System.err.println("O SERVIDOR TERMINOU IO");
            }
            
            StringTokenizer tokenizer = new StringTokenizer(receiveTCP, "+");
            if (receiveTCP.startsWith("100")) {
                
                Integer.parseInt(tokenizer.nextToken());
                serverIp = tokenizer.nextToken();
                serverUdpPort = Integer.parseInt(tokenizer.nextToken());
                System.out.println("Ip: " + serverIp + " Port: " + serverUdpPort);
                /*if (tokenizer.hasMoreTokens()) {
                    String buffer = tokenizer.nextToken();
                    if (buffer.contains("Logged")) {
                        UIText.setValidation(true);
                        createThreadTCP();
                    }
                }*/ 
                continue;
            } else if (receiveTCP.contains("Logged")) {
                UIText.setValidation(true);
                createThreadTCP();
                break;
            } else {
                return "";
            }
        }
        return receiveTCP;
    }
}
 