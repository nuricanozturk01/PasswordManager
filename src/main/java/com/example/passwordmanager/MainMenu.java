package com.example.passwordmanager;

import com.example.passwordmanager.Hash.Hash;
import com.example.passwordmanager.LoginScreen.ForgotPasswordScreen;
import com.example.passwordmanager.LoginScreen.Login;
import com.example.passwordmanager.LoginScreen.SignUpScreen;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;


public class MainMenu extends Application implements EventHandler<ActionEvent>
{
    private TextField usernameTxt;
    private PasswordField passwordTxt;

    private Label usernameLbl;
    private Label passwordLbl;
    private JFXButton loginBtn;
    private JFXButton signUpBtn;
    private JFXButton forgotPasswordBtn;
    private VBox vb;
    private Label stateLbl;
    private Scene currentScene;

    public MainMenu()
    {
        usernameLbl = new Label("Username: ");
        passwordLbl = new Label("Password: ");
        stateLbl    = new Label();
        stateLbl.setStyle("-fx-text-fill: green; -fx-font-size: 13pt;");

        usernameTxt = new TextField();
        passwordTxt = new PasswordField();

        loginBtn = new JFXButton("Login");
        signUpBtn = new JFXButton("Sign Up!");
        forgotPasswordBtn = new JFXButton("Forgot Password");

        FlowPane fp1 = new FlowPane(usernameLbl,usernameTxt);
        FlowPane fp2 = new FlowPane(passwordLbl,passwordTxt);
        FlowPane fp3 = new FlowPane(loginBtn);

        fp1.setHgap(10);
        fp3.setHgap(30);
        fp2.setHgap(10);


        fp1.setAlignment(Pos.CENTER);
        fp2.setAlignment(Pos.CENTER);
        fp3.setAlignment(Pos.CENTER);


        HBox hBox = new HBox(forgotPasswordBtn,signUpBtn);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(240);
        hBox.setPadding(new Insets(10));
        hBox.setMinHeight(hBox.getHeight() + 200);
        hBox.setAlignment(Pos.BOTTOM_CENTER);

        signUpBtn.setAlignment(Pos.BOTTOM_RIGHT);
        vb = new VBox(fp1,fp2,fp3,stateLbl,hBox);
        vb.setAlignment(Pos.BOTTOM_CENTER);
        vb.setSpacing(15);



        usernameLbl.setTextFill(Color.WHITE);
        passwordLbl.setTextFill(Color.WHITE);
    }

    @Override
    public void start(Stage stage)
    {
        currentScene = new Scene(vb, Constants.MAIN_MENU_SC_X,Constants.MAIN_MENU_SC_Y);
        currentScene.getStylesheets().add(getClass().getResource("dark_theme.css").toExternalForm());
        Methods.initStage("Password Manager",stage,currentScene,false,Constants.ICON_IMG);

        loginBtn.setOnAction(e->{login(stage);});
        loginBtn.setDefaultButton(true);
        signUpBtn.setOnAction(e->{signUp(stage);});
        forgotPasswordBtn.setOnAction(e->{forgotPassword(stage);});
    }

    private void forgotPassword(Stage stage)
    {
       ForgotPasswordScreen screen = new ForgotPasswordScreen(stage,currentScene);
       screen.init();
    }

    @Override
    public void handle(ActionEvent actionEvent)
    {

    }

    private void signUp(Stage stage)
    {
        SignUpScreen sign = new SignUpScreen(stage,currentScene);
        sign.init();
        clear();
    }
    private void clear()
    {
        usernameTxt.setText(null);
        passwordTxt.setText(null);
    }
    private void login(Stage stage)
    {
        try
        {
            if (usernameTxt.getText().isEmpty() || usernameTxt.getText().isBlank()
                || passwordTxt.getText().isEmpty() || passwordTxt.getText().isBlank())
                throw new Exception();

            String pass = Hash.hashing(passwordTxt.getText());
            passwordTxt.setText(null);

            Login l = new Login(usernameTxt.getText(),pass,currentScene);
            l.login(stage);
        }
        catch (Exception e) {
            System.err.println("NOT NULL!");
        }
    }

    public static void main(String[] args)
    {
        launch();
    }



}