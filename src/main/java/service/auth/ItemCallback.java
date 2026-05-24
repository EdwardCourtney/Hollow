package service.auth;

import model.response.BaseItemResponse;

public interface ItemCallback {
    void onSuccess(BaseItemResponse response);

    void onError(String message);
}