package ClientRMI;

import ObserverRMI.ObserverRemoteInterface;

import java.rmi.Remote;

public interface ServerRemoteInterface extends Remote {
    public void makeRegister(String name, String username, String password, String photo_path) throws java.rmi.RemoteException;
    public void sendMsgAll(String msg) throws java.rmi.RemoteException;

    public void addObserver(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
    public void removeObserver(ObserverRemoteInterface observer) throws java.rmi.RemoteException;
}
