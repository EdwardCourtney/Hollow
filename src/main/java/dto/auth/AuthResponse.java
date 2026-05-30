package dto.auth;

public class AuthResponse {
    public boolean status;
    public String message;
    public String accessToken;
    public String refreshToken;

    public AuthResponse() {
    }
}
