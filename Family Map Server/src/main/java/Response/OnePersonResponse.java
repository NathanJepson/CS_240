package Response;

/**
 * This class will give all the information that a person object can have, depending on whether the one person
 * request was successful (if not, an error message will be given)
 */
public class OnePersonResponse
{
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private char gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private boolean success;
    private String message;

    /**
     * This function handles whether the one-person request is successful
     * @param associatedUsername The associated username of this person
     * @param personID The unique identifier for this person
     * @param firstName The first name of this person
     * @param lastName The last name of this person
     * @param gender This will identify the gender of the person
     */
    public OnePersonResponse(String associatedUsername, String personID,
                             String firstName, String lastName, char gender) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     *This function handles when the one-person request fails
     * @param Success This boolean will be set to 'false' when this constructor is used
     * @param message This message gives the error message of where the person request went wrong
     */
    public OnePersonResponse(boolean Success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
