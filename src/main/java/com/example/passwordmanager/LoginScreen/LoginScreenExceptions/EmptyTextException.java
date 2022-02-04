package com.example.passwordmanager.LoginScreen.LoginScreenExceptions;

public class EmptyTextException extends Exception
{
    public EmptyTextException()
    {
        super("Please fill the blanks!");
    }
}
