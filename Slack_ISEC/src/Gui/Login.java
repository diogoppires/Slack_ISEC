package Gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Login extends BorderPane {
    UserInterface ui;
    Observable obs;
    Button btnSendLogin;
    Button btnRegister;
    Button btnexit;
    VBox vbox;

    public Login(Observable obs, UserInterface ui){
        this.obs = obs;
        vbox = new VBox();
        this.ui = ui;
        createLayout();

    }

    private void createLayout() {
        Text textlogin = new Text("Login");
        Text textUserName = new Text("Username");
        Text textPassword = new Text("Password");
        TextField getUserName = new TextField();
        PasswordField getPassword = new PasswordField();
        getUserName.setMaxSize(150,50);
        getPassword.setMaxSize(150,50);

        btnSendLogin = new Button("Login");
        btnexit = new Button("Cancel");
        btnRegister = new Button("Register");
        HBox btnBox = new HBox();
        btnBox.getChildren().addAll(btnSendLogin,btnexit);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(50);


        vbox.getChildren().addAll(textlogin,textUserName,getUserName,textPassword,getPassword, btnBox, btnRegister);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        setCenter(vbox);

        btnSendLogin.setOnAction(e -> {
            obs.sendLogin(getUserName.getText(),getPassword.getText());
        });
        btnRegister.setOnAction(e -> {
            setVisible(false);
            ui.setRegister();

        });
        btnexit.setOnAction(e -> {
            System.exit(1);
        });


    }

}
