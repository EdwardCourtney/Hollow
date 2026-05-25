package service.auth;

import dto.response.ItemStatusResponse;

public interface ItemStatusCallback {
    void onSuccess(ItemStatusResponse response);

    void onError(String message);
}
