package Client;

import Client.Interface.Text.UIText;
import Client.clientCommunication.ClientCommunication;


public class mainClient {
    public static void main(String[] args) throws InterruptedException {
        ClientCommunication cC;
        if(args.length == 0)
            cC = new ClientCommunication(9999, "localhost");
        else
            cC = new ClientCommunication(Integer.parseInt(args[0]), args[1]);
        
        UIText ui = new UIText(cC);
        ui.run();
    }
}