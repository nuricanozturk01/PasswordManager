package com.example.passwordmanager.Email;

import com.example.passwordmanager.LoginScreen.SignUpScreen;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class Authentication extends SignUpScreen implements EventHandler<ActionEvent>,Runnable
{
    private Scene prevScene;
    private Stage stage;
    private String key;
    private String email;

    private Thread thread;
    private Label codeLbl;
    private Label stateLbl;
    private TextField codeTxt;
    private JFXButton enterBtn;
    private JFXButton backBtn;

    private String name;
    private String surname;
    private String username;
    private String password;

    public Authentication(Stage stage, String email,Scene prevScene,String name, String surname, String username, String password)
    {
        super(stage,prevScene);

        this.stage = stage;
        this.prevScene=prevScene;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;


        initComponents();
    }

    private void initComponents()
    {
        codeLbl = new Label("Enter the code: ");
        stateLbl = new Label("1 times left!");
        stateLbl.setStyle("-fx-font-size: 13pt; -fx-text-fill: green;");
        codeTxt = new TextField();

        enterBtn = new JFXButton("Enter");
        backBtn = new JFXButton("Back");

        HBox hBox = new HBox(codeLbl,codeTxt);

        hBox.setSpacing(15);
        hBox.setAlignment(Pos.CENTER);

        VBox backButtonBox = new VBox(backBtn);
        backButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
        backButtonBox.setMinHeight(backButtonBox.getHeight());
        backButtonBox.setPadding(new Insets(10));

        VBox vBox = new VBox(hBox,enterBtn,stateLbl,backButtonBox);

        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.setSpacing(10);

        Scene scene = new Scene(vBox,Constants.AUTH_SC_X,Constants.AUTH_SC_Y);
        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());
        stage.setScene(scene);

        thread = new Thread(this,"emailThread");
        thread.start();

        enterBtn.setOnAction(this);
        backBtn.setOnAction(this);

    }

    @Override
    public void handle(ActionEvent actionEvent)
    {
        if (enterBtn.isArmed())
        {
            try
            {
                validator();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (backBtn.isArmed())
            stage.setScene(prevScene);
    }

    private void validator() throws SQLException, InterruptedException {
        String userKey = codeTxt.getText();

        if (userKey.equals(key))
        {
            saveUser(name,surname,email,username,password);
            stage.setScene(prevScene);
            stateLbl.setText("Successful!");
        }
        else
            stateLbl.setText("Try Again!");
    }


    @Override
    public void run()
    {
        synchronized (this) {
            key = GenerateKey.getKey();
            Mail.sendMessage(email, "Email Verification", "Your verification code: " + key);
        }

    }
}
