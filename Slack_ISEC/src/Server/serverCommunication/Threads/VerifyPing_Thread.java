/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.serverCommunication.Threads;

import Server.serverCommunication.Data.ServerInfo;
import Server.serverCommunication.Data.ServerData;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This thread will verify the ping flag from all servers that are connected.
 * If the flag is false the server will be removed otherwise the thread will change the flag value.
 */
public class VerifyPing_Thread extends Thread {
    private final ServerInfo iS;
    private AtomicBoolean end;

    public VerifyPing_Thread(ServerInfo iS, AtomicBoolean end) {
        this.iS = iS;
        this.end = end;
    }

    @Override
    public void run() {
        while(!end.get()) {
            for (Map.Entry<Integer, ServerData> obj : iS.getAllServersData().entrySet()) {
                synchronized (iS.getAllServersData()) {
                    if (obj.getValue().getPing()) {
                        obj.getValue().setPing(false);
                    } else {
                        iS.removeServer(obj.getKey());
                    }
                }
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VerifyPing_Thread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("[VERIFY PING THREAD]: Closed.");
    }
}
