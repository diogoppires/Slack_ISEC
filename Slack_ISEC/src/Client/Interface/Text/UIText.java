/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Interface.Text;

import Client.clientCommunication.ClientCommunication;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Rui
 */



public class UIText {
    private final static String DEFAULT = "[CLIENT]: Invalid command.";
    private final ClientCommunication cC;
    private boolean validation;
    private boolean end;
    
    
    public UIText(ClientCommunication cC){
        this.cC = cC;
        validation = false;
        end = false;
        cC.askForConnection();
    }
    
    private String getOptions(Options actualOpt){
        StringBuilder sb = new StringBuilder();
        if(actualOpt == Options.authentication || actualOpt == Options.register){
            sb.append("\t\t>> SLACK ISEC <<\n");
            sb.append("________________________________________________________\n");
            sb.append("[1] - Register.\n");
            sb.append("[2] - Log In.\n");
            sb.append("[3] - Exit.\n");
            sb.append(">> ");
        }
        else{
            sb.append("\t\t>> SLACK ISEC <<\n");
            sb.append("________________________________________________________\n");
            sb.append("[1] - Create a channel           [6] - List all channels and users\n");
            sb.append("[2] - Edit a channel.            [7] - List last messages.\n");
            sb.append("[3] - Delete a channel.          [8] - Show statics from all channels.\n");
            sb.append("[4] - Talk with other user.      [9] - Log Out.\n");
            sb.append("[5] - Share files.               [10] - Exit.\n");
            sb.append(">> ");
        }
        return sb.toString();
    }
    private Options chooseOpt(Options actualOpt){
        System.out.print(getOptions(actualOpt));
        if(actualOpt == Options.register || actualOpt == Options.authentication){
            int opt = readInt();
            switch(opt){
                case 1: return Options.register;
                case 2: return Options.authentication;
                case 3: return Options.exit;
            }
        }else{
            int opt = readInt();
            switch(opt){
                case 1: return Options.createChannel;
                case 2: return Options.editChannel;
                case 3: return Options.deleteChannel;
                case 4: return Options.talkWithUser;
                case 5: return Options.shareFiles;
                case 6: return Options.listAll;
                case 7: return Options.listLastMsg;
                case 8: return Options.showStats;
                case 9: return Options.logOut;
                case 10: return Options.exit;
            }
        }
        return Options.invalid;
    }
    private int readInt() {
        Scanner sc = new Scanner(System.in);
        try {
            int num = sc.nextInt();
            return num;
        } catch (InputMismatchException ex) {
            System.out.println("[ERROR] You need to insert a number!");
            return -1;
        }
    }
    private void uiRegister(){
        Scanner sc = new Scanner(System.in);
        String s = new String("");
        s = "1+";
        System.out.println("Please Insert Username: ");
        s += sc.next() + "+";
        System.out.println("Please Insert Password: ");
        s += sc.next();
        cC.sendMessage(s);
        //.out.println("[CLIENT]: NOT IMPLEMENTED");
        //cC.sendMessage(sc.next());
    }
    private void uiAuthentication(){
        
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiCreateChannel(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiEditChannel(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiDeleteChannel(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiConversation(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiShareFiles(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiListAll(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiListLastMsg(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiShowStats(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    private void uiLogOut(){
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }
    
    public void run(){
        Options opt = Options.register;
        while(!end){
            opt = chooseOpt(opt);
            switch(opt){
                case register: uiRegister();
                    break;
                case authentication: uiAuthentication();
                    break;
                case createChannel: uiCreateChannel();
                    break;
                case editChannel: uiEditChannel();
                    break;
                case deleteChannel: uiDeleteChannel();
                    break;
                case talkWithUser: uiConversation();
                    break;
                case shareFiles: uiShareFiles();
                    break;
                case listAll: uiListAll();
                    break;
                case listLastMsg: uiListLastMsg();
                    break;
                case showStats: uiShowStats();
                    break;
                case logOut: uiLogOut();
                    break;
                case invalid: System.out.println(DEFAULT);
                    break;
                case exit: end = true;
                    break;
            }
        }
    }
    public enum Options{
        register,
        authentication,
        createChannel,
        editChannel,
        deleteChannel,
        talkWithUser,
        shareFiles,
        listAll,
        listLastMsg,
        showStats,
        logOut,
        exit,
        invalid
    }
}
