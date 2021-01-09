package ClientRMI;

import ObserverRMI.ObserverRemoteInterface;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRemote extends UnicastRemoteObject implements ServerRemoteInterface {

    protected ClientRemote() throws RemoteException {
    }

    @Override
    public void makeRegister(String name, String username, String password, String photo_path) throws RemoteException {

    }

    @Override
    public void sendMsgAll(String msg) throws RemoteException {

    }

    @Override
    public void addObserver(ObserverRemoteInterface observer) throws RemoteException {

    }

    @Override
    public void removeObserver(ObserverRemoteInterface observer) throws RemoteException {

    }

    public static void main(String[] args) {
        String objectUrl;
        ClientRemote clientRemote;
        ServerRemoteInterface remObj;
        try{

            objectUrl = "rmi://localhost/ServerRemote";
            /*
             * Obtem a referencia remota para o servico com nome "GetRemoteFile"
             */
            remObj = (ServerRemoteInterface) Naming.lookup(objectUrl);
            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            clientRemote = new ClientRemote();
            remObj.makeRegister("Ola", "xau", "tau", "pau");
            remObj.sendMsgAll("ola");

        } catch (RemoteException e) {
            System.out.println("Remote Error - " + e);
        } catch (NotBoundException e) {
            System.out.println("Unknown Remote Service - " + e);
        } catch (IOException e) {
            System.out.println("Error E/S - " + e);
        }
    }
}
