package com.example.familymapclient.Response;

/**
 * Once a user has logged in, the response will return the relevant AuthToken, and other information about the person...
 * ...or it will fail, returning an error message
 */
public class LoginResponse
{
    private String authToken;
    private String userName;
    private String personID;
    private boolean success;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    /**
     * This function handels when the login response is successful
     * @param authToken The authtoken (a user can have several)
     * @param userName The associated username for this login-attempt
     * @param personID The associated personID which uniquely identifies the person
     * @param success The boolean signifying that the request was successful
     */
    public LoginResponse(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    /**
     * This function handles when the login-request fails
     * @param message This will be a String identifying an error message
     * @param success This will be set to 'false' if this constructor is used
     */
    public LoginResponse(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
