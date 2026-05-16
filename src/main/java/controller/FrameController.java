package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.AuctionItem;

import java.io.IOException;

public class FrameController {
    private static FrameController instance;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label sectionTitle;

    @FXML
    public void initialize() throws IOException {
        instance = this;
        showDashboard();
    }

    public static FrameController getInstance() {
        return instance;
    }

    @FXML
    public void showDashboard(ActionEvent event) throws IOException {
        showDashboard();
    }

    @FXML
    public void showBrowseAuction(ActionEvent event) throws IOException {
        sectionTitle.setText("Browse Auction");
        loadContent("/fxml/browseAuction.fxml");
    }

    @FXML
    public void showMySales(ActionEvent event) throws IOException {
        sectionTitle.setText("My Sales");
        loadContent("/fxml/mySales.fxml");
    }

    @FXML
    public void showWatchlist(ActionEvent event) throws IOException {
        sectionTitle.setText("Watchlist");
        loadContent("/fxml/watchlist.fxml");
    }

    public void openBid(AuctionItem auctionItem) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bid.fxml"));
        Node content = loader.load();
        BidController controller = loader.getController();
        controller.setAuctionItem(auctionItem);
        setContent(content);
        sectionTitle.setText("Auction Details");
    }

    private void showDashboard() throws IOException {
        sectionTitle.setText("Dashboard");
        loadContent("/fxml/dashboard.fxml");
    }

    private void loadContent(String resourcePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        Node content = loader.load();
        setContent(content);
    }

    private void setContent(Node content) {
        contentArea.getChildren().setAll(content);
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
    }
}
