package model.request;

public class BidPostRequest {
    public Long itemId;
    public Double bidAmount;

    public BidPostRequest(Long itemId, Double bidAmount) {
        this.itemId = itemId;
        this.bidAmount = bidAmount;
    }
}
