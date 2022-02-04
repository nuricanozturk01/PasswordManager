package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.Crypthology.AES256;
import com.example.passwordmanager.DB.App;
import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.User;
import com.example.passwordmanager.LoginScreen.LoginScreenExceptions.EmptyTextException;
import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.*;

public class AddScreen implements EventHandler<ActionEvent>
{
    private TableScreen tableScreen;
    private User user;

    private Statement statement;
    private Connection connection;

    private Label addLbl;
    private Label nameLbl;
    private Label passwordLbl;
    private Label appUsernameLbl;

    private TextField nameTxt1;
    private TextField appUsernameTxt;

    private PasswordField passwordTxt;

    private Button addButton;



    public AddScreen(User user,TableScreen tableScreen)
    {
        this.tableScreen = tableScreen;
        this.user = user;

        initComponents();
        verifyConnection();
    }

    private void verifyConnection()
    {
        try
        {
            connection = Database.connectDatabase();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void initComponents()
    {
        addLbl      = new Label("\t\t\t\tAdd");

        addLbl.setFont(new Font("Arial",15));
        addLbl.setStyle("-fx-font-weight: bold");


        nameLbl     = new Label("Name: ");
        nameTxt1    = new TextField();

        appUsernameLbl = new Label("Username: ");
        appUsernameTxt = new TextField();

        passwordLbl = new Label("Password: ");
        passwordTxt = new PasswordField();


        addButton = new Button("Add");
        addButton.setAlignment(Pos.CENTER);

        addButton.setOnAction(this);
    }
    public VBox getVBox()
    {
        HBox Hbox1 = new HBox(nameLbl,nameTxt1);
        HBox Hbox2 = new HBox(passwordLbl, passwordTxt);
        HBox Hbox3 = new HBox(appUsernameLbl,appUsernameTxt);
        HBox Hbox4 = new HBox(addButton);

        Hbox1.setSpacing(28);
        Hbox2.setSpacing(10);
        Hbox3.setSpacing(7);
        Hbox4.setAlignment(Pos.CENTER);

        VBox addVBox = new VBox(addLbl,Hbox1,Hbox3,Hbox2,Hbox4);
        addVBox.setSpacing(10);


        return addVBox;
    }
    public void add()
    {

        try
        {
            String appuname = appUsernameTxt.getText();
            String appName = nameTxt1.getText();
            String pass = passwordTxt.getText();


            if (appName == null || appuname == null || pass == null
                    ||appName.isEmpty() || appName.isBlank() ||
                    pass.isEmpty() || pass.isBlank() ||
                    appuname.isBlank() || appuname.isEmpty())
                throw new EmptyTextException();

            ResultSet r = statement.executeQuery("CALL getAppCount()");
            r.next();
            int size = r.getInt(1);

            App app = new App((size+1),appName,appuname,pass);
            tableScreen.getAppList().add(app);
            App app2 = new App((size+1),app.getAppName(),app.getAppUsername(),app.getAppPassword());
            app2.setAppPassword(Constants.MASK);
            tableScreen.getSecretAppList().add(app2);

            String secretKey = AES256.generateSecretKey(user.getUsername());
            AES256 aes256 = new AES256(secretKey);
            pass = aes256.encrypt(pass);

            clear();


            String q1 = "CALL insertApp(\"" + appName + "\",\"" + appuname + "\",\""+pass + "\");";

            String q2 = "CALL insertUserApp(\"" + user.getUserID() + "\",\"" + (size + 1) + "\");";

            PreparedStatement pst = connection.prepareStatement(q1);
            pst.executeUpdate();

            PreparedStatement pst2  = connection.prepareStatement(q2);
            pst2.executeUpdate();

            pst.close();
            pst2.close();


            tableScreen.update();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (EmptyTextException x)
        {
            Methods.alertScreen(Alert.AlertType.WARNING,x.getMessage(),ButtonType.OK);
            clear();
        }
    }

    private void clear()
    {
        nameTxt1.setText(null);
        passwordTxt.setText(null);
        appUsernameTxt.setText(null);
    }
    @Override
    public void handle(ActionEvent actionEvent)
    {
        if (addButton.isArmed())
            add();
    }
}
