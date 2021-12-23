package Handler;

public class ClientError extends Exception {

    private String message;

    public ClientError(){}
    public ClientError(String msg) {
        this.message =  msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
