package ClientRMI;

import ServerRMI.ServerRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;



public class ClientRemote extends UnicastRemoteObject implements ClientRemoteInterface {
    //StringBuilder
    StringBuilder sb = new StringBuilder();

    //Services
    String[] allServices;
    String formattedList;
    String currentService;
    int indexCurrentService;

    //Remotes
    Remote remObj;

    //Interfaces
    ServerRemoteInterface remoteInterface;

    protected ClientRemote() throws RemoteException {
    }

    @Override
    public void notifyNewOperationConcluded(String description) throws RemoteException {
        System.out.println(description);
    }

    public String printCommands(){
        StringBuilder sb = new StringBuilder();
        sb.append("[CURRENT SERVICE]: ").append(currentService).append("\n");
        sb.append("Choose an option:\n");
        sb.append("1 - Register User                2 - Send Message\n");
        sb.append("3 - Notify Users                 4 - Notify Message\n");
        sb.append("5 - Next Service(Automatic)      6 - Previous Service(Automatic)\n");
        sb.append("7 - Change Service(manually)     8 - Check list of all services\n");
        sb.append("9 - Exit");
        return sb.toString();
    }

    public void nextService() throws RemoteException, NotBoundException, MalformedURLException {
        if(indexCurrentService < allServices.length - 1){
            indexCurrentService++;
            currentService = allServices[indexCurrentService];
            remoteInterface = (ServerRemoteInterface) Naming.lookup(currentService);
        }
        else{
            System.out.println("[ERROR]: No more services available");
        }
    }

    public void previousService() throws RemoteException, NotBoundException, MalformedURLException {
        if(indexCurrentService > 0){
            indexCurrentService--;
            currentService = allServices[indexCurrentService];
            remoteInterface = (ServerRemoteInterface) Naming.lookup(currentService);
        }
        else{
            System.out.println("[ERROR]: No more services available");
        }
    }

    public void changeServiceManually() throws RemoteException, NotBoundException, MalformedURLException {
        Scanner sc = new Scanner(System.in);
        int ans = -1;
        while(ans < 0 || ans > allServices.length){
            System.out.println(formattedList);
            System.out.println("Insert the position you want to move:");
            ans = sc.nextInt();
        }
        indexCurrentService = ans;
        currentService = allServices[indexCurrentService];
        remoteInterface = (ServerRemoteInterface) Naming.lookup(currentService);
    }


    public void run(){
        try{
            Registry r = LocateRegistry.getRegistry(1099);
            allServices = r.list();     //Get list of all services

            //Format the list of services
            sb.append("List of Services:\n");
            for(int i = 0; i < allServices.length; i++){
                sb.append(i).append(" ~~>").append(allServices[i]).append("\n");
            }
            formattedList = sb.toString();

            //Get the first service. This first service to be chosen will be the first one of the list.
            indexCurrentService = 0;
            currentService = allServices[indexCurrentService];
            remoteInterface = (ServerRemoteInterface) Naming.lookup(currentService);

            boolean over = false;
            Scanner sc = new Scanner(System.in);
            //Get inside a while-loop, where the user can execute commands.
            while (!over){
                System.out.println(printCommands());
                switch(sc.nextInt()){
                    case 1 -> remoteInterface.makeRegister("Ola", "xau", "tau", "pau");
                    case 2 -> remoteInterface.sendMsgAll("[REMOTE CLIENT] TEST");
                    case 3 -> remoteInterface.addObserverUsers(this);
                    case 4 -> remoteInterface.addObserversMessages(this);
                    case 5 -> nextService();
                    case 6 -> previousService();
                    case 7 -> changeServiceManually();
                    case 8 -> System.out.println(formattedList);
                    case 9 -> over = true;
                }
            }

            UnicastRemoteObject.unexportObject(this, true);
        } catch (RemoteException e) {
            System.out.println("Remote Error - " + e);
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ClientRemote cR = new ClientRemote();
            cR.run();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
