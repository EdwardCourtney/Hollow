package service;

import model.response.AuthResponse;

public interface AuthCallback {
    void onSuccess(AuthResponse response);

    void onError(String message);
}
