package Response;

/**
 * A class signifying whether a load request (see the LoadRequest class) was successful or not.
 */
public class LoadResponse
{
    private boolean success;
    private String message;

    /**
     *
     * @param message A message signifying whether users, persons, and events were added successfully, or there is
     *                an error message
     * @param success A boolean signifying whether the load was request was 'true'-ly successful or not successful: 'false'.
     */
    public LoadResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
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
