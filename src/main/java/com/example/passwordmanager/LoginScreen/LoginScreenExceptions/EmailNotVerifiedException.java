package com.example.passwordmanager.LoginScreen.LoginScreenExceptions;

public class EmailNotVerifiedException extends Exception
{
    public EmailNotVerifiedException() {
        super("Email not verified!");
    }
}
