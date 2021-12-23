package Response;

/**
 * A class signifying whether a fill request was completed or not (i.e., certain persons and events were added
 * to the database)
 */
public class FillResponse
{
    private boolean success;
    private String message;

    /**
     *
     * @param message A message signifying that a successful number of persons and events were added. Or, it will signify
     *                failure (with an error message)
     * @param success A boolean value signifying whether the request succeeded or not
     */
    public FillResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}
