package service.auth;

import dto.response.GetItemPageResponse;

public interface ItemPageCallback {
    void onSuccess(GetItemPageResponse response);
    void onError(String message);
}
