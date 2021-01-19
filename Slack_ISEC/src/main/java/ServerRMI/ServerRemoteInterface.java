package ServerRMI;

import ClientRMI.ClientRemoteInterface;

import java.rmi.Remote;

public interface ServerRemoteInterface extends Remote {
     void makeRegister(String name, String username, String password, String photo_path) throws java.rmi.RemoteException;
     void sendMsgAll(String msg) throws java.rmi.RemoteException;
     void shutdown() throws  java.rmi.RemoteException;

     void addObserverUsers(ClientRemoteInterface observer) throws java.rmi.RemoteException;
     void addObserversMessages(ClientRemoteInterface observer) throws java.rmi.RemoteException;
     void removeObserverUsers(ClientRemoteInterface observer) throws java.rmi.RemoteException;
     void removeObserverMessages(ClientRemoteInterface observer) throws java.rmi.RemoteException;
}
