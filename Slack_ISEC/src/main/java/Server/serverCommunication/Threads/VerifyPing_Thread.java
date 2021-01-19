/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.Data.ServerData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread will verify the ping flag from all servers that are connected. If
 * the flag is false the server will be removed otherwise the thread will change
 * the flag value.
 */
public class VerifyPing_Thread extends Thread {

    private final ServerInfo iS;
    private ArrayList<ClientData> clientsConnections;
    private AtomicBoolean end;

    public VerifyPing_Thread(ServerInfo iS, AtomicBoolean end, ArrayList<ClientData> clientsConnections) {
        this.iS = iS;
        this.end = end;
        this.clientsConnections = clientsConnections;
    }

    @Override
    public void run() {
        ServerData backupServer = null;
        while (!end.get()) {
            synchronized (iS) {
                int serverToRemove = 0;
                boolean remove = false;
                for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {

                    if (obj.getValue().getPing()) {
                        obj.getValue().setPing(false);
                    } else {
                        serverToRemove = obj.getKey();
                        remove = true;

                    }
                }
                // ------- Send Backup Server To Clients Connected -------

                if (iS.getAllServersData().size() > 1) {
                    if (remove || backupServer == null) {
                        if (remove) {
                            iS.removeServer(serverToRemove);
                        }
                        Set set = iS.getAllServersData().keySet();
                        Iterator it = set.iterator();
                        while (it.hasNext()) {
                            int port = (int) it.next();
                            ServerData sData = iS.getAllServersData().get(port);
                            if (backupServer != sData) {
                                backupServer = sData;
                                if (sData.getPortServer() != iS.getServerId()) {
                                    for (ClientData clientsConnection : clientsConnections) {
                                        String buffer = "100+" + sData.getIPServer() + "+" + sData.getPortServer();
                                        clientsConnection.sentTcpText(buffer);
                                        clientsConnection.setServerBackup(backupServer);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                // Send First Bakcup Server
                for (ClientData client : clientsConnections) {
                    if (client.getServerBackup() == null && backupServer != null) {
                        client.setServerBackup(backupServer);
                        String buffer = "100+" + backupServer.getIPServer() + "+" + backupServer.getPortServer() + "+";
                        client.sentTcpText(buffer);
                    }
                }

                // ---------------------------------------------
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                // REtirar cliente que saiu
                Logger.getLogger(VerifyPing_Thread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("[VERIFY PING THREAD]: Closed.");
    }
}
