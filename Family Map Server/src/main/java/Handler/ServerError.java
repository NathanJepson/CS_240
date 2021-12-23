package Handler;

public class ServerError extends Exception {
    private String message;

    public ServerError(String msg) {
        this.message = msg;
    }
    public ServerError() {}

    public String getMessage() {
        return this.message;
    }
}
