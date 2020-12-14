package Gui;


import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller extends Application {

    public void run (String []args){
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Observable Obs = new Observable();
        UserInterface userInterface = new UserInterface(Obs);
        stage.setMinHeight(Constantes.WIN_HEIGHT);
        stage.setMinWidth(Constantes.WIN_WIDTH);
        //stage.setMaxHeight(Constantes.WIN_HEIGHT);
        // stage.setMaxWidth(Constantes.WIN_WIDTH);
        Scene scene = new Scene(userInterface, Constantes.WIN_HEIGHT, Constantes.WIN_WIDTH);
        stage.setScene(scene);
        stage.setTitle("Slack Isec");
        stage.show();

    }



}