package Gui;


import Gui.ChannelsPane.CreateChannel;
import Gui.ChannelsPane.DeleteChannel;
import Gui.ChannelsPane.EditChannel;
import Gui.Integration.PropsID;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

abstract class PropertyChangeListenerJFX implements PropertyChangeListener{


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(()-> {
            onChange(evt);
        });
    }

    protected abstract void onChange(PropertyChangeEvent evt);
}
public class UserInterface extends BorderPane {

    private Observable Obs;
    private Login loginPane;
    private Register registerPane;
    private Chat chatPane;
    private Init initPane;
    private CreateChannel createChannelPane;
    private EditChannel editChannelPane;
    private DeleteChannel deleteChannelPane;
    private Pane last;
    private boolean validation = false;
    private UserInterface ui = this;
    Info info;

    public UserInterface(Observable obs) {

        Obs = obs;
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        initPane = new Init(Obs, this);
        info = new Info(obs);
        criaLayout();

        setMinHeight(Constantes.WIN_HEIGHT);
        setMinWidth(Constantes.WIN_WIDTH);


    }

    private void criaLayout() {
        setTop(criaMenu());
        setCenter(initPane);
        last = initPane;
    }



    MenuBar criaMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile =  new Menu("_File");
        MenuItem openFile = new MenuItem("_Open");
        MenuItem saveFile = new MenuItem("_Save");
        MenuItem loadFile = new MenuItem("_Load");
        MenuItem itemExit = new MenuItem("_Exit");
        Menu menuChannel = new Menu("Channel");
        MenuItem createChannel = new MenuItem("Create");
        MenuItem editChannel = new MenuItem("Edit");
        MenuItem deleteChannel = new MenuItem("Delete");

        menuFile.getItems().addAll(openFile, new SeparatorMenuItem(),saveFile, loadFile, new SeparatorMenuItem(),itemExit);
        menuChannel.getItems().addAll(createChannel,editChannel,deleteChannel);
        menuBar.getMenus().addAll(menuFile, menuChannel);

        itemExit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        itemExit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        createChannel.setOnAction(e -> {
            if(validation == true){
                setCenter(new CreateChannel(Obs, this));
            }

        });
        editChannel.setOnAction(e -> {
            if(validation == true){
                setCenter(new EditChannel(Obs, this));
            }
        });
        deleteChannel.setOnAction(e -> {
            if(validation == true){
                setCenter(new DeleteChannel(Obs, this));
            }
        });
        return menuBar;
    }

    public void setLogin() {
        loginPane = new Login(Obs, this);
        loginPane.setVisible(true);
        last = loginPane;
        setCenter(loginPane);
        registaListeners();

    }

    public void setRegister() {
        registerPane = new Register(Obs, this);
        registerPane.setVisible(true);
        last=registerPane;
        setCenter(registerPane);
    }

    public void setLast() {
        setCenter(last);

    }

    private void registaListeners() {
        Obs.registaPropertyChangeListener(PropsID.PROP_LOGIN , new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                validation = true;
                chatPane = new Chat(Obs, ui);
                ui.setCenter(chatPane);
                ui.set(chatPane);
            }
        });
        Obs.registaPropertyChangeListener(PropsID.PROP_TEXTINFO, new PropertyChangeListenerJFX() {
            @Override
            protected void onChange(PropertyChangeEvent evt) {
                if (info.getOwner() == null) {
                    info.initOwner(getScene().getWindow());
                    info.setX(getScene().getWindow().getX());
                    info.setY(getScene().getWindow().getY());
                    info.initModality(Modality.NONE);
                    info.setMsgView((Obs.getLog()));
                    info.show();
                } else
                    info.setMsgView((Obs.getLog()));
                    info.show();

            }
        });
    }
    private void set(Pane pane) {
        last=pane;
    }


}
