package service.auth;

import dto.response.AuthResponse;

public interface LoginCallback {
    void onSuccess(AuthResponse response);

    void onError(String message);
}
