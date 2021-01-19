package controllers;


import ClientRMI.ClientRemote;
import Server.serverCommunication.Data.ClientData;
import Server.serverCommunication.ServerCommunication;
import ServerRMI.ServerRemoteInterface;
import org.springframework.web.bind.annotation.*;
import web.DataBase;
import web.RMIService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

@RestController
@RequestMapping("messages")
public class Messages {

    @PostMapping("/get")
    public String messages (
        @RequestParam(value="number") int number){
        var text = DataBase.searchMessages(number);
        System.out.println(text);
        return text;

    }
    @PostMapping("/send")
    public String sendmessage (
            @RequestParam(value="message") String text){
        try {
            RMIService.sendMsg(text);
        } catch (RemoteException e) {
            return ("[API]Error sending message!");
        }
        return "[API]Message send with success";
    }
}
