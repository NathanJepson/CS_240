package Response;

import Model.Person;

/**
 * This class will handle when a person request (for multiple persons) is successful or whether it has failed
 */
public class PersonResponse
{
    private Person[] data;
    private String message;
    private boolean success;

    /**
     * This function handles when a person request is successful
     * @param data all of person data will be put as an array in this object
     * @param success This boolean will signify 'true' when this constructor is used
     */
    public PersonResponse(Person[] data, boolean success)
    {
        this.data = data;
        this.success = success;
    }

    /**
     * This function handles when a person request has failed
     * @param message This will give an error message when this constructor is used
     * @param success This will signify 'false' when this constructor is used
     */
    public PersonResponse(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
