package com.example.passwordmanager.LoginScreen;

import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.Hash.Hash;
import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.PasswordNotMatchedException;
import com.example.passwordmanager.MainMenu;


import com.example.passwordmanager.Util.Constants;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class PasswordChangeScreen implements EventHandler<ActionEvent>
{
    private Stage stage;
    private Scene loginScene;
    private VBox vBox;

    private Statement statement;
    private Connection connection;

    private String email;

    private PasswordField newPasswordTxt1;
    private PasswordField newPasswordTxt2;

    private JFXButton sendBtn;
    private JFXButton backBtn;

    private Label pass1;
    private Label pass2;

    private Label stateLabel;

    public PasswordChangeScreen(String email, Stage stage, Scene loginScene)
    {
        this.email = email;
        this.stage = stage;
        this.loginScene = loginScene;

        try
        {
            stateLabel = new Label();
            pass1 = new Label("New Password: ");
            pass2 = new Label("New Password: ");
            stateLabel.setStyle("-fx-font-size: 13pt;-fx-text-fill: green;");

            newPasswordTxt1 = new PasswordField();
            newPasswordTxt2 = new PasswordField();

            sendBtn = new JFXButton("Send");
            backBtn = new JFXButton("Back");

            HBox hBox2 = new HBox(pass1,newPasswordTxt1);
            hBox2.setSpacing(10);
            hBox2.setAlignment(Pos.CENTER);

            HBox hBox3 = new HBox(pass2,newPasswordTxt2);
            hBox3.setSpacing(10);
            hBox3.setAlignment(Pos.CENTER);

            HBox btn = new HBox(backBtn);
            btn.setSpacing(10);
            btn.setAlignment(Pos.BASELINE_RIGHT);
            btn.setPadding(new Insets(10));
            btn.setMinHeight(btn.getHeight());

            vBox = new VBox(hBox2,hBox3,sendBtn,stateLabel,btn);
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setSpacing(10);

            sendBtn.setOnAction(this);
            backBtn.setOnAction(e->{stage.setScene(loginScene);});

            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (sendBtn.isArmed())
        {
            try
            {
                String passTxt1 = newPasswordTxt1.getText();
                String passTxt2 = newPasswordTxt2.getText();

                if (!passTxt1.equals(passTxt2) || passTxt1.isEmpty() || passTxt2.isBlank())
                    throw new PasswordNotMatchedException();

                validator(passTxt1,passTxt2);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            catch (PasswordNotMatchedException e)
            {
               stateLabel.setText(e.getMessage());

            }
        }
    }

    private void validator(String pass1, String pass2) throws SQLException {
      try
      {
          if (pass1.equals(pass2))
          {
              changeUserPassword(email,pass1);
              stateLabel.setText("Password changed successfully!");
              stage.setScene(loginScene);
          }

          else throw new PasswordNotMatchedException();
      }
      catch (PasswordNotMatchedException e)
      {
          stateLabel.setText(e.getMessage());
      }

    }

    public void init()
    {
        Scene scene = new Scene(vBox, Constants.PASS_CHANGE_SC_X,Constants.PASS_CHANGE_SC_Y);
        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());

        stage.setScene(scene);
    }

    private void changeUserPassword(String email, String password) throws SQLException
    {
        String getUserQuery = "SELECT userID FROM User WHERE email = \""+email+"\";";

        ResultSet rset = statement.executeQuery(getUserQuery);

        rset.next();

        int userID = rset.getInt("userID");

        if (userID == 0)
            throw new SQLException();

        String hashedPassword = Hash.hashing(password);

        String updatePasswordQuery = "CALL updatePassword("+userID+",\""+hashedPassword+"\");";

        PreparedStatement pst = connection.prepareStatement(updatePasswordQuery);
        pst.executeUpdate();


    }
}
