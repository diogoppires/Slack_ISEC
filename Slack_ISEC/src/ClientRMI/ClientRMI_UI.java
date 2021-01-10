package ClientRMI;

import ObserverRMI.ObserverRemote;

import java.rmi.RemoteException;

public class ClientRMI_UI {
    private ClientRemote cR;
    private ObserverRemote oR;

    public ClientRMI_UI(){
        try {
            cR = new ClientRemote();
            oR = new ObserverRemote();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //oR.run();
        //cR.run();
    }

    public static void main(String[] args) {
        ClientRMI_UI uiRmi = new ClientRMI_UI();
        uiRmi.run();
    }
}
