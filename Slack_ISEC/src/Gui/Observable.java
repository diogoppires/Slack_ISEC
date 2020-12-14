package Gui;

import Client.clientCommunication.ClientCommunication;
import Gui.Integration.PropsID;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.StringTokenizer;

public class Observable {

    ClientCommunication cC;
    private PropertyChangeSupport props = null;
    String username;
    String destination;



    public Observable() {

    }

    public void registaPropertyChangeListener(PropsID prop,PropertyChangeListener listener){
        props.addPropertyChangeListener(prop.toString(),listener);
    }

    public void sendLogin(String username, String password) {
        System.out.println("Call Authentication");
        System.out.println("Username: " + username + " password: " + password);
        this.username = username;
        cC.sendLogin(username + "+" + password);
    }
    public void sendRegister(String s, String photoPath, String fileName) {
        System.out.println("Call Authentication");
        System.out.println("S: " + s +  " photo: " + photoPath + "namefile: " + fileName);
        cC.sendRegister(s, photoPath,fileName);
    }
    public void sendCreateChannel(String s){
        System.out.println("CreateChannel");
        System.out.println(s);
        cC.sendCreateChannel(s);

    }
    public void sendEditChannel(String s){
        System.out.println("EditChannel");
        System.out.println(s);

    }
    public void deleteEditChannel(String s){
        System.out.println("DeleteChannel");
        System.out.println(s);
    }
    public void sendJoinChannel(String s){
        System.out.println("JoinChannel");
        System.out.println(s);

    }

    public void sendFile(String path, String name, String destination) {
        System.out.println("PATH "  + path+" NAME " + name + " DEST " + destination);
    }

    public void sendConversation(String s) {
        System.out.println("SendMessage");
        System.out.println(s);
        cC.sendConversation(destination + "+" + s);
        System.out.println("SENT: " + destination + "+" + s);

    }

    public void startCommunication(String ip, int port) {

        System.out.println("Start Communication");
        System.out.println("IP " + ip +" PORT " + port);
        cC = new ClientCommunication(9999, "localhost");
        if (!cC.askForConnection()) {
            System.out.println("O Servidor indicado não está Disponivel.");
        }
        props = new PropertyChangeSupport(cC);
        registaListeners();
    }
    public void sendListAll() {
        cC.sendListAll();
    }

    public void sendList(String text) {
        cC.sendList(text);
    }

    public void sendShowStats() {
        cC.sendShowStats();
    }

    public String getLog(){
        return cC.getLog();
    }
    public String getLogM(){
        return cC.getLogM();
    }
    public void fire (PropsID prop){
        props.firePropertyChange(prop.toString(),null,null);
    }

    private void registaListeners() {
        cC.registaPropertyChangeListener(PropsID.PROP_LOGIN , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                fire(PropsID.PROP_LOGIN);
            }
        });
        cC.registaPropertyChangeListener(PropsID.PROP_NOTIFICATION , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                fire(PropsID.PROP_NOTIFICATION);
            }
        });
        cC.registaPropertyChangeListener(PropsID.PROP_TEXTINFO , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                fire(PropsID.PROP_TEXTINFO);
            }
        });
        cC.registaPropertyChangeListener(PropsID.PROP_CHANNELANDUSERS , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                fire(PropsID.PROP_CHANNELANDUSERS);
            }
        });
        cC.registaPropertyChangeListener(PropsID.PROP_MSG , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                fire(PropsID.PROP_MSG);
                System.out.println("fireOBS");
            }
        });
    }

    public void sendListLastMsg(String text, String text1) {
        cC.sendList(username + text + "+" + text1);
    }

    public void receiveFile(String text) {
        cC.receiveFile(Integer.parseInt(text));
    }

    public void getUsersAndChannels() {
        cC.getUserAndChannels();
    }

    public void setDestination(String destination) {
        StringTokenizer tok = new StringTokenizer(destination, "\n");
        this.destination = tok.nextToken();
    }

}