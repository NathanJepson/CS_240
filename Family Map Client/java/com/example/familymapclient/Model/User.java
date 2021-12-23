package com.example.familymapclient.Model;

/**
 * This absolutely magical class mirrors the user table in the SQLite database.
 * Every user must be attached to a person (through the personID, which is a foreign key in the user table).
 * Gender is mandatory (no one can be undecided...)
 *Email is pretty straightforward.
 * Passwords are in plaintext for now (and the security gods looked down in shame, utter shame)
 */
public class User
{
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     *
     * @param userName the unique identifier (almost like a net id) for a user
     * @param password the authentication string that users type in to access the relevant service
     * @param email A string identifying the user email
     * @param firstName A string identifying the first name
     * @param lastName A string identifying the last name
     * @param gender A string identifying the gender of the user
     * @param personID The associated personID referencing a person object (or a row in the person table)
     */
    public User(String userName, String password, String email, String firstName,
                String lastName, String gender, String personID)
    {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername()
    {
        return userName;
    }

    public void setUsername(String username)
    {
        this.userName = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getPersonId()
    {
        return personID;
    }

    public void setPersonID(String personID)
    {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        User cast = (User)obj;
        return ( cast.getUsername().equals(this.getUsername()) && (cast.getGender().equals(this.gender))
        && (cast.getPassword().equals(this.getPassword())) && (cast.getEmail().equals(this.getEmail()))
        && (cast.getFirstName().equals(this.getFirstName()))  && cast.getLastName().equals(this.getLastName())
        && cast.getPersonId().equals(this.getPersonId()));
    }
}
