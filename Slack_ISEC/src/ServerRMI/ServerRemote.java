package ServerRMI;

import ObserverRMI.ObserverRemoteInterface;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRemote extends UnicastRemoteObject implements ServerRemoteInterface {
    public static final String SERVICE_NAME = "ServerRemote";
    private DBCommuncation dbCommuncation;
    List<ObserverRemoteInterface> observersMessages, observersUsers;

    protected ServerRemote(/*DBCommuncation dbCommuncation*/) throws RemoteException {
        //this.dbCommuncation = dbCommuncation;
        observersMessages = new ArrayList<>();
        observersUsers = new ArrayList<>();
    }

    @Override
    public void makeRegister(String name, String username, String password, String photo_path) throws RemoteException {
        //dbCommuncation.userRegister(name, username, password, photo_path);
        System.out.println("TBD - Make Register");
        notifyObservers("New User", observersUsers);
    }

    @Override
    public void sendMsgAll(String msg) throws RemoteException {
        System.out.println("TBD - Send all a message");
        notifyObservers("New Message", observersMessages);
    }

    @Override
    public void addObserverUsers(ObserverRemoteInterface observer) throws RemoteException {
        if(!observersUsers.contains(observer)){
            observersUsers.add(observer);
            System.out.println("+ an user observer.");
        }
    }

    @Override
    public void addObserversMessages(ObserverRemoteInterface observer) throws RemoteException {
        if(!observersMessages.contains(observer)){
            observersMessages.add(observer);
            System.out.println("+ a message observer.");
        }
    }

    @Override
    public void removeObserverUsers(ObserverRemoteInterface observer) throws RemoteException {
        if(observersUsers.remove(observer))
            System.out.println("- an user observer.");
    }

    @Override
    public void removeObserverMessages(ObserverRemoteInterface observer) throws RemoteException {
        if(observersMessages.remove(observer))
            System.out.println("- an message observer.");
    }

    public synchronized void notifyObservers(String msg, List<ObserverRemoteInterface> observers)
    {
        int i;

        for(i=0; i < observers.size(); i++){
            try{
                observers.get(i).notifyNewOperationConcluded(msg);
            }catch(RemoteException e){
                observers.remove(i--);
                System.out.println("- an observer (an inaccessible observer)");
            }
        }
    }

    public static void main(String args[]) {
        /*
         * Lanca o rmiregistry localmente no porto TCP por omissao (1099) ou, caso este ja' se encontre
         * a correr, obtem uma referencia.
         */
        try {
            Registry r;
            try {
                System.out.println("Trying to launch Registry on port: " +
                        Registry.REGISTRY_PORT + "...");
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.out.println("Registry launched!");

            } catch (RemoteException e) {
                System.out.println("Registry probably in execution!");
                r = LocateRegistry.getRegistry();
            }
            ServerRemote service = new ServerRemote();
            /*
             * Cria o servico
             */
            System.out.println("Service GetRemoteFile created and in execution ("+service.getRef().remoteToString()+"...");

            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */
            r.bind(SERVICE_NAME, service);
            System.out.println("Service " + SERVICE_NAME + " registered on registry...");

        }catch(RemoteException e){
            System.out.println("Remote error - " + e);
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Error - " + e);
            System.exit(1);
        }
    }
}
