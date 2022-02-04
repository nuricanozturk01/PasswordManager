package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.sql.Connection;


public class MainScreen
{
    public static boolean isShow;

    private Stage stage;

    private Scene widowScene;
    private Scene scene;

    private User user;

    private GeneratePasswordScreen passwordGenerateScreen;
    private AddScreen addScreen;
    private MenuScreen menuScreen;
    private TableScreen tableScreen;
    private Connection connection;


    public MainScreen(Stage stage, User user, Connection connection,Scene scene)
    {
        isShow = false;

        this.connection = connection;
        this.scene = scene;
        this.stage = stage;
        this.user = user;

        passwordGenerateScreen = new GeneratePasswordScreen();
        tableScreen            = new TableScreen(stage,user);
        addScreen              = new AddScreen(user,tableScreen);
        menuScreen             = new MenuScreen(stage,connection,scene,user,tableScreen);

        initComponents();
    }

    private void initComponents()
    {


        VBox tableVBox             = tableScreen.getVBox();
        VBox passwordGeneratorVBox = passwordGenerateScreen.getVBox();
        VBox addVBox               = addScreen.getVBox();

        VBox menuVBox = menuScreen.getMenuVBox();

        VBox leftSide     = new VBox(tableVBox);
        VBox rightSide    = new VBox(passwordGeneratorVBox,addVBox);

        leftSide.setSpacing(15);
        rightSide.setSpacing(10);


        BorderPane borderPane = new BorderPane();

        borderPane.setTop(menuVBox);
        borderPane.setLeft(leftSide);
        borderPane.setRight(rightSide);

        BorderPane.setMargin(borderPane.getLeft(),new Insets(10));
        BorderPane.setMargin(borderPane.getRight(),new Insets(10));

        widowScene = new Scene(borderPane,Constants.MAIN_SC_X,Constants.MAIN_SC_Y);

        widowScene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());
        String stageTitle = stage.getTitle();

        Methods.initStage(stageTitle,stage,widowScene,false,Constants.ICON_IMG);

    }


}
