package com.example.passwordmanager.DB;

public class App
{
    private int iterator;
    private int appID;
    private String appUsername;
    private String appName;
    private String appPassword;


    public App(int iterator,int appID,String appName,String appUsername, String appPassword)
    {
        this.appID = appID;
        this.appUsername = appUsername;
        this.iterator = iterator;
        this.appName = appName;
        this.appPassword = appPassword;
    }
    public App(int appID,String appName, String appUsername, String appPassword)
    {
        this.appID = appID;
        this.appUsername = appUsername;
        this.appName = appName;
        this.appPassword = appPassword;
    }
    public String getAppName() {
        return appName;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public void setAppUsername(String appUsername) {
        this.appUsername = appUsername;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public int getAppID() {
        return appID;
    }

    public String getAppUsername() {
        return appUsername;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    @Override
    public String toString() {
        return "realID: "+getAppID()+"id: "+ getIterator() +" AppName: "+getAppName() + " appPassword: "+getAppPassword()+"\n";
    }


}
