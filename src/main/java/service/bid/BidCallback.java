package service.bid;

import dto.response.BidPostResponse;

public interface BidCallback {
    void onSuccess(BidPostResponse response);

    void onError(String message);
}
