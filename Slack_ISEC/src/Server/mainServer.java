package Server;

import Server.serverCommunication.ServerCommunication;
import java.util.Scanner;

public class mainServer {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String getVal = "";
        ServerCommunication sC = new ServerCommunication(9998, 9321, "localhost");
        sC.initializeComms();
        sC.startThreads();
        while(!getVal.equals("exit")){
            getVal = sc.next();
        }
        sC.finishThreads();
        System.out.println("Server Ended!");
    }
}
