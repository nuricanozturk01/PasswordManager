package com.example.passwordmanager.DB;

public class User
{
    private int userID;

    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;

    public User(int userID, String name, String surname, String username, String password, String email)
    {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int userID, String username, String password)
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID()
    {
        return userID;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public String toString()
    {
        return "ID: "+getUserID()+" Name: "+getName()+" Surname: "+getSurname()+" Username: "+getUsername()+
                " Password: "+getPassword()+ " Email: "+getEmail()+"\n";
    }

}
