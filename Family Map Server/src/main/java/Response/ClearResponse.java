package Response;

/**
 * The response to clear the database can either return 'success = true' or 'success=false' but in either case
 * the message will say what happened (whether the request was fulfilled, or why not).
 */
public class ClearResponse
{
    private boolean success;
    private String message;

    /**
     *
     * @param message A string identifying what happened (e.g., "Clear succeeded" or "error:....")
     * @param success a boolean identifying "false" or "true" with regards to whether the request was
     *                successful
     */
    public ClearResponse(String message, boolean success) {
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
