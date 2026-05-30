package service.auction;

import dto.auction.ItemStatusResponse;

public interface ItemStatusCallback {
    void onSuccess(ItemStatusResponse response);

    void onError(String message);
}
