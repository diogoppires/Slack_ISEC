package Client;

import Client.clientCommunication.ClientCommunication;


public class mainClient {
    public static void main(String[] args) {
        System.out.println("------------------------------------------------");
        ClientCommunication cC = new ClientCommunication(9999, "localhost");
        if(cC.askForConnection()){
            System.out.println("[CLIENT]: The connection was made with success.");
        }
        else{
            System.out.println("[CLIENT]: The connection failed.");
        }
    }
}
