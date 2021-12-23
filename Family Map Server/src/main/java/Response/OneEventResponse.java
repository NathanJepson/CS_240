package Response;

/**
 * This class identifies the response for a one-event request. When successful, it returns all the information in
 * an Event object
 */
public class OneEventResponse
{
    private String associatedUsername;

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    private String eventID;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;
    private boolean success;
    private String message;

    /**
     * This function will be used when the request is successful
     * @param associatedUsername Username attached to the event
     * @param eventID Unique identifier of this event
     * @param personID Unique identifier of the person attached to the event
     * @param latitude latitude of the point where this event happened
     * @param longitude longitude of the point where this event happened
     * @param country country where the event happened
     * @param city city where the event happened
     * @param eventType Event type could be 'baptism','death', or 'reaching the age of 24.92'
     * @param year An int signifying when the event happened
     * @param success This boolean will signify that the request was successful
     */
    public OneEventResponse(String associatedUsername, String eventID, String personID, double latitude,
                            double longitude, String country, String city, String eventType, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    public OneEventResponse(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
