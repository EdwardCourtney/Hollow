package service.auction;

import dto.auction.BidPostResponse;

public interface BidCallback {
    void onSuccess(BidPostResponse response);

    void onError(String message);
}
