package service.auth;

import dto.response.BaseItemResponse;

public interface ItemCallback {
    void onSuccess(BaseItemResponse response);

    void onError(String message);
}