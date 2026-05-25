package service.auction;

import dto.auction.GetItemPageResponse;

public interface ItemPageCallback {
    void onSuccess(GetItemPageResponse response);
    void onError(String message);
}
