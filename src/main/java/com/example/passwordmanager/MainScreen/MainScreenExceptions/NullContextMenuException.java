package com.example.passwordmanager.MainScreen.MainScreenExceptions;

public class NullContextMenuException extends Exception
{
    public NullContextMenuException() {
        super("You should not click empty area!");
    }
}
