package Gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Init extends BorderPane {

    Observable obs;
    UserInterface ui;
    Text textIp;
    Text textPort;
    TextField getIp;
    TextField getPort;
    Button btnConnect;
    Button btnCancel;

    public Init(Observable obs, UserInterface ui) {
        this.obs = obs;
        this.ui = ui;

        createLayout();
        registeListeners();
    }

    private void createLayout() {

        Text Welcome = new Text("Welcome to Slack Isec\nPlease insert the Ip and Port of Server.");

        textIp = new Text("Server IP:");
        textPort = new Text("Server Port");
        getIp = new TextField();
        getPort = new TextField();

        btnConnect = new Button("Enter");
        btnCancel = new Button("Cancel");

        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnConnect, btnCancel);

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(Welcome,textIp,getIp,textPort,getPort, btnBox);
        mainBox.setMaxWidth(Constantes.WIN_WIDTH * 0.50);
        setCenter(mainBox);
    }

    private void registeListeners() {

        btnConnect.setOnAction((e) -> {
            obs.startCommunication(getIp.getText(),
                    Integer.parseInt(getPort.getText()));
            ui.setLogin();
        });

        btnCancel.setOnAction((e) -> {
            ui.setLast();
        });
    }


}