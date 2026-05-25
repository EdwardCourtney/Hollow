package dto.response;

public class BidPostResponse extends BaseResponse {
    public BidResponse bid;

    public BidPostResponse() {
    }

    public static class BidResponse {
        public Long bidId;
        public Double bidAmount;
        public Long time;
    }
}
