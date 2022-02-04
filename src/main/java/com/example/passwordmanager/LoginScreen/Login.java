package com.example.passwordmanager.LoginScreen;

import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.NullFieldException;
import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.UserNotFoundException;
import com.example.passwordmanager.MainScreen.LogScreen;
import com.example.passwordmanager.MainScreen.MainScreen;
import com.example.passwordmanager.Util.Methods;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.*;

public class Login
{

    private String username;
    private String password;
    private Connection connection;
    private Statement statement;
    private Scene scene;


    public Login(String username, String password, Scene scene)
    {
        this.scene = scene;
        try
        {
            if (username.isEmpty() || password.isEmpty())
                throw new NullFieldException();

            this.username = username;
            this.password = password;

            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException | NullFieldException e)
        {
            Methods.alertScreen(Alert.AlertType.WARNING,"Please fill the all blanks!",ButtonType.OK);
        }
    }



    public void login(Stage stage)
    {
        try
        {
            User user = getUser(username);
            LogScreen log = new LogScreen(user);

            if (user == null)
                throw new UserNotFoundException();

            if (isVerify(user.getUsername(),user.getPassword()))
            {
                log.addLog(true);
                new MainScreen(stage,user,connection,scene);

            }
            else if (isUsernameValid(user.getUsername(),user.getPassword()))
            {
                log.addLog(false);
                throw new UserNotFoundException();
            }
            else
                throw new UserNotFoundException();

        }
        catch (UserNotFoundException e)
        {

            Methods.alertScreen(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
        }
    }



    private boolean isVerify(String userUsername, String userPassword)
    {
        return userUsername.equals(username) && userPassword.equals(password);
    }
    private boolean isUsernameValid(String userUsername, String userPassword)
    {
        return userUsername.equals(username) && !userPassword.equals(password);
    }

    private User getUser(String username)
    {

        String query =
                "SELECT userID, username, password FROM User WHERE username = \""+username+"\" ;";
        try
        {
            User user = null;

            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            user = new User(resultSet.getInt("userID"),resultSet.getString("username"),resultSet.getString("password"));
            resultSet.close();
            return user;
        }
        catch (SQLException e)
        {
            System.err.println("EMPTY!");
        }
        return null;
    }

}
