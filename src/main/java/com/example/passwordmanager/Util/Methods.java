package com.example.passwordmanager.Util;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Methods
{
    public static void initStage(String title, Stage stage, Scene scene, boolean resizable, String icon)
    {
        stage.setTitle(title);
        stage.getIcons().add(new Image(icon));
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.show();
    }


    public static void alertScreen(Alert.AlertType type, String msg, ButtonType btn)
    {
        Alert alert = new Alert(type,msg,btn);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
