package ClientRMI;

import ObserverRMI.ObserverRemoteInterface;
import ServerRMI.ServerRemoteInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRemote extends UnicastRemoteObject implements ServerRemoteInterface, ObserverRemoteInterface {

    public ClientRemote() throws RemoteException {
    }

    @Override
    public void makeRegister(String name, String username, String password, String photo_path) throws RemoteException {

    }

    @Override
    public void sendMsgAll(String msg) throws RemoteException {

    }

    @Override
    public void addObserverUsers(ObserverRemoteInterface observer) throws RemoteException {

    }

    @Override
    public void addObserversMessages(ObserverRemoteInterface observer) throws RemoteException {

    }

    @Override
    public void removeObserverUsers(ObserverRemoteInterface observer) throws RemoteException {

    }

    @Override
    public void removeObserverMessages(ObserverRemoteInterface observer) throws RemoteException {

    }

    @Override
    public void notifyNewOperationConcluded(String description) throws RemoteException {
        System.out.println(description);
    }

    public static void main(String args[]) {
        String objectUrl;
        ClientRemote clientRemote;
        ServerRemoteInterface remObj;
        try{

            objectUrl = "rmi://127.0.0.1/ServerRemote";
            /*
             * Obtem a referencia remota para o servico com nome "GetRemoteFile"
             */
            remObj = (ServerRemoteInterface) Naming.lookup(objectUrl);
            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            clientRemote = new ClientRemote();

            remObj.addObserversMessages(clientRemote);
            remObj.addObserverUsers(clientRemote);
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
