package ServerRMI;

import ObserverRMI.ObserverRemoteInterface;

import java.rmi.Remote;

public interface ServerRemoteInterface extends Remote {
     void makeRegister(String name, String username, String password, String photo_path) throws java.rmi.RemoteException;
     void sendMsgAll(String msg) throws java.rmi.RemoteException;

     void addObserverUsers(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
     void addObserversMessages(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
     void removeObserverUsers(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
     void removeObserverMessages(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
}
