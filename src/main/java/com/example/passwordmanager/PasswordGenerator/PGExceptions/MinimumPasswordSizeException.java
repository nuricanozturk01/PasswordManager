package com.example.passwordmanager.PasswordGenerator.PGExceptions;

public class MinimumPasswordSizeException extends Exception
{
    public MinimumPasswordSizeException(int size)
    {
        super("Minimum password size must be higher than "+size+"!");
    }
}
