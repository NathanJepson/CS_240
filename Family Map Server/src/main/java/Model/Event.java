package Model;

/**
 * The Event Class stores information about an event that happened in a person's life. These events are attached
 * to persons via a personID. Each event has a unique Event ID, and has relevant geographic information, as
 * well as what kind of event it is (baptism, death, becoming one with the Babayaga, etc.).
 */
public class Event
{
    private String eventID;
    private String associatedUsername;
    private String personID;
    private double latitude;
    private double longitude;

    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     *
     * @param eventID the unique identifier of the event
     * @param associatedUsername the associated username that the event is tied to
     * @param personID the ID of the person of which the event is for
     * @param latitude latitude of the geographic point of the event
     * @param longitude longitude of the geographic point of the event
     * @param country country where the event happened
     * @param city city where the event happened
     * @param eventType types of events would include 'baptism','death','becoming one with the Baba Yaga',etc.
     * @param year an int signifying the year when it happened, dawg
     */
    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year)
    {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID()
    {
        return eventID;
    }

    public void setEventID(String eventID)
    {
        this.eventID = eventID;
    }

    public String getAssociatedUsername()
    {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername)
    {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID()
    {
        return personID;
    }

    public void setPersonID(String personID)
    {
        this.personID = personID;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(long latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(long longitude)
    {
        this.longitude = longitude;
    }

    public String getCountry()
    {
        return this.country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getEventType()
    {
        return this.eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public int getYear()
    {
        return this.year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Event cast = (Event)obj;
        return (this.getEventID().equals(cast.getEventID()) && this.getAssociatedUsername().equals(cast.getAssociatedUsername())
                && this.getPersonID().equals(cast.getPersonID()) && this.getLatitude() == cast.getLatitude() &&
                this.getLongitude() == cast.getLongitude() && this.getCountry().equals(cast.getCountry()) &&
                this.getCity().equals(cast.getCity()) && this.getEventType().equals(cast.getEventType()) &&
                this.getYear() == cast.getYear());
    }
}
