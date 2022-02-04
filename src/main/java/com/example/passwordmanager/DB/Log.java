package com.example.passwordmanager.DB;

public class Log
{

    private String date;
    private String verify;


    public Log(String date, String verify)
    {
        this.date = date;
        this.verify = verify;
    }



    public String getDate()
    {
        return date;
    }


    public String getVerify() {
        return verify;
    }


    @Override
    public String toString()
    {
        return " date: "+getDate()+" : "+getVerify() ;
    }
}
