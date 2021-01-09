package ObserverRMI;

import ClientRMI.ClientRemote;
import ServerRMI.ServerRemoteInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverRemote extends UnicastRemoteObject implements ObserverRemoteInterface {
    ServerRemoteInterface service;


    public ObserverRemote() throws RemoteException { }

    @Override
    public void notifyNewOperationConcluded(String description) throws RemoteException {
        System.out.println(description);
    }

    public void removeObservers(){
        service.removeObserverMessages(observer);
        service.removeObserverUsers(observer);
        UnicastRemoteObject.unexportObject(observer, true);
    }

    public static void main(String args[]){
        try{
            //Cria e lanca o servico
            ObserverRemote observer = new ObserverRemote();
            System.out.println("Service ObserverRemote created and being executed..");

            //Localiza o servico remoto nomeado "GetRemoteFile"
            String objectUrl = "rmi://localhost/ServerRemote";

//            if(args.length > 0)
//                objectUrl = "rmi://"+args[0]+"/GetRemoteFile";

            service = (ServerRemoteInterface) Naming.lookup(objectUrl);

            //adiciona observador no servico remoto
            service.addObserversMessages(observer);
            service.addObserverUsers(observer);

            System.out.println("<Enter> to end...");
            System.out.println();
            //System.in.read();

        }catch(RemoteException e){
            System.out.println("Remote Error - " + e);
            System.exit(1);
        }catch(IOException | NotBoundException e){
            System.out.println("Error - " + e);
            System.exit(1);
        }
    }
}
