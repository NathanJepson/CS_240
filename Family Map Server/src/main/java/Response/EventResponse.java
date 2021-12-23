package Response;

import Model.Event;

/**
 * A class identifying whether a particular event was able to be grabbed from the database. Either the response
 * indicated success or failure
 */
public class EventResponse
{
    private Event[] data;
    private String message;
    private boolean success;

    /**
     *This function handles when the request succeeded
     * @param data Where data is an array of all event objects
     * @param success Where success is a boolean signifying whether the event request was successful or not (in this case, yes)
     */
    public EventResponse(Event[] data, boolean success)
    {
        this.data = data;
        this.success = success;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
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

    /**
     * This function handles when the request fails
     * @param message Where the message is an error message
     * @param success Where this variable will be 'false' signifying that the request failed
     */
    public EventResponse(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }




}
