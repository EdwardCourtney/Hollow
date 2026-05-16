package model;

public class AuctionItem {
    private final String name;
    private double currentBid;
    private String timeLeft;
    private int totalBids;
    private String highestBidder;

    public AuctionItem(String name, double currentBid, String timeLeft, int totalBids, String highestBidder) {
        this.name = name;
        this.currentBid = currentBid;
        this.timeLeft = timeLeft;
        this.totalBids = totalBids;
        this.highestBidder = highestBidder;
    }

    public String getName() {
        return name;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public int getTotalBids() {
        return totalBids;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void placeBid(double newBid, String bidderName) {
        currentBid = newBid;
        highestBidder = bidderName;
        totalBids++;
    }
}
