package service.auth;

import model.response.GetItemPageResponse;

public interface ItemPageCallback {
    void onSuccess(GetItemPageResponse response);
    void onError(String message);
}
