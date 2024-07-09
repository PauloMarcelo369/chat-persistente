package entitys;

public class UserEntity {
    private String username;

    public UserEntity(String user) {
        this.username = user;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String user) {
        this.username = user;
    }
}
