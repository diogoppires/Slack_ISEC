package Gui.ChannelsPane;

import Gui.Constantes;
import Gui.Observable;
import Gui.UserInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class JoinChannel extends BorderPane {
    Observable obs;
    UserInterface ui;
    Text textName;
    Text textPassword;
    TextField getName;
    PasswordField getPassword;
    Button btnSendDelete;
    Button btnCancel;

    public JoinChannel(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;

        createLayout();
        registeListeners();
    }
    private void createLayout() {

        textName = new Text("Channel Name:");
        textPassword = new Text("Channel Password");
        getName = new TextField();
        btnSendDelete = new Button("Join");
        btnCancel = new Button("Cancel");
        getName = new TextField();
        getPassword = new PasswordField();

        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnSendDelete,btnCancel);

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(textName,getName,textPassword,getPassword,btnBox);
        mainBox.setMaxWidth(Constantes.WIN_WIDTH * 0.50);
        setCenter(mainBox);
    }

    private void registeListeners() {

        btnSendDelete.setOnAction((e) -> {
            obs.sendJoinChannel(getName.getCharacters().toString()+ "+" +
                    getPassword.getText());
            ui.setLast();
        });

        btnCancel.setOnAction((e) -> {
            ui.setLast();
        });
    }



}
