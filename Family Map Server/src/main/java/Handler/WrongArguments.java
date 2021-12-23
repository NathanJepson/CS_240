package Handler;

public class WrongArguments extends Exception {
    public WrongArguments() {
        super();
    }
    public WrongArguments(String str) {
        super(str);
    }
}
