package Gui.ChannelsPane;

import Gui.Constantes;
import Gui.Observable;
import Gui.UserInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class DeleteChannel extends BorderPane {
    Observable obs;
    UserInterface ui;
    Text textName;
    TextField getName;
    Button btnSendDelete;
    Button btnCancel;

    public DeleteChannel(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;

        createLayout();
        registeListeners();
    }
    private void createLayout() {

        textName = new Text("Channel Name:");
        getName = new TextField();
        btnSendDelete = new Button("Delete");
        btnCancel = new Button("Cancel");

        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnSendDelete,btnCancel);

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(textName,getName,btnBox);
        mainBox.setMaxWidth(Constantes.WIN_WIDTH * 0.50);
        setCenter(mainBox);
    }

    private void registeListeners() {

        btnSendDelete.setOnAction((e) -> {
            obs.deleteEditChannel(
                    getName.getCharacters().toString());
            ui.setLast();
        });

        btnCancel.setOnAction((e) -> {
            ui.setLast();
        });
    }



}
