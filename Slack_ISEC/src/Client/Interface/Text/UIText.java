/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Interface.Text;

import Client.clientCommunication.ClientCommunication;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rui
 */
public class UIText {

    private final static String DEFAULT = "[CLIENT]: Invalid command.";

    private final ClientCommunication cC;
    private static boolean validation;
    private boolean end;
    private String username;

    public static void setValidation(boolean b) {
        validation = b;
        System.out.println("validation: " + validation);
    }

    public UIText(ClientCommunication cC) {
        this.cC = cC;
        validation = false;
        end = false;
        if (!cC.askForConnection()) {
            System.out.println("O Servidor indicado não está Disponivel.");
        }
    }

    private String getOptions() {
        StringBuilder sb = new StringBuilder();
        if (!validation) {
            sb.append("\t\t>> SLACK ISEC <<\n");
            sb.append("________________________________________________________\n");
            sb.append("[1] - Register.\n");
            sb.append("[2] - Log In.\n");
            sb.append("[3] - Exit.\n");
            sb.append(">> ");
        } else {
            sb.append("\t\t>> SLACK ISEC <<\n");
            sb.append("________________________________________________________\n");
            sb.append("[1] - Create a channel               [7] - List Search\n");
            sb.append("[2] - Edit a channel.                [8] - List last messages.\n");
            sb.append("[3] - Delete a channel.              [9] - Show statics from all channels.\n");
            sb.append("[4] - Talk with other user.          [10] - Join a channel.\n");
            sb.append("[5] - Share files.                   [11] - Log Out.\n");
            sb.append("[6] - List all channels and users    [12] - Exit.\n");
            sb.append(">> ");
        }
        return sb.toString();
    }

    private Options chooseOpt() {
        System.out.print(getOptions());
        if (!validation) {
            int opt = readInt();
            switch (opt) {
                case 1:
                    return Options.register;
                case 2:
                    return Options.authentication;
                case 3:
                    return Options.exit;
            }
        } else {
            int opt = readInt();
            switch (opt) {
                case 1:
                    return Options.createChannel;
                case 2:
                    return Options.editChannel;
                case 3:
                    return Options.deleteChannel;
                case 4:
                    return Options.talkWithUser;
                case 5:
                    return Options.shareFiles;
                case 6:
                    return Options.listAll;
                case 7:
                    return Options.list;
                case 8:
                    return Options.listLastMsg;
                case 9:
                    return Options.showStats;
                case 10:
                    return Options.joinChannel;
                case 11:
                    return Options.logOut;
                case 12:
                    return Options.exit;
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

    private void uiRegister() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        System.out.println("Please Insert Name: ");
        sb.append(sc.next()).append("+");
        System.out.println("Please Insert Username: ");
        sb.append(sc.next()).append("+");
        System.out.println("Please Insert Password: ");
        sb.append(sc.next()).append("+");
        System.out.println("Please Insert PhotoPath: ");
        String fileName = sc.next();
        sb.append("\\").append(fileName); //sc.next();

        cC.sendRegister(sb.toString(), "C:\\Temp" , fileName);
       /* if (!cC.sendMessage(s)) {
        sb.append(sc.next());
            System.out.println("Não existem Servidores Disponiveis");
        }*/
        //cC.awaitResponse();

        //.out.println("[CLIENT]: NOT IMPLEMENTED");
        //cC.sendMessage(sc.next());
    }

    private void uiAuthentication() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        System.out.println("Please Insert Username: ");
        username = sc.next();
        sb.append(username).append("+");
        System.out.println("Please Insert Password: ");
        sb.append(sc.next());

        cC.sendLogin(sb.toString());
    }

    private void uiCreateChannel() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            System.out.println("Please Insert Channel Name: ");
            sb.append(sc.nextLine()).append("+");
            System.out.println("Please Insert Description: ");
            sb.append(sc.nextLine()).append("+");
            System.out.println("Please Insert Channel Password: ");
            sb.append(sc.next());

            cC.sendCreateChannel(sb.toString());
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiEditChannel() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            System.out.println("Please Insert Channel Name to Edit: ");
            sb.append(sc.next()).append("+");
            System.out.println("Please Insert New Channel Name: ");
            sb.append(sc.next()).append("+");
            System.out.println("Please Insert Description: ");
            sb.append(sc.next()).append("+");
            System.out.println("Please Insert New Channel Password: ");
            sb.append(sc.next());

            cC.sendEditChannel(sb.toString());
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiDeleteChannel() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please Insert Channel Name: ");
            String s = sc.next();

            cC.sendDeleteChannel(s);
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiJoinChannel(){
        if (validation) {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            System.out.print("Insert Channel Name: ");
            sb.append(sc.next()).append("+");
            System.out.print("Insert Password:");
            sb.append(sc.next());

            cC.sendJoinChannel(sb.toString());
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiConversation() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            System.out.println("Insert the username: ");
            sb.append(sc.nextLine()).append("+");
            System.out.println("Write your message: ");
            sb.append(sc.nextLine());

            cC.sendConversation(sb.toString());
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiShareFiles() {

        System.out.println("[1] - Enviar Ficheiro");
        System.out.println("[2] - Receber Ficheiro");
        System.out.println(">>");
        Scanner sc = new Scanner(System.in);
        switch (sc.nextInt()){
            case 1: {
                System.out.println("Indique a Diretoria.");
                String localDirectory = "C:\\" + sc.next();
                System.out.println("Indique o Caminho do ficheiro.");
                String fileName = sc.next();
                System.out.println("Indique o destino.");
                String destination = sc.next();
                cC.sendFile(localDirectory, fileName, destination);
                break;
            }
            case 2: {
                System.out.println("Indique o Código do Ficheiro.");
                int fileCode = sc.nextInt();
                cC.receiveFile(fileCode);
                break;
            }
        }
    }

    private void uiListAll() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            cC.sendListAll();
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiList() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please Insert Text to Search: ");
            String s = sc.next();
            cC.sendList(s);
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiListLastMsg() {
        if (validation) {
            Scanner sc = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            System.out.print("Insert name1 : ");
            sb.append(sc.next()).append("+");
            System.out.print("Insert name2 : ");
            sb.append(sc.next()).append("+");
            System.out.print("Insert the number of messages to display: ");
            sb.append(sc.next());

            cC.sendListLastMsg(sb.toString());
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiShowStats() {
        if (validation) {
            cC.sendShowStats();
        } else {
            System.out.println("Please Login!");
        }
    }

    private void uiLogOut() {
        System.out.println("[CLIENT]: NOT IMPLEMENTED");
    }

    public void run() {
        Options opt;
        while (!end) {
            opt = chooseOpt();
            switch (opt) {
                case register:
                    uiRegister();
                    break;
                case authentication:
                    uiAuthentication();
                    break;
                case createChannel:
                    uiCreateChannel();
                    break;
                case editChannel:
                    uiEditChannel();
                    break;
                case deleteChannel:
                    uiDeleteChannel();
                    break;
                case joinChannel:
                    uiJoinChannel();
                    break;
                case talkWithUser:
                    uiConversation();
                    break;
                case shareFiles:
                    uiShareFiles();
                    break;
                case list:
                    uiList();
                    break;
                case listAll:
                    uiListAll();
                    break;
                case listLastMsg:
                    uiListLastMsg();
                    break;
                case showStats:
                    uiShowStats();
                    break;
                case logOut:
                    uiLogOut();
                    break;
                case invalid:
                    System.out.println(DEFAULT);
                    break;
                case exit:
                    end = true;
                    break;
            }
        }
    }

    public enum Options {
        register,
        authentication,
        createChannel,
        editChannel,
        deleteChannel,
        joinChannel,
        talkWithUser,
        shareFiles,
        list,
        listAll,
        listLastMsg,
        showStats,
        logOut,
        exit,
        invalid
    }
}
