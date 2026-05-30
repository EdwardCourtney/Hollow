package service.auction;

import dto.auction.BaseItemResponse;

public interface ItemCallback {
    void onSuccess(BaseItemResponse response);

    void onError(String message);
}