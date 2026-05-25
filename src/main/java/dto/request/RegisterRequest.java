package dto.request;

public class RegisterRequest {
    public String username;
    public String displayName;
    public String password;

    public RegisterRequest(String username, String displayName, String password){
        this.username = username;
        this.displayName = displayName;
        this.password = password;
    }
}
