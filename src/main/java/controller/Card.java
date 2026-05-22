package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.response.ItemResponse;

public class Card {
    @FXML private Label itemLabel;
    @FXML private Label priceLabel;
    @FXML private Label timeLabel;
    @FXML private Label infoLabel;
    @FXML private Button viewButton;

    private Long itemId;

    public void initialize() {
        viewButton.setDisable(true);
    }

    public void setItem(ItemResponse item) {
        itemId = item.itemId;
        itemLabel.setText("Item: " + valueOrFallback(item.title, "Untitled")
                + " | ID " + item.itemId
                + " | " + valueOrFallback(item.description, "No description"));
        priceLabel.setText("Price: loading");
        timeLabel.setText("Time: loading");
        infoLabel.setText("Info: loading");
    }

    public void setStatus(String price, String time, String info) {
        priceLabel.setText("Price: " + price);
        timeLabel.setText("Time: " + time);
        infoLabel.setText("Info: " + info);
    }

    private String valueOrFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
