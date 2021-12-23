package Model;

/**
 * This class mirrors the person table in the SQLite database.
 * As it stands, the username is optional (since a person, such as a father or mother, doesn't have to be a
 * user of our program).
 * Gender is binary in the parallel person table (suck it, libs)
 * And having a known father, mother, or spouse is optional (unless you are pro-arranged marriage. Which,
 * you know, is cool and all, and I don't judge).
 */
public class Person
{

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     *
     * @param personID the unique identifier for the person object
     * @param associatedUsername the associated username (account) with which this person is associated with
     * @param firstName first name of person
     * @param lastName last name of person
     * @param gender character signifying 'F' or 'M'
     * @param fatherID optional identifier for a known father
     * @param motherID optional identifier for a known mother
     * @param spouseID optional identifier for the relevant person's honey
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID)
    {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonId()
    {
        return personID;
    }

    public void setPersonId(String personId)
    {
        this.personID = personId;
    }

    public String getAssociatedUsername()
    {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername)
    {
        this.associatedUsername = associatedUsername;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getFatherID()
    {
        return fatherID;
    }

    public void setFatherID(String fatherID)
    {
        this.fatherID = fatherID;
    }

    public String getMotherID()
    {
        return motherID;
    }

    public void setMotherID(String motherID)
    {
        this.motherID = motherID;
    }

    public String getSpouseID()
    {
        return spouseID;
    }

    public void setSpouseID(String spouseID)
    {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Person cast = (Person)obj;

        return (this.getPersonId().equals(cast.getPersonId()) && this.getAssociatedUsername().equals(cast.getAssociatedUsername())
        && this.getFirstName().equals(cast.getFirstName()) && this.getLastName().equals(cast.getLastName())
        && this.getGender().equals(cast.getGender()));
    }
}
