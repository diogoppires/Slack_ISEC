package ServerRMI;

import ClientRMI.ClientRemoteInterface;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import Server.serverCommunication.CommsTypes.MulticastCommunication;
import Server.serverCommunication.Data.ClientData;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRemote extends UnicastRemoteObject implements ServerRemoteInterface {
    private static final String SERVICE_NAME = "ServerRemote";
    private final int serverId;
    private DBCommuncation dbCommuncation;
    private static List<ClientRemoteInterface> observersMessages, observersUsers;

    public ServerRemote(DBCommuncation dbCommuncation, int serverId) throws RemoteException {
        this.dbCommuncation = dbCommuncation;
        this.serverId = serverId;
        observersMessages = new ArrayList<>();
        observersUsers = new ArrayList<>();
    }

    @Override
    public void makeRegister(String name, String username, String password, String photo_path) throws RemoteException {
        //dbCommunication.userRegister(name, username, password, photo_path);
        System.out.println("TBD - Make Register");
    }

    @Override
    public void sendMsgAll(String msg) throws RemoteException {

    }

    @Override
    public void addObserverUsers(ClientRemoteInterface observer) throws RemoteException {
        if(!observersUsers.contains(observer)){
            observersUsers.add(observer);
            System.out.println("+ an user observer.");
        }
    }

    @Override
    public void addObserversMessages(ClientRemoteInterface observer) throws RemoteException {
        if(!observersMessages.contains(observer)){
            observersMessages.add(observer);
            System.out.println("+ a message observer.");
        }
    }

    @Override
    public void removeObserverUsers(ClientRemoteInterface observer) throws RemoteException {
        if(observersUsers.remove(observer))
            System.out.println("- an user observer.");
    }

    @Override
    public void removeObserverMessages(ClientRemoteInterface observer) throws RemoteException {
        if(observersMessages.remove(observer))
            System.out.println("- an message observer.");
    }

    public static synchronized void notifyObservers(String msg, int type)
    {
        int i;
        List<ClientRemoteInterface> observers = null;
        switch(type){
            case 1 -> observers = observersMessages;
            case 2 -> observers = observersUsers;
        }
        if(observers != null){
            for(i=0; i < observers.size(); i++){
                try{
                    observers.get(i).notifyNewOperationConcluded(msg);
                }catch(RemoteException e){
                    observers.remove(i--);
                    System.out.println("- an observer (an inaccessible observer)");
                }
            }
        }
    }

    @Override
    public void shutdown(){
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    public void run() {
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
            /*
             * Cria o servico
             */
            System.out.println("Service GetRemoteFile created and in execution ("+this.getRef().remoteToString()+"...");

            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */

            StringBuilder sb = new StringBuilder();
            String uniqueName = sb.append(SERVICE_NAME).append("_").append(serverId).toString();
            System.out.println(uniqueName);
            r.bind(uniqueName, this);
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
