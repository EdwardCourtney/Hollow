package service.auth;

import dto.auth.AuthResponse;

public interface LoginCallback {
    void onSuccess(AuthResponse response);

    void onError(String message);
}
