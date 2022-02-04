package com.example.passwordmanager.LoginScreen;

import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.Email.GenerateKey;
import com.example.passwordmanager.Email.Mail;

import com.example.passwordmanager.MainMenu;

import com.example.passwordmanager.Util.Constants;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ForgotPasswordScreen implements EventHandler<ActionEvent>
{
    private Stage stage;
    private Scene prevScene;

    private Connection connection;
    private Statement statement;

    private Label emailLabel;
    private Label stateLbl;

    private TextField emailTxt;

    private JFXButton sendBtn;
    private JFXButton backBtn;

    private VBox vBox;

    private String key;
    public ForgotPasswordScreen(Stage stage, Scene prevScene)
    {
        this.stage = stage;
        this.prevScene = prevScene;

        try
        {
            key = GenerateKey.getKey();

            emailLabel = new Label("Email: ");
            stateLbl = new Label();
            stateLbl.setStyle("-fx-font-size: 13pt; -fx-text-fill: green;");

            emailTxt = new TextField();

            sendBtn = new JFXButton("Send");
            backBtn = new JFXButton("Back");


            HBox hBox = new HBox(emailLabel,emailTxt);
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);

            HBox btn = new HBox(backBtn);
            btn.setSpacing(10);
            btn.setAlignment(Pos.BASELINE_RIGHT);
            btn.setPadding(new Insets(10));
            btn.setMinHeight(btn.getHeight());

            vBox = new VBox(hBox,sendBtn,stateLbl,btn);
            vBox.setSpacing(20);
            vBox.setAlignment(Pos.BOTTOM_CENTER);

            sendBtn.setOnAction(this);
            backBtn.setOnAction(e->{stage.setScene(prevScene);});

            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void init()
    {
        Scene scene = new Scene(vBox, Constants.FORGOT_PASSWORD_SC_X,Constants.FORGOT_PASSWORD_SC_Y);
        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void handle(ActionEvent event)
    {
        if (sendBtn.isArmed())
        {
            try
            {
                String email = emailTxt.getText();
                if (email.isBlank() || email.isBlank() || !email.contains("@"))
                    throw new Exception();

                AuthenticationEmail(email);
            }
            catch (Exception e)
            {
                stateLbl.setText("Please control email!");
            }
        }
    }

    private void AuthenticationEmail(String email)
    {
        try
        {
            String query = "SELECT email FROM User WHERE email = \""+email+"\";";

            ResultSet rset = statement.executeQuery(query);

            String mail = "";

            rset.next();
            mail = rset.getString("email");

            if (mail.equals(email))
            {
                sendCode(email);
                new EmailValidation(stage,email,key,prevScene).init();
            }
            else throw new Exception();


        }
        catch (SQLException e)
        {
            stateLbl.setText("Email is not matched!");
        }
        catch (Exception e)
        {
            stateLbl.setText("Email is not matched!");
        }
    }

    private void sendCode(String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Mail.sendMessage(email,"Password Reset",("Reset Code: " + key));
            }
        }).start();
    }


}
