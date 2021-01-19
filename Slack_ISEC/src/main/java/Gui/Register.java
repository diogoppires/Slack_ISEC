package Gui;


import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;

public class Register extends BorderPane {

    UserInterface ui;
    Observable obs;
    Button btnSendRegister;
    Button btnLogin;
    Button btnexit;
    Button btnimg;
    VBox vbox;
    TextField getUserName;
    PasswordField getPassword;
    TextField getName;
    ImageView imgView;
    String imagePath;

    public Register(Observable obs, UserInterface ui){

        this.ui = ui;
        this.obs = obs;
        vbox = new VBox();
        createLayout();
        registeListeners();

    }

    private void createLayout() {

        Text textRegister = new Text("Register");
        Text textName = new Text("Name");
        Text textUserName = new Text("Username");
        Text textPassword = new Text("Password");

        getUserName = new TextField();
        getPassword = new PasswordField();
        getName = new TextField();
        imgView = new ImageView();

        getUserName.setMaxSize(250,50);
        getPassword.setMaxSize(250,50);
        getName.setMaxSize(250,50);

        btnSendRegister = new Button("Sign Up");
        btnexit = new Button("Exit");
        btnLogin = new Button("Login");
        HBox btnBox = new HBox();
        btnBox.getChildren().addAll(btnSendRegister,btnexit);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(50);

        VBox imgBox = new VBox();
        btnimg = new Button("Image");
        imgView.setFitHeight(100);
        imgView.setFitWidth(100);
        imgBox.getChildren().addAll(imgView,btnimg);
        imgBox.setSpacing(20);
        imgBox.setAlignment(Pos.CENTER);


        vbox = new VBox();
        vbox.getChildren().addAll(
                textRegister,
                imgBox,
                textName, getName,
                textUserName,getUserName,
                textPassword, getPassword,
                btnBox, btnLogin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        setCenter(vbox);
    }
    File image;
    private void registeListeners() {
        btnimg.setOnAction((e) -> {
            FileChooser fc = new FileChooser();
            image = fc.showOpenDialog(getScene().getWindow());
            if(image == null)
                return;
            try{
                image.getAbsolutePath();
                image.getName();
                imagePath = image.getAbsolutePath();
                Image img = new Image(new FileInputStream(image));
                imgView.setImage(img);

            } catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Load file failed.");
                alert.showAndWait();
            }
        });

        btnSendRegister.setOnAction((e) -> {
            if(image == null) {
                obs.sendRegister(getName.getCharacters().toString() + "+" +
                                getUserName.getCharacters().toString() + "+" +
                                getPassword.getCharacters().toString() + "+" +
                                "null", null, null);
            } else
                obs.sendRegister(getName.getCharacters().toString() + "+" +
                                getUserName.getCharacters().toString() + "+" +
                                getPassword.getCharacters().toString() + "+" +
                                "null"
                        , image.getParent(), image.getName());

            ui.setLogin();
        });

        btnLogin.setOnAction((e) -> {
            setVisible(false);
            ui.setLogin();
        });
        btnexit.setOnAction(e -> {
            setVisible(false);
            ui.setLogin();
        });
    }
}
