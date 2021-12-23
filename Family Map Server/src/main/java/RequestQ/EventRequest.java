package RequestQ;

public class EventRequest {

    private String authToken;
    private String eventID;

    public EventRequest (String authToken) {
        this.authToken = authToken;
    }

    public EventRequest (String authToken, String eventID) {
        this.authToken = authToken;
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}

