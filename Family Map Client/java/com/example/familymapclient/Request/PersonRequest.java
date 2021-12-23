package com.example.familymapclient.Request;

public class PersonRequest {
    private String authToken;
    private String personID;


    public PersonRequest(String authToken) {
        this.authToken = authToken;
    }

    public PersonRequest(String authToken, String personID) {
        this.authToken = authToken;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
