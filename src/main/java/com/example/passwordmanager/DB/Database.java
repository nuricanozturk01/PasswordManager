package com.example.passwordmanager.DB;

import com.example.passwordmanager.Util.Constants;
import com.example.passwordmanager.Util.Methods;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.SQLException;

public class Database
{
    private static DBConnect db = null;
    private static Connection connection;

    public static Connection connectDatabase()
    {

        try
        {
            db = new DBConnect(Constants.dbUsername, Constants.dbPassword, Constants.dbPath);
            connection = db.getConnection();

        }
        catch (SQLException e)
        {
            Methods.alertScreen(Alert.AlertType.WARNING,"Please Check the Database Connection!", ButtonType.OK);
        }
        return connection;
    }

    public static void main(String[] args) {

            connectDatabase();

    }
}
