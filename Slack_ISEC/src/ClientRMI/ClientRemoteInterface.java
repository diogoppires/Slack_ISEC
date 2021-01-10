package ClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote {
     void notifyNewOperationConcluded(String description) throws RemoteException;
}
