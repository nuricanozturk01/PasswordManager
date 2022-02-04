package com.example.passwordmanager.LoginScreen;


import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.EmailNotVerifiedException;
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


public class EmailValidation implements EventHandler<ActionEvent> {
    private Label lbl;
    private Label stateLbl;

    private TextField code;

    private JFXButton sendBtn;
    private JFXButton backBtn;

    private Stage stage;
    private Scene loginScene;

    private VBox vBox;


    private String email;
    private String key;

    public EmailValidation(Stage stage, String email, String key, Scene loginScene)
    {
        this.loginScene = loginScene;
        this.key = key;
        this.stage = stage;
        this.email = email;



        lbl = new Label("(!) Email Code: ");
        stateLbl = new Label();
        stateLbl.setStyle("-fx-font-size: 13pt; -fx-text-fill: green;");

        code = new TextField();

        backBtn = new JFXButton("Back");
        sendBtn = new JFXButton("Send");

        HBox codeBox = new HBox(lbl,code);
        codeBox.setSpacing(10);
        codeBox.setAlignment(Pos.CENTER);

        HBox btn = new HBox(backBtn);
        btn.setSpacing(10);
        btn.setAlignment(Pos.BASELINE_RIGHT);
        btn.setPadding(new Insets(10));
        btn.setMinHeight(btn.getHeight());

        vBox = new VBox(codeBox, sendBtn,stateLbl,btn);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        sendBtn.setOnAction(this);
        backBtn.setOnAction(e->{stage.setScene(loginScene);});

    }

    public void init() {
        Scene scene = new Scene(vBox, Constants.EMAIL_VALIDATION_SC_X,Constants.EMAIL_VALIDATION_SC_Y);
        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());

        stage.setScene(scene);
    }


    @Override
    public void handle(ActionEvent event)
    {

        if (sendBtn.isArmed())
        {
            try
            {
                String userKey = code.getText();

                if (userKey.isBlank() || userKey.isEmpty())
                    throw new Exception();

                isValid(userKey);
            }
            catch (Exception e)
            {
                stateLbl.setText("Code should not be empty or blank!");
            }

        }
    }

    private void isValid(String userKey)
    {
        try
        {
            if (userKey.equals(key))
                new PasswordChangeScreen(email,stage,loginScene).init();
            else throw new EmailNotVerifiedException();
        }
        catch (EmailNotVerifiedException e)
        {
            stateLbl.setText("Key is wrong!");
        }
    }
}
