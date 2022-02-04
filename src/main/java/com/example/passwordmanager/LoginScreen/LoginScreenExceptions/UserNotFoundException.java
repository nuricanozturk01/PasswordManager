package com.example.passwordmanager.LoginScreen.LoginScreenExceptions;

public class UserNotFoundException extends Exception
{
    public UserNotFoundException()
    {
        super("Not Matched! Please check the information or Sign Up!");
    }
}
