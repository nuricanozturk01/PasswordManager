package com.example.passwordmanager.LoginScreen.LoginScreenExceptions;

public class PasswordNotMatchedException extends Exception
{
    public PasswordNotMatchedException() {
        super("Please control the passwords!");
    }
}
