package com.example.passwordmanager.LoginScreen;

import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.Email.Authentication;
import com.example.passwordmanager.Email.Mail;
import com.example.passwordmanager.Hash.Hash;
import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.NullFieldException;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class SignUpScreen implements EventHandler<ActionEvent>
{

    private Stage stage;
    private Connection connection;
    private Scene prevScene;

    private Label nameLbl;
    private Label surnameLbl;
    private Label emailLbl;
    private Label usernameLbl;
    private Label passwordLbl;

    private TextField nameTxt;
    private TextField surnameTxt;
    private TextField emailTxt;
    private TextField usernameTxt;
    private PasswordField passwordTxt;

    private JFXButton signUpButton;
    private JFXButton backButton;

    private  VBox vbox;

    private Label stateLbl;


    public SignUpScreen(Stage stage, Scene prevScene)
    {

        this.stage = stage;
        this.prevScene = prevScene;

        nameLbl     = new Label("Name:     ");
        surnameLbl  = new Label("Surname:  ");
        emailLbl    = new Label("email:    ");
        usernameLbl = new Label("Username: ");
        passwordLbl = new Label("Password: ");
        stateLbl    = new Label();

        nameTxt     = new TextField();
        surnameTxt  = new TextField();
        emailTxt    = new TextField();
        usernameTxt = new TextField();
        passwordTxt = new PasswordField();

        signUpButton = new JFXButton("Sign Up!");
        backButton   = new JFXButton("Back");

        FlowPane fp1 = new FlowPane(nameLbl,nameTxt);
        FlowPane fp2 = new FlowPane(surnameLbl,surnameTxt);
        FlowPane fp3 = new FlowPane(emailLbl,emailTxt);
        FlowPane fp4 = new FlowPane(usernameLbl,usernameTxt);
        FlowPane fp5 = new FlowPane(passwordLbl,passwordTxt);
        HBox fp6 = new HBox(backButton);
        fp6.setSpacing(10);


        fp1.setHgap(15);    fp1.setAlignment(Pos.CENTER);
        fp2.setHgap(10);    fp2.setAlignment(Pos.CENTER);
        fp3.setHgap(20);    fp3.setAlignment(Pos.CENTER);
        fp4.setHgap(10);    fp4.setAlignment(Pos.CENTER);
        fp5.setHgap(10);    fp5.setAlignment(Pos.CENTER);
                            fp6.setAlignment(Pos.BOTTOM_RIGHT);

        fp6.setMinHeight(fp6.getHeight() + 150);


        stateLbl.setStyle("-fx-text-fill: green; -fx-font-size: 13pt;");

        vbox = new VBox(fp1,fp2,fp3,fp4,fp5,signUpButton,stateLbl,fp6);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(10));

        signUpButton.setOnAction(this);
        backButton.setOnAction(this);

        nameLbl.setTextFill(Color.WHITE);
        surnameLbl.setTextFill(Color.WHITE);
        usernameLbl.setTextFill(Color.WHITE);
        passwordLbl.setTextFill(Color.WHITE);
        emailLbl.setTextFill(Color.WHITE);

        connection = Database.connectDatabase();
    }

    public void init()
    {

        Scene scene = new Scene(vbox,Constants.SIGN_SC_X,Constants.SIGN_SC_Y);

        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }
    private void signUp(String name, String surname, String email, String username, String password)
    {
        try
        {
            if(name == null || username == null || email == null || surname == null || password == null)
                throw new NullFieldException();

            if(            name.isEmpty() || name.isBlank()
                    || username.isEmpty() || username.isBlank()
                    || surname.isEmpty()  || surname.isBlank()
                    || email.isEmpty()    || email.isBlank()
                    || password.isBlank() || password.isEmpty()
             )
                        throw new NullFieldException();
            // Authentication Screen
            new Authentication(stage,email,prevScene,name,surname,username,password);

        }
        catch ( NullFieldException e)
        {
            stateLbl.setText(e.getMessage());
        }
        catch (Exception e)
        {
            stateLbl.setText("Somethings are wrong!");
        }

    }
    protected void saveUser(String name, String surname, String email, String username, String password) throws SQLException
    {

           String insertUserQuery =
                   "CALL insertUser(\" "+name+"\",\""+surname+"\",\""+username+"\",\""+password+"\",\""+email+"\");";

           PreparedStatement prs = connection.prepareStatement(insertUserQuery);

           prs.executeUpdate();

           prs.close();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String txt = "Name: "+ name + "\n\nSurname: " + surname + "\n\nUsername: " +
                            username + "\n\nEmail: " + email + "\n\t\t\t\t\tjoin the program!";

                    Mail.sendMessage(Constants.ADMIN_MAIL,"Member Information",txt);
                }
            }).start();

           clear();
    }


    private void clear()
    {
        nameTxt.setText(null);
        surnameTxt.setText(null);
        usernameTxt.setText(null);
        passwordTxt.setText(null);
        emailTxt.setText(null);
    }


    @Override
    public void handle(ActionEvent actionEvent)
    {
        if (backButton.isArmed())
        {
            stage.setScene(prevScene);
            stage.show();
        }
        if (signUpButton.isArmed())
        {
             String name = nameTxt.getText();
             String surname = surnameTxt.getText();
             String email = emailTxt.getText();
             String uname = usernameTxt.getText();
             String pass = Hash.hashing(passwordTxt.getText());

            signUp(name,surname,email,uname,pass);
        }
    }



}
