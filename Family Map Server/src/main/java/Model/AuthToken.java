package Model;

/**
 *The AuthToken class will be used to associate relationships between unique session IDs (or authentication tokens)
 * with the respective persons that they are attached to (a user can have more than one AuthToken associated with
 * them
 */
public class AuthToken
{
    /**
     * A token which is a unique string; used as a session ID for the user (a user can have more than one)
     */
    private String theToken;
    /**
     * A string which is the identifier of the person that this AuthToken is attached to
     */
    private String userName;

    /**
     *
     * @param thisToken A string signifying the unique token ID (which will be a session ID for a user)
     * @param thisUserName A string signifying the unique identifier for this person
     */
    public AuthToken(String thisToken, String thisUserName)
    {
        this.theToken = thisToken;
        this.userName = thisUserName;
    }

    public void setTheToken(String theToken)
    {
        this.theToken = theToken;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getTheToken()
    {
        return theToken;
    }

    public String getUserName()
    {
        return userName;
    }

    /**
     *
     * @param obj (passed in object, to check whether it is equal to this AuthToken)
     * @return boolean signifying whether the passed in AuthToken equals this one
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        AuthToken thisCast = (AuthToken)obj;

        if (!this.userName.equals(thisCast.getUserName()) || !this.theToken.equals(thisCast.getTheToken()))
        {
            return false;
        }
        return true;
    }

}
