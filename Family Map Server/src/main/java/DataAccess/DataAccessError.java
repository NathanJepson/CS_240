package DataAccess;

public class DataAccessError extends Exception {
    DataAccessError(String message)
    {
        super(message);
    }

    DataAccessError()
    { super(); }
}
