package models;

public class Client {
    
    private String username;

    public Client(String user) {
        this.username = user;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String user) {
        this.username = user;
    }
}
