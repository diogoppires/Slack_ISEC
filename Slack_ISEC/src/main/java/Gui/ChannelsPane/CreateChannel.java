package Gui.ChannelsPane;


import Gui.Constantes;
import Gui.Observable;
import Gui.UserInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class CreateChannel extends BorderPane {
    Observable obs;
    UserInterface ui;
    Text textName;
    Text textPassword;
    Text textDescription;
    TextField getName;
    TextArea getDescription;
    PasswordField getPassword;
    Button btnSendChannel;
    Button btnCancel;

    public CreateChannel(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;


        createLayout();
        registeListeners();
    }
    private void createLayout() {

        textName = new Text("Channel Name:");
        textPassword = new Text("Channel Password");
        textDescription = new Text("Channel Description:");
        getName = new TextField();
        getPassword = new PasswordField();
        getDescription = new TextArea();
        btnSendChannel = new Button("Create");
        btnCancel = new Button("Cancel");

        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnSendChannel,btnCancel);

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(textName,getName,textPassword,getPassword,textDescription,getDescription,btnBox);
        mainBox.setMaxWidth(Constantes.WIN_WIDTH * 0.50);
        setCenter(mainBox);

    }

    private void registeListeners() {
        btnSendChannel.setOnAction((e) -> {
            obs.sendCreateChannel(
                    getName.getCharacters().toString()+"+" +
                            getDescription.getText() +"+" +
                            getPassword.getText());
            ui.setLast();
        });
        btnCancel.setOnAction((e) -> {
            ui.setLast();
        });
    }
}