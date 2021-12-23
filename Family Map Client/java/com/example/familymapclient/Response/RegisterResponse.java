package com.example.familymapclient.Response;

/**
 * This class handles the response for a register request (meaning that a user account was either created
 * or it was failed to be created)
 */
public class RegisterResponse
{
    private String authToken;
    private String userName;
    private String personID;
    private boolean success;
    private String message;

    /**
     * This function / constructor handles when the account was able to be created successfully
     * @param authToken Unique authtoken for the current user session (which is created when the account is created)
     * @param userName Unique username for the user that was just created
     * @param personID Unique person identifier for the associated person attached to the just-created-account
     * @param success A boolean which will always signify 'true' when this constructor is called
     */
    public RegisterResponse(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    /**
     * This constructor handles when a Register Request has failed
     * @param message An error message (String type)
     * @param success A boolean which will always signify 'false' when this constructor is used
     */
    public RegisterResponse(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }

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
}
