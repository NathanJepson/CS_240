package com.example.familymapclient.Request;

/**
 * This is a log-in request class, containing a username and password
 */
public class LoginRequest
{
    //private String server;
    //private String port;

    private String userName;
    private String password;

    /**
     *
     * @param userName a unique string identifying a user account
     * @param password a not-necessarily unique password which can authenticate the user
     */
    public LoginRequest(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
