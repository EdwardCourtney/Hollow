package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import dto.response.ItemResponse;

import java.io.IOException;

public class Card {
    @FXML private Label itemLabel;
    @FXML private Label priceLabel;
    @FXML private Label timeLabel;
    @FXML private Label infoLabel;

    private ItemResponse item;

    public void setItem(ItemResponse item) {
        this.item = item;
        itemLabel.setText("Item: " + item.title);
        infoLabel.setText("Info: " + item.description);
        priceLabel.setText("Price: loading...");
        timeLabel.setText("Time: loading...");
    }

    @FXML
    public void seeDetails() {
        if (item == null) {
            AppPopup.error("Missing item");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bidderViewPage.fxml"));
            Parent view = loader.load();

            BidderViewPage controller = loader.getController();
            controller.setItem(item);

            SceneManager.changeContent(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
