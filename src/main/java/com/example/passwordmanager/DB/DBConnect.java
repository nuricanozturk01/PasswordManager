package com.example.passwordmanager.DB;
import java.sql.*;

public class DBConnect
{
    private String userName;
    private String password;
    private String host;

    public DBConnect(String userName, String password, String host)
    {
        this.userName = userName;
        this.password = password;
        this.host = host;
    }

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(host , userName , password);
    }
}
