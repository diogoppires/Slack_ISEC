package Client;

import Client.clientCommunication.ClientCommunication;


public class mainClient {
    public static void main(String[] args) {
        System.out.println("------------------------------------------------");
        ClientCommunication cC = new ClientCommunication(9999, "localhost"); 
       // ClientCommunication cC = new ClientCommunication(Integer.parseInt(args[0]), args[1]);
        if(cC.askForConnection()){
            System.out.println("[CLIENT]: The connection was made with success.");
        }
        else{
            System.out.println("[CLIENT]: The connection failed.");
        }
    }
}
