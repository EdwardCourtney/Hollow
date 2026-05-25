package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import dto.response.ItemResponse;

public class Card {
    @FXML private Label itemLabel;
    @FXML private Label priceLabel;
    @FXML private Label timeLabel;
    @FXML private Label infoLabel;


    public void setItem(ItemResponse item) {
        itemLabel.setText("Item: " + item.title);
        infoLabel.setText("Info: " + item.description);
        priceLabel.setText("Price: loading...");
        timeLabel.setText("Time: loading...");
    }
}
