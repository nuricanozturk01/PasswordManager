package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.DB.User;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class MenuScreen implements EventHandler<ActionEvent>
{
    private MenuBar menuBar;

    private Menu settingMenu;
    private Menu fileMenu;

    private MenuItem exit;
    private MenuItem log;

    private RadioMenuItem hidePassword;
    private RadioMenuItem showPassword;

    private Stage stage;
    private Scene loginScene;

    private User user;

    private TableScreen tableScreen;

    private Connection connection;

    public MenuScreen(Stage stage, Connection connection,Scene loginScene,User user,TableScreen tableScreen)
    {

        this.tableScreen = tableScreen;
        this.user = user;
        this.loginScene = loginScene;
        this.connection = connection;
        this.stage = stage;

        initComponents();
    }

    public MenuScreen(User user,TableScreen tableScreen,RadioMenuItem hidePassword)
    {
        this.hidePassword = hidePassword;
        this.user = user;
        this.tableScreen = tableScreen;
        initComponents();
    }


    private void initComponents()
    {
        fileMenu     = new Menu("File");
        settingMenu  = new Menu("Settings");
        log          = new MenuItem("Log");

        exit         = new RadioMenuItem("Exit");
        hidePassword = new RadioMenuItem("Hide Password");
        showPassword = new RadioMenuItem("Show Password");


        fileMenu.getItems().addAll(log,exit);
        
        settingMenu.getItems().addAll(hidePassword,showPassword);

        menuBar = new MenuBar(fileMenu,settingMenu);

        hidePassword.setSelected(true);

        log.setOnAction(e->{showLog();});
        exit.setOnAction(e->{exitUser();});

        hidePassword.setOnAction(e->{hidePassword();});
        showPassword.setOnAction(e->{showPasswords();});

    }

    private void showPasswords()
    {
        if (!showPassword.isSelected())
        {
            showPassword.setSelected(true);
            return;
        }
        new PasswordValidation(user,tableScreen,hidePassword).init();

    }

    private void hidePassword()
    {
        if (!hidePassword.isSelected())
            hidePassword.setSelected(true);

        showPassword.setSelected(false);
        MainScreen.isShow = false;
        tableScreen.update();
    }

    private void exitUser()
    {
        try
        {
            connection.close();
            stage.setScene(loginScene);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showLog()
    {
        try
        {
            new LogScreen(user).start(new Stage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getMenuVBox()
    {
        return new VBox(menuBar);
    }


    @Override
    public void handle(ActionEvent actionEvent)
    {

    }
}


