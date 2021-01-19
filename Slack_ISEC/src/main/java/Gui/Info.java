package Gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Info extends Stage {

    Observable obs;

    BorderPane borderPane;
    Scene scene;
    Label info;
    Text msgView;

    public Info(Observable obs) {
        this.obs = obs;
        info = new Label();
        borderPane = new BorderPane();
        info.setMinSize(Constantes.INFO_WIDTH, Constantes.INFO_HEIGHT);
        borderPane.setMinSize(Constantes.INFO_WIDTH, Constantes.INFO_HEIGHT);
        //borderPane.setTop(info);
         msgView = new Text("Lorem Ipsum is simply dummy text of the printing and\n");
// chat VIEW
        BorderPane textPane = new BorderPane();
        textPane.setMinSize(Constantes.INFO_WIDTH, Constantes.INFO_HEIGHT);
        textPane.setPadding(new Insets(20,20,20,20));
        textPane.getChildren().add(msgView);
        ScrollPane textScroll = new ScrollPane();
        textScroll.setMaxSize(Constantes.INFO_WIDTH, Constantes.INFO_HEIGHT);
        textScroll.setMinSize(Constantes.INFO_WIDTH, Constantes.INFO_HEIGHT );
        textScroll.setContent(textPane);
        borderPane.setCenter(textScroll);
        scene = new Scene(borderPane);
        setScene(scene);


    }
    public void setMsgView(String s){
        msgView.setText("\n\n" + s);
    }




}
