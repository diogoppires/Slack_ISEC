package Server.serverCommunication.Threads;

import Server.Utils.*;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.CommsTypes.TCPCommunication;
import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.Data.ServerInfo;
import ServerRMI.ServerRemote;

import java.io.*;

import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient_Thread implements Runnable {

    private static final int SIZE = 5000;
    private ClientData cD;
    private final ServerInfo iS;
    private MulticastCommunication mcC;
    private DBCommuncation dbC;
    private InputStream inS;
    private OutputStream outS;
    private TCPCommunication tcpC;
    private String username;

    public TCPClient_Thread(ClientData cD, ServerInfo iS, MulticastCommunication mcC, DBCommuncation dbC, TCPCommunication tcpC) {
        this.iS = iS;
        this.cD = cD;
        this.mcC = mcC;
        this.dbC = dbC;
        this.tcpC = tcpC;
    }

    public void sendTCP(String msg) throws IOException {
        outS.write(msg.getBytes());
        outS.flush();
    }

    public String receiveTCP() throws IOException {
        byte[] bufStr = new byte[SIZE];
        int nBytes = inS.read(bufStr);
        String tempStr = new String(bufStr, 0, nBytes);
        return tempStr;
    }

    public int receiveFile(String fileName, String destination) {
        // Cria Novo Server Socket
        try {
            TCPCommunication receiveFileTCP = new TCPCommunication(0);
            receiveFileTCP.initializeTCP();
            sendTCP("201+" + receiveFileTCP.getServerPort());
            receiveFileTCP.acceptConnection();
            // Thread Receive File
            Runnable runnable = () -> {
                FileOutputStream fileOS = null;
                try {
                    String directoryName = "";
                    if (destination.equals("profile"))
                         directoryName += "C:\\" + iS.getServerId() + "\\profileImages";
                    else
                        directoryName += "C:\\" + iS.getServerId();
                    File localdirectory = new File(directoryName);
                    System.out.println(localdirectory.toPath());
                    if (!localdirectory.exists()) {
                        Files.createDirectories(localdirectory.toPath());
                    }
                    File localFilePath = new File(directoryName + File.separator + fileName);
                    if (localFilePath.exists()) {
                        System.err.println("[ThreadDownloadFromClient] -> O Ficheiro já existe.");
                        StringTokenizer nameTokenizer = new StringTokenizer(fileName, ".");
                        String name = nameTokenizer.nextToken();
                        String extension = nameTokenizer.nextToken();
                        int i = 0;
                        while (localFilePath.exists()) {
                            String aux = name + "(" + ++i + ")";
                            localFilePath = new File(directoryName + File.separator + aux + "." + extension);
                        }
                    }
                    System.out.println(localFilePath.getCanonicalPath());
                    fileOS = new FileOutputStream(localFilePath.getCanonicalPath());
                    receiveFileTCP.receiveTcpFile(fileOS, SIZE);
                    System.err.println("[ThreadDownloadFromClient] -> Download from Client Complete");

                    int fileID =  dbC.insertFile(destination, username, localFilePath.getCanonicalPath());
                    if(destination.equals("profile")){
                        dbC.setUserPhotoID(username, fileID);
                    }

                    System.err.println("[ThreadDownloadFromClient] -> Finish");


                    //NEW --> Spreading file
                    System.out.println("[Spreading]Beginning spreading...");
                    spreadFile(localFilePath, fileName, destination, iS.getServerId(), fileID);
                } catch (IOException ex) {
                    Logger.getLogger(TCPClient_Thread.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        fileOS.close();
                        receiveFileTCP.closeTCP();
                    } catch (IOException ex1) {
                        Logger.getLogger(TCPClient_Thread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            };
            Thread t1 = new Thread(runnable);
            t1.start();
        } catch (IOException ex) {
            System.err.println("[TCPClient_Thread]-Erro a Criar a Thread para Receber Ficheiro");
        }
        
        return 0;
    }

    private void spreadFile(File localFilePath, String fileName, String destination, int serverId, int fileId) throws IOException {
        FileInputStream fIn = new FileInputStream(localFilePath);
        byte[] bufStr;
        int nTimes = 0;
        while(fIn.available() != 0){
            bufStr = fIn.readNBytes(SIZE);
            Chunk ck = new Chunk(fileName, username, destination, fileId,serverId, nTimes, bufStr, false);
            mcC.spreadInfo(ck);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nTimes++;
            System.out.println(nTimes + " - Aqui(TCPCLIENT_THREAD)");
        }
        Chunk ck = new Chunk(fileName, username, destination, fileId, serverId, nTimes, new byte[0], true);
        mcC.spreadInfo(ck);
        fIn.close();
    }

    @Override
    public void run() {
        try {
            inS = cD.getSocket().getInputStream();
            outS = cD.getSocket().getOutputStream();

            while (true) {
                String ansClient = receiveTCP();
                System.out.println("[TCP] Received: " + ansClient); // [DEBUG]
                StringTokenizer tokenizer = new StringTokenizer(ansClient, "+");
                int count = tokenizer.countTokens();
                try {
                    if (count > 1) {
                        switch (Integer.parseInt(tokenizer.nextToken())) {
                            case 1: {
                                System.out.println("Recebi um registo");
                                String name = tokenizer.nextToken();
                                username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                String photopath = tokenizer.nextToken();
                                //System.out.println(username);
                                //System.out.println(password);
                                if (dbC.userRegister(name, username, password, photopath)) {
                                    sendTCP("101+REGISTERED");
                                    Register r = new Register(name, username, password, photopath, iS.getServerId());
                                    synchronized (iS) {
                                        mcC.spreadInfo(r);
                                    }
                                } else {
                                    sendTCP("101+UNREGISTERED");
                                }
                                break;
                            }
                            case 2: {
                                System.out.println("Recebi um Login");
                                username = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                //System.out.println(username);
                                //System.out.println(password);

                                if (dbC.userLogin(username, password)) {
                                    cD.setUsername(username);
                                    sendTCP("Logged");
                                } else {
                                    sendTCP("101+UNLOGGED");
                                }
                                break;
                            }

                            case 3: {
                                System.out.println("Recebi um Canal Novo");
                                String name = tokenizer.nextToken();
                                String description = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                //System.out.println(username);
                                //System.out.println(password);

                                if (dbC.newChannel(name, description, password, username)) {
                                    sendTCP("101+CREATED");
                                    Channels c = new Channels(name, description, password, username, 1, iS.getServerId());
                                    synchronized (iS) {
                                        mcC.spreadInfo(c);
                                    }
                                } else {
                                    sendTCP("101+UNCREATED");
                                }
                                break;
                            }
                            case 4: {
                                System.out.println("Recebi uma Ediçao de canal");
                                String name = tokenizer.nextToken();
                                String newName = tokenizer.nextToken();
                                String description = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                //System.out.println(username);
                                //System.out.println(password);

                                if (dbC.editChannel(name, newName, description, password, username)) {
                                    sendTCP("101+EDITED");
                                } else {
                                    sendTCP("101+PLEASE VERIFY LOGIN AND PASSWORD");
                                }
                                break;
                            }
                            case 5: {
                                System.out.println("Recebi um Pedido para Apagar Canal");
                                String name = tokenizer.nextToken();
                                if (dbC.deleteChannel(name, username)) {
                                    sendTCP("101+Channel Deleted");
                                } else {
                                    sendTCP("101+Failure Deleting channel");
                                }
                                break;
                            }
                            case 6: {
                                System.out.println("Recebi uma nova conversação.");
                                String receiver = tokenizer.nextToken();
                                String msg = tokenizer.nextToken();
                                System.out.println("Emissor: " + username);     //[DEBUG]
                                System.out.println("Recetor: " + receiver);     //[DEBUG]
                                System.out.println("Msg: " + msg);              //[DEBUG]
                                if (dbC.conversation(username, receiver, msg)) {
                                    ServerRemote.notifyObservers("Enviei coisas", 1);
                                    sendTCP("101+Message sent.");
                                    Conversation conv = new Conversation(username, msg, receiver, iS.getServerId());
                                    synchronized (iS) {
                                        mcC.spreadInfo(conv);
                                    }
                                } else {
                                    sendTCP("101+An error has occurred and the message was not sent.");
                                }
                                break;
                            }
                            case 8: {
                                System.out.println("Recebi uma Consulta de Lista");
                                tokenizer.nextToken();
                                String response = dbC.showAllUsersAndChannels();
                                //System.err.println(response); /*DEBUG*/
                                sendTCP("0+" + response);
                                break;
                            }
                            case 9: {
                                System.out.println("Recebi uma Consulta de Procura");
                                String text = tokenizer.nextToken();
                                String response = dbC.searchUserAndChannel(text);
                                System.err.println(response);
                                /*DEBUG*/
                                sendTCP("0+" + response);
                                break;
                            }
                            case 10:{
                                System.out.println("Recebi uma Consulta de Mensagens");
                                String nameOrg = tokenizer.nextToken();
                                String nameDest = tokenizer.nextToken();
                                String n = tokenizer.nextToken();
                                String response = dbC.searchMessages(nameOrg, nameDest, n);
                                System.out.println(response);
                                /*DEBUG*/
                                sendTCP("0+" + response);
                                break;
                            }
                            case 13 :{
                                System.out.println("Recebi uma Consulta de Users + Lista -> USER");
                                String response = dbC.getAllUsersAndChannels();
                                sendTCP("13+" + response);
                                break;
                            }


                            case 201: {
                                String fileName = tokenizer.nextToken();
                                String destination = tokenizer.nextToken();
                                receiveFile(fileName, destination);
                                break;
                            }
                            case 202: {
                                try {
                                    String fileCode = tokenizer.nextToken();
                                    String filePath = dbC.getFilePath(fileCode);
                                    File fileToSend = new File(filePath);

                                    if (filePath.equals("0") || !fileToSend.exists()) {
                                        sendTCP("203+O Ficheiro Indicado não Existe!");
                                        break;
                                    }
                                    TCPCommunication sendFileTCP = new TCPCommunication(0);
                                    sendFileTCP.initializeTCP();
                                    sendTCP("202+" + sendFileTCP.getServerPort());
                                    sendFileTCP.acceptConnection();
                                    Runnable runnable = () -> {
                                        try {
                                            sendFileTCP.sendTCP(fileToSend.getName());
                                            FileInputStream fileIS = new FileInputStream(filePath);
                                            sendFileTCP.sendFile(fileIS, SIZE);
                                            fileIS.close();
                                            sendFileTCP.closeTCP();
                                            System.err.println("[TCPClient_Thread] -> Ficheiro Enviado com sucesso.");
                                        } catch (IOException ex) {
                                            System.err.println("[TCPClient_Thread]-Erro a Criar a Thread para Enviar Ficheiro");
                                        }

                                    };
                                    Thread t1 = new Thread(runnable);
                                    t1.start();

                                } catch (IOException ex) {
                                    Logger.getLogger(TCPClient_Thread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            }

                            case 11:{
                                System.out.println("Recebi uma Consulta de Dados dos Canais");
                                tokenizer.nextToken();
                                String response = dbC.getChannelInfo();
                                //System.err.println(response);
                                if( response != null) {
                                    sendTCP("0+" + response);
                                } else
                                    sendTCP("101+No data to show");

                                break;
                            }
                            case 12:{
                                System.out.println("Recebi um pedido para se juntar ao channel");
                                String nameChannel = tokenizer.nextToken();
                                String password = tokenizer.nextToken();
                                if(dbC.joinChannel(nameChannel,password,username)){
                                    sendTCP("101+Username added to channel");
                                }else
                                    sendTCP("101+Failed to add Username");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid! " + e);
                    continue;
                }
            }
        } catch (SocketException ex) {
            System.out.println("[CLOSED]: TCPClient_Thread");
        } catch (IOException ex) {

        }
        synchronized (iS) {
            iS.subClient();
            try {
                mcC.spreadInfo(iS);
            } catch (SocketException e) {
                System.out.println("[TCPClient_Thread]Multicast closed before finishing thread.");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
