package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.AuctionItem;

import java.io.IOException;

public class BidController {
    @FXML
    private Label auctionNameLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label currentBidLabel;

    @FXML
    private Label highestBidderLabel;

    @FXML
    private Label totalBidsLabel;

    @FXML
    private Label yourLatestBidLabel;

    @FXML
    private Label bidStatusLabel;

    @FXML
    private TextField newBidField;

    private AuctionItem auctionItem;
    private Double yourLatestBid;

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
        refreshView();
    }

    @FXML
    public void placeBid() {
        if (auctionItem == null) {
            return;
        }

        String rawBid = newBidField.getText().trim();
        if (rawBid.isEmpty()) {
            bidStatusLabel.setText("Enter a bid amount first.");
            return;
        }

        double newBid;
        try {
            newBid = Double.parseDouble(rawBid);
        } catch (NumberFormatException exception) {
            bidStatusLabel.setText("Bid amount must be a valid number.");
            return;
        }

        if (newBid <= auctionItem.getCurrentBid()) {
            bidStatusLabel.setText("New bid must be higher than the current bid.");
            return;
        }

        auctionItem.placeBid(newBid, "You");
        yourLatestBid = newBid;
        newBidField.clear();
        bidStatusLabel.setText("Mock bid placed successfully.");
        refreshView();
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        FrameController.getInstance().showBrowseAuction(event);
    }

    private void refreshView() {
        auctionNameLabel.setText(auctionItem.getName());
        timeLabel.setText(auctionItem.getTimeLeft());
        currentBidLabel.setText(String.format("$%.2f", auctionItem.getCurrentBid()));
        highestBidderLabel.setText(auctionItem.getHighestBidder());
        totalBidsLabel.setText(String.valueOf(auctionItem.getTotalBids()));
        if (yourLatestBid == null) {
            yourLatestBidLabel.setText("No bids yet");
        } else {
            yourLatestBidLabel.setText(String.format("$%.2f", yourLatestBid));
        }
    }
}
