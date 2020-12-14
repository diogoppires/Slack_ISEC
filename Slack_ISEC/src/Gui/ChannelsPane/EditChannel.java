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


public class EditChannel extends BorderPane {
    Observable obs;
    UserInterface ui;
    Text textActualName;
    Text textName;
    Text textPassword;
    Text textDescription;
    TextField getActualName;
    TextField getName;
    TextArea getDescription;
    PasswordField getPassword;
    Button btnEditChannel;
    Button btnCancel;

    public EditChannel(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;


        createLayout();
        registeListeners();
    }
    private void createLayout() {

        textActualName = new Text("Channel Name to Edit:");
        textName = new Text("New Channel Name:");
        textPassword = new Text("New Channel Password");
        textDescription = new Text("New Channel Description:");
        getActualName = new TextField();
        getName = new TextField();
        getPassword = new PasswordField();
        getDescription = new TextArea();
        btnEditChannel = new Button("Edit");
        btnCancel = new Button("Cancel");

        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnEditChannel,btnCancel);

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(textActualName,getActualName,textName,getName,textPassword,getPassword,textDescription,getDescription,btnBox);
        mainBox.setMaxWidth(Constantes.WIN_WIDTH * 0.50);
        setCenter(mainBox);

    }

    private void registeListeners() {

        btnEditChannel.setOnAction((e) -> {
            obs.sendEditChannel(
                    getActualName.getCharacters().toString() + "+" +
                            getName.getCharacters().toString()+ "+" +
                            getDescription.getText() + "+" +
                            getPassword.getText()
            );
            ui.setLast();
        });

        btnCancel.setOnAction((e) -> {
            ui.setLast();
        });
    }

}
