package com.example.passwordmanager.PasswordGenerator.PGExceptions;

public class CharacterCountNotMatchedException extends Exception
{
    public CharacterCountNotMatchedException()
    {
        super("Lower Count + Upper Count must be equal to Character Count!");
    }
}
