package Server;

import Server.serverCommunication.ServerCommunication;
import java.util.Scanner;

public class mainServer {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String getVal = "";
        ServerCommunication sC;
        //MAC
        System.setProperty("java.net.preferIPv4Stack", "true");
        if(args.length == 0)
            sC = new ServerCommunication(9999, 9000, "localhost");
        else{
            sC = new ServerCommunication(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
        }
        sC.initializeComms();
        sC.startThreads();
        while(!getVal.equals("exit")){
            getVal = sc.next();
        }
        sC.finishThreads();
        System.out.println("Server Ended!");
    }
}
