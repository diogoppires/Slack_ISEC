package ClientRMI;

import ObserverRMI.ObserverRemoteInterface;
import Server.serverCommunication.CommsTypes.DBCommuncation;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRemote extends UnicastRemoteObject implements ServerRemoteInterface {
    public static final String SERVICE_NAME = "ServerRemote";
    private DBCommuncation dbCommuncation;

    protected ServerRemote(/*DBCommuncation dbCommuncation*/) throws RemoteException {
        //this.dbCommuncation = dbCommuncation;
    }

    @Override
    public void makeRegister(String name, String username, String password, String photo_path) throws RemoteException {
        //dbCommuncation.userRegister(name, username, password, photo_path);
        System.out.println("TBD - Make Register");
    }

    @Override
    public void sendMsgAll(String msg) throws RemoteException {
        System.out.println("TBD - Send all a message");
    }

    @Override
    public void addObserver(ObserverRemoteInterface observer) throws RemoteException {
        System.out.println("TBD - Add Observer");
    }

    @Override
    public void removeObserver(ObserverRemoteInterface observer) throws RemoteException {
        System.out.println("TBD - Remove Observer");
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
