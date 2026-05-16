package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import model.AuctionItem;

import java.io.IOException;
import java.util.List;

public class BrowseAuctionController {
    @FXML
    private VBox auctionList;

    @FXML
    public void initialize() throws IOException {
        List<AuctionItem> mockAuctions = List.of(
                new AuctionItem("Gaming Laptop", 1250.0, "01:45:10", 12, "Alex"),
                new AuctionItem("Mechanical Keyboard", 145.0, "00:20:33", 7, "Mia"),
                new AuctionItem("Sony Headphones", 210.0, "03:12:05", 4, "Noah"),
                new AuctionItem("Vintage Camera", 540.0, "05:08:14", 9, "Emma"),
                new AuctionItem("iPad Air", 680.0, "02:30:41", 6, "Liam"),
                new AuctionItem("LEGO Starship Set", 320.0, "07:55:29", 14, "Sophia")
        );

        for (AuctionItem auctionItem : mockAuctions) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auctionCard.fxml"));
            Parent cardRoot = loader.load();

            CardController cardController = loader.getController();
            cardController.setAuctionItem(auctionItem);

            auctionList.getChildren().add(cardRoot);
        }
    }
}
