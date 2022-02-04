package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.Crypthology.AES256;
import com.example.passwordmanager.DB.App;
import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.MainMenu;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
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


public class UpdateScreen extends Application implements EventHandler<ActionEvent>
{
    private Label updateAppNameLbl;
    private Label updateAppUnameLbl;
    private Label updateAppPasswordLbl;

    private TextField updateAppNameTxt;
    private TextField updateAppUnameTxt;
    private TextField updateAppPasswordTxt;

    private TableScreen tableScreen;

    private JFXButton updateButton;

    private User user;
    private App app;

    private Connection connection;

    private int idx;

    public UpdateScreen(User user, App app,TableScreen tableScreen)
    {
        this.tableScreen = tableScreen;
        this.user = user;
        this.app = app;

        connection = Database.connectDatabase();

        if (MainScreen.isShow)
            idx = tableScreen.getAppIndex(app,tableScreen.getAppList());

        else idx = tableScreen.getAppIndex(app,tableScreen.getSecretAppList());
    }

    @Override
    public void start(Stage stage)
    {
        initCompenents(stage);
    }
    private void initCompenents(Stage stage)
    {
        updateAppNameLbl        = new Label("Name: ");
        updateAppUnameLbl       = new Label("Username: ");
        updateAppPasswordLbl    = new Label("Password: ");

        updateAppPasswordTxt    = new TextField();
        updateAppNameTxt        = new TextField();
        updateAppUnameTxt       = new TextField();


        updateAppPasswordTxt.setPrefWidth(180);
        updateAppPasswordTxt.setMinWidth(updateAppPasswordTxt.getPrefWidth());
        updateAppNameTxt.setMinWidth(updateAppPasswordTxt.getPrefWidth());
        updateAppUnameTxt.setMinWidth(updateAppPasswordTxt.getPrefWidth());


        updateAppPasswordTxt.setMaxWidth(100);
        updateAppNameTxt.setMaxWidth(100);
        updateAppUnameTxt.setMaxWidth(100);

        updateButton            = new JFXButton("Update");

        HBox updateVBox1 = new HBox(updateAppNameLbl,updateAppNameTxt);
        HBox updateVBox2 = new HBox(updateAppUnameLbl,updateAppUnameTxt);
        HBox updateVBox3 = new HBox(updateAppPasswordLbl,updateAppPasswordTxt);

        updateVBox1.setSpacing(22);
        updateVBox1.setAlignment(Pos.CENTER);
        updateVBox2.setAlignment(Pos.CENTER);
        updateVBox3.setAlignment(Pos.CENTER);
        updateVBox3.setSpacing(5);

        VBox updateHBox  = new VBox(updateVBox1,updateVBox2,updateVBox3,updateButton);
        updateHBox.setPadding(new Insets(10));
        updateHBox.setSpacing(10);
        updateHBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(updateHBox, Constants.UPDATE_SC_X,Constants.UPDATE_SC_Y);
        scene.getStylesheets().add(MainMenu.class.getResource("dark_theme.css").toExternalForm());

        Methods.initStage("Update",stage,scene,false, Constants.ICON_IMG);

        updateAppNameTxt.setText(app.getAppName());
        updateAppUnameTxt.setText(app.getAppUsername());
        updateAppPasswordTxt.setText(app.getAppPassword());

        updateButton.setOnAction(this);
    }


    public void update()
    {
        try
        {
            String pass = updateAppPasswordTxt.getText();
            updateLists();
            pass = new AES256(AES256.generateSecretKey(user.getUsername())).encrypt(pass);

            String updateQuery =  "CALL updateApp(\"" + app.getAppName() + "\",\"" + app.getAppUsername() + "\",\""+ pass + "\",\""+app.getAppID()+"\");";

            PreparedStatement pst = connection.prepareStatement(updateQuery);
            pst.executeUpdate();

            pst.close();
            tableScreen.update();


        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateLists()
    {
        tableScreen.getAppList().set(idx,app);
        tableScreen.getSecretAppList().set(idx,new App(app.getAppID(),app.getAppName(),app.getAppUsername(),Constants.MASK));
    }

    @Override
    public void handle(ActionEvent actionEvent)
    {
        if (updateButton.isArmed())
        {

            app.setAppName(updateAppNameTxt.getText());
            app.setAppUsername(updateAppUnameTxt.getText());
            app.setAppPassword(updateAppPasswordTxt.getText());

            update();
        }

    }





}
