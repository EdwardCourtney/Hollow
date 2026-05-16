package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.AuctionItem;

import java.io.IOException;

public class CardController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label peopleLabel;

    private AuctionItem auctionItem;

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
        nameLabel.setText(auctionItem.getName());
        priceLabel.setText(String.format("Current bid: $%.2f", auctionItem.getCurrentBid()));
        timeLabel.setText("Time left: " + auctionItem.getTimeLeft());
        peopleLabel.setText("Total bids: " + auctionItem.getTotalBids());
    }

    @FXML
    public void bidScreen(ActionEvent event) throws IOException {
        FrameController.getInstance().openBid(auctionItem);
    }
}
