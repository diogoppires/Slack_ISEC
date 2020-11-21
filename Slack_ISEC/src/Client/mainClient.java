package Client;

import Client.Interface.Text.UIText;
import Client.clientCommunication.ClientCommunication;


public class mainClient {
    public static void main(String[] args) throws InterruptedException {
//        System.out.println("------------------------------------------------");
//       // ClientCommunication cC = new ClientCommunication(Integer.parseInt(args[0]), args[1]);
//        if(cC.askForConnection()){
//            System.out.println("[CLIENT]: The connection was made with success.");
//        }
//        else{
//            System.out.println("[CLIENT]: The connection failed.");
//        }
          ClientCommunication cC = new ClientCommunication(9999, "localhost");
          UIText ui = new UIText(cC);
          ui.run();
    }
}
