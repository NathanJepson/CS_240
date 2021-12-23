package RequestQ;

public class FillRequest {
    private String userName;
    private int generations;

    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }
    public FillRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
