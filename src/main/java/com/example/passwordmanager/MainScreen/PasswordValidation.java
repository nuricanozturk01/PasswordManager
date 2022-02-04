package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.Hash.Hash;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PasswordValidation extends MenuScreen implements EventHandler<ActionEvent>
{

    private Label label;
    private Label stateLbl;

    private PasswordField pass;

    private JFXButton btn;

    private VBox vBox;

    private Stage verfiedStage;

    private User user;

    private TableScreen tableScreen;

    private RadioMenuItem hidePassword;

    public PasswordValidation(User user,TableScreen tableScreen,RadioMenuItem hidePassword)
    {
        super(user,tableScreen,hidePassword);

        this.hidePassword = hidePassword;
        this.tableScreen = tableScreen;
        this.user = user;

        label = new Label("Enter the password");
        stateLbl = new Label();

        stateLbl.setStyle("-fx-font-size: 13pt;-fx-text-fill: #085D34;");

        pass = new PasswordField();

        btn = new JFXButton("Enter");

        HBox hBox = new HBox(label,pass);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        vBox = new VBox(hBox,btn,stateLbl);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);


        btn.setOnAction(this);

    }

    public void init()
    {
        Scene scene = new Scene(vBox, Constants.PASS_VAL_SC_X,Constants.PASS_VAL_SC_Y);

        verfiedStage = new Stage();

        Methods.initStage("Password Validation",verfiedStage,scene,false, Constants.ICON_IMG);
        verfiedStage.setScene(scene);

        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());
        btn.setDefaultButton(true);
        verfiedStage.show();
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (btn.isArmed())
            isMatched();
    }

    private void isMatched()
    {
        if (Hash.hashing(pass.getText()).equals(user.getPassword()))
        {
            hidePassword.setSelected(false);

            MainScreen.isShow = true;

            tableScreen.update();

            stateLbl.setText("Success!");

            verfiedStage.close();
        }
        else
            stateLbl.setText("Password is not matched!");

    }
}
