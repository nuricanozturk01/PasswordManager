package com.example.passwordmanager.LoginScreen.LoginScreenExceptions;

public class NullFieldException extends Exception
{
    public NullFieldException()
    {
        super("Please fill all fields!");
    }
}
