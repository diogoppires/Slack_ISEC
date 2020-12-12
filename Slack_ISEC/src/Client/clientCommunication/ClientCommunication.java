package Client.clientCommunication;

import Client.Interface.Text.UIText;
import Client.clientCommunication.CommsType.TCP_Communication;
import Client.clientCommunication.CommsType.UDP_Communication;
import Server.serverCommunication.Data.ServerDetails;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCommunication {

    private final static String EXIT_SUCCESSFULLY = "exitByClient";
    private static final String TCP_CONNECTION = "TCP_CONNECTION?";
    private static final String ANS_FAIL = "FAIL";
    private static final int MAX_DATA = 5000;
    private UDP_Communication udpC;
    private TCP_Communication tcpC;
    private String serverIp;
    private int serverUdpPort;
    private int serverTcpPort;
    FileInputStream fileIS;

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
                    serverTcpPort = Integer.parseInt(ans);
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
            String receiveTCP = "";
            try {

                while (true) {
                    receiveTCP = tcpC.receiveTCP();
                    System.out.println(receiveTCP);
                    StringTokenizer tokenizer = new StringTokenizer(receiveTCP, "+");
                    switch (Integer.parseInt(tokenizer.nextToken())) {
                        case 201: {
                            int port = Integer.parseInt(tokenizer.nextToken());
                            sendFileThread(port);
                            break;
                        }
                        case 202: {
                            int port = Integer.parseInt(tokenizer.nextToken());

                            TCP_Communication receiveFile = new TCP_Communication(serverIp, port);
                            receiveFile.initializeTCP();
                            // Envia nome do Ficheiro;
                            Runnable receiveFileRun = () -> {
                                try {
                                    String fileName = receiveFile.receiveTCP();
                                    String home = System.getProperty("user.home");
                                    File localFilePath = new File(home + "/Downloads/" + fileName);
                                    if (localFilePath.exists()) {
                                        System.err.println("[ThreadDownloadFromServer] -> O Ficheiro já existe.");
                                        StringTokenizer nameTokenizer = new StringTokenizer(fileName, ".");
                                        String name = nameTokenizer.nextToken();
                                        String extension = nameTokenizer.nextToken();
                                        int i = 0;
                                        while (localFilePath.exists()) {
                                            String aux = name + "(" + ++i + ")";
                                            localFilePath = new File(home + "/Downloads/" + aux + "." + extension);
                                        }
                                    }
                                    FileOutputStream fileOS = new FileOutputStream(localFilePath.getCanonicalPath());
                                    receiveFile.receiveFileTCP(fileOS, MAX_DATA);

                                    System.err.println("[ThreadReceiveFile] -> Download Complete");
                                    System.out.println("Download Complete : " + localFilePath.getName());
                                    fileOS.close();
                                    receiveFile.closeTCP();

                                } catch (IOException ex) {
                                    System.err.println("[ThreadReceiveFile] -> Erro:" + ex);

                                }
                            };
                            Thread t1 = new Thread(receiveFileRun);
                            t1.start();
                            break;
                        }
                        case 203: {
                            // O Ficheiro Solicitado não existe.
                            System.out.println(tokenizer.nextToken());
                            break;
                        }
                        case 0:
                            System.out.println(tokenizer.nextToken());
                            break;
                        default:
                            System.out.println(receiveTCP);
                            break;

                    }
                } // end of while
            } catch (SocketException ex) {
                System.err.println("O Servidor terminou inesperadamente");
                if (!askForConnection()) {
                    return;
                }
            } catch (IOException ex) {
                System.err.println("Erro: " + ex);
                return;
            } finally {
                try {
                    tcpC.closeTCP();
                    tcpC.receiveTCP();

                } catch (IOException e) {
                    System.exit(1);
                }
            } //end of try

        }; // end of runnable

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
            } else if (receiveTCP.startsWith("201")) {
                // Consome 201;
                tokenizer.nextToken();
                int port = Integer.parseInt(tokenizer.nextToken());
                sendFileThread(port);
                break;

            } else if (receiveTCP.contains("Logged")) {
                UIText.setValidation(true);
                createThreadTCP();
                break;
            } else if (receiveTCP.contains("REGISTERED") && !receiveTCP.contains("UNREGISTERED")) {
                return "REGISTERED";
            } else {
                return "";
            }
        }
        return receiveTCP;
    }

    public void sendFile(String localDirectory, String fileName, String destination) {

        File directory = new File(localDirectory);
        File file = new File(localDirectory + File.separator + fileName);

        System.out.println("file: " + file.toString());

        if (!file.exists()) {
            System.out.println("o Ficheiro nao existe");
            return;
        }
        if (!directory.exists()) {
            System.out.println("A diretoria \"" + file + "\" nao existe)");
            return;
        }
        if (!directory.isDirectory()) {
            System.out.println("O caminho \"" + file + "\" não se refere a uma diretoria!");
            return;
        }
        if (!directory.canRead()) {
            System.out.println("Sem permissoes de leitura na diretoria \"" + file + "\"");
            return;
        }
        try {
            fileIS = new FileInputStream(file);
            tcpC.sendTCP("201+" + fileName + "+" + destination);
        } catch (IOException ex) {
            System.err.println("Enviar pedido para upload Ficheiro");
        }
    }

    public void receiveFile(int fileCode) {
        try {
            tcpC.sendTCP("202+" + fileCode);
        } catch (IOException ex) {
            System.err.println("Enviar pedido para download Ficheiro");
        }
    }

    public void sendRegister(String s, String localDirectory, String fileName) {
        sendMessage(s);
        if (awaitResponse().equals("REGISTERED")) {
            sendFile(localDirectory, fileName, "profile");
            awaitResponse();
        }

    }

    private void sendFileThread(int port) {
        System.out.println("envia ficheiro: " + port);
        TCP_Communication sendFile = new TCP_Communication(serverIp, port);
        sendFile.initializeTCP();
        // Envia nome do Ficheiro;
        Runnable sendFileRun = () -> {
            try {
                System.err.println("Thread criada para enviar Ficheiro.");
                byte[] bufStr = new byte[MAX_DATA];
                while (fileIS.available() != 0) {
                    bufStr = fileIS.readNBytes(MAX_DATA);
                    sendFile.sendTCP(bufStr);
                }
                fileIS.close();
                sendFile.closeTCP();
            } catch (IOException ex) {
                System.err.println("[Thread Upload] - Erro a enviar o ficheiro.");
            } finally {
                try {
                    fileIS.close();
                    sendFile.closeTCP();
                } catch (IOException ex) {
                    Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Thread t2 = new Thread(sendFileRun);
        t2.start();
    }
}
