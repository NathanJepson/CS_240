package RequestQ;


/**
 * This is a request to create a new user account
 */
public class RegisterRequest
{
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     *
     * @param userName unique identifier for new user
     * @param password authenticating string for the new user
     * @param email Email address for new user
     * @param firstName First name of new user
     * @param lastName Last name of new user
     * @param gender 'M' or 'F' identifying the gender of the user
     */
    public RegisterRequest(String userName, String password, String email, String firstName,
                           String lastName, String gender)
    {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
