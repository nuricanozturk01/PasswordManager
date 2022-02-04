package com.example.passwordmanager.MainScreen;

import com.example.passwordmanager.DB.App;
import com.example.passwordmanager.DB.Database;
import com.example.passwordmanager.DB.User;
import java.sql.*;

public class DeleteScreen
{
    private App app;
    private User user;

    private TableScreen tableScreen;

    private Connection connection;

    public DeleteScreen(User user, App app,TableScreen tableScreen)
    {
        this.tableScreen = tableScreen;
        this.user = user;
        this.app = app;

        connection = Database.connectDatabase();
    }

    public void delete(App app)
    {
        try
        {
            int userID = user.getUserID();


            int idx0 = tableScreen.getAppIndex(app.getAppUsername(),tableScreen.getAppList());
            tableScreen.getAppList().remove(idx0);

            int idx = tableScreen.getAppIndex(app.getAppUsername(),tableScreen.getSecretAppList());
            tableScreen.getSecretAppList().remove(idx);

            PreparedStatement pst = connection.prepareStatement("CALL deleteApp(\"" + userID + "\",\""+ app.getAppID() +"\")");
            pst.executeUpdate();
            pst.close();

            connection.close();
            tableScreen.update();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
    }


}
