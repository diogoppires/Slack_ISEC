package ObserverRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRemoteInterface extends Remote {
     void notifyNewOperationConcluded(String description) throws RemoteException;
}
