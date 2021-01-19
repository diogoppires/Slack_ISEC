package web;

import ClientRMI.ClientRemote;
import ClientRMI.ClientRemoteInterface;
import ServerRMI.ServerRemoteInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIService extends UnicastRemoteObject {
    private static ServerRemoteInterface remObj;
    protected RMIService() throws RemoteException {
    }

    public static void RMIServiceInit(int port){
        String objectUrl;
        ClientRemote cR;

        try{
            objectUrl = "rmi://127.0.0.1/ServerRemote_" + port;
            /*
             * Obtem a referencia remota para o servico com nome "GetRemoteFile"
             */
            remObj = (ServerRemoteInterface) Naming.lookup(objectUrl);
            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
        } catch (RemoteException e) {
            System.out.println("Remote Error - " + e);
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(String msg) throws RemoteException {
        remObj.sendMsgAll(msg);
    }
}
