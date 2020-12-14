package Gui;


import Gui.Integration.PropsID;
import com.sun.javafx.text.TextLine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.awt.font.TextLayout;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Chat extends BorderPane {
    Observable obs;
    UserInterface ui;
    Info info;
    Text chat;
    Button btnSendMessage;
    Button btnSendFile;
    Button btnListAll;
    Button btnListSerch;
    Button btnListLastMsg;
    Button btnShowStats;
    Button btnDownload;
    TextField getText;
    BorderPane notificationBar;
    TextField getUser;
    TextField getNumber;
    TextField getDownload;
    Button b;
    ArrayList<String> list;
    ArrayList<Button> arrayListCanais;
    VBox canais;
    ScrollPane canaisScrollBar;
    Text msgView;
    BorderPane chatPane;
    BorderPane textPane;
    ScrollPane textScroll;
    VBox text;



    public Chat(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;
        info = new Info(obs);


        btnSendMessage = new Button("Send Message");
        btnSendFile = new Button("Send File");
        msgView = new Text("\n\nLorem Ipsum is simply dummy text \n");
        //msgView.setEditable(false);

        chatPane = new BorderPane();
        chatPane.setMaxSize(Constantes.WIN_WIDTH*0.9,Constantes.WIN_HEIGHT);
        chatPane.setMinSize(Constantes.WIN_WIDTH*0.9,Constantes.WIN_HEIGHT*0.90);

        notificationBar = new BorderPane();
        notificationBar.setMinHeight(Constantes.WIN_HEIGHT * 0.1);
        notificationBar.setCenter(new Text("Notifications"));
        chatPane.setTop(notificationBar);

        // BOXButoes sendMessage + File
        HBox btnBox = new HBox();
        getDownload = new TextField();
        btnDownload = new Button("Download");
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setMaxHeight(Constantes.WIN_HEIGHT * 0.05);
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(btnSendMessage, btnSendFile, new Text("File Code:"),getDownload,btnDownload);


        HBox btnListBox = new HBox();
        btnListLastMsg = new Button ("List last Messages");
        btnListAll = new Button("List Users and Channels");
        btnListSerch = new Button("Search Users and Channels");
        btnShowStats = new Button("Show Stats");
        btnListBox.setAlignment(Pos.CENTER);
        btnListBox.setMaxHeight(Constantes.WIN_HEIGHT * 0.05);
        btnListBox.setSpacing(10);
        btnListBox.getChildren().addAll(btnListLastMsg,btnListAll,btnListSerch,btnShowStats);

        VBox btnsBox = new VBox();
        btnsBox.setAlignment(Pos.CENTER);
        btnsBox.setSpacing(1);
        getUser = new TextField();
        getUser.setMaxWidth(Constantes.WIN_WIDTH*0.3);
        getNumber = new TextField();
        getNumber.setMaxWidth(Constantes.WIN_WIDTH*0.3);
        HBox getData = new HBox(new Text("Search:    "), getUser, new Text("    Number Msg:    "),getNumber);
        getData.setAlignment(Pos.CENTER);
        btnsBox.setSpacing(10);
        btnsBox.getChildren().addAll(btnBox,getData,btnListBox);


        // chat VIEW
        textPane = new BorderPane();
        textPane.setMinSize(Constantes.WIN_WIDTH*0.8, Constantes.WIN_HEIGHT * 10);
        textPane.setPadding(new Insets(20,20,20,20));
        textPane.getChildren().add(msgView);

        textScroll = new ScrollPane();
        textScroll.setMaxWidth(Constantes.WIN_WIDTH*0.8);
        textScroll.setMinSize(Constantes.WIN_WIDTH*0.8, Constantes.WIN_HEIGHT * 0.6);
        textScroll.setContent(textPane);

        getText = new TextField();
        getText.setMaxSize(Constantes.WIN_WIDTH*0.8, Constantes.WIN_HEIGHT * 0.6);

        text = new VBox();
        text.getChildren().addAll(textScroll,getText);
        text.setAlignment(Pos.CENTER);
        text.setSpacing(10);
        chatPane.setCenter(text);
        chatPane.setBottom(btnsBox);

        canais = new VBox();
        canais.setSpacing(5);

        canaisScrollBar = new ScrollPane();
        canaisScrollBar.setContent(canais);
        canaisScrollBar.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        setLeft(canaisScrollBar);
        setRight(chatPane);

        registeListeners();

        list = new ArrayList<>();
        arrayListCanais = new ArrayList<>();
        obs.getUsersAndChannels();
        toArrayList(obs.getLog());
        getButtonsArray(list);
        setCanais(arrayListCanais);
        canaisScrollBar.setContent(canais);
        registaListenersCanais();
    }

    private void registeListeners() {
        btnSendFile.setOnAction((e) -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(getScene().getWindow());

            if(file == null)
                return;
            try{
                obs.sendFile( file.getParent() ,file.getName(), "destination");
            } catch(Exception ex){
            }
        });
        btnSendMessage.setOnAction((e) -> {
            obs.sendConversation(getText.getText());
        });
        btnListAll.setOnAction((e) -> {
            obs.sendListAll();
        });
        btnListLastMsg.setOnAction((e) -> {
            obs.sendListLastMsg(getUser.getText(),getNumber.getText());
        });
        btnListSerch.setOnAction((e) -> {
            obs.sendList(getUser.getText());
        });
        btnShowStats.setOnAction((e) -> {
            obs.sendShowStats();
        });
        btnDownload.setOnAction((e) -> {
            obs.receiveFile(getDownload.getText());
        });
        obs.registaPropertyChangeListener(PropsID.PROP_NOTIFICATION , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                notificationBar.setCenter(new Text(obs.getLog()));
            }
        });

        obs.registaPropertyChangeListener(PropsID.PROP_CHANNELANDUSERS, new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
               toArrayList(obs.getLog());
               getButtonsArray(list);
               setCanais(arrayListCanais);
               canaisScrollBar.setContent(canais);
               registaListenersCanais();
            }
        });
        obs.registaPropertyChangeListener(PropsID.PROP_MSG , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {

                StringBuilder sb = new StringBuilder(msgView.getText());
                sb.append(obs.getLogM());
                sb.append("\n");
                //String s = "\n\n\n" + obs.getLogM();
                System.out.println("MSGVIEW: " + sb.toString());

                msgView.setText(sb.toString());

                System.out.println("FIRE NO TEXTAREA");

            }
        });

    }
    public void toArrayList(String s){
        StringTokenizer tokenizer = new StringTokenizer(s,"-");
        list.clear();
        while(tokenizer.hasMoreTokens()){
            list.add(tokenizer.nextToken());
        }
    }

    public void getButtonsArray(ArrayList<String> arraylist){
        arrayListCanais.clear();
        for (String item:  arraylist){
            arrayListCanais.add(new Button(item));
        }
    }

    public void setCanais(ArrayList<Button> arrayButtons) {
        for (Button item: arrayButtons){
            item.setMinWidth(Constantes.WIN_WIDTH*0.1);
            item.setMinHeight(Constantes.WIN_HEIGHT*0.1);
            item.setMaxWidth(Constantes.WIN_WIDTH*0.1);
            item.setMaxHeight(Constantes.WIN_HEIGHT*0.1);
            canais.getChildren().add(item);
        }
    }
    public void registaListenersCanais() {
        for(Button item: arrayListCanais){
            item.setOnAction((e) ->{
                StringTokenizer tok = new StringTokenizer(item.getText(), "\n");
                obs.setDestination(tok.nextToken());
            });
        }
    }


}
