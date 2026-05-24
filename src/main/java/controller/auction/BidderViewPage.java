package controller.auction;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.request.BidPostRequest;
import model.response.BaseItemResponse;
import model.response.BidPostResponse;
import model.response.ItemResponse;
import model.response.ItemStatusGetResponse;
import model.response.ItemStatusResponse;
import network.ApiClient;
import network.TokenStorage;
import controller.navigation.SceneManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BidderViewPage {
    @FXML private Label description;

    private final NumberFormat moneyFormat = NumberFormat.getNumberInstance(Locale.US);
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Label titleLabel;
    private Label currentPriceLabel;
    private Label highestBidderLabel;
    private Label startingPriceLabel;
    private Label bidIncrementLabel;
    private Label startTimeLabel;
    private Label endTimeLabel;
    private Label lastBidLabel;
    private TextField newBidField;
    private Button confirmButton;

    private ItemResponse item;
    private ItemStatusResponse status;

    public void initialize() {
        moneyFormat.setMaximumFractionDigits(2);
        bindControls();
        item = AuctionViewContext.selectedItem;

        if (item == null || item.itemId == null) {
            showPlaceholder("No auction selected.");
            setBidControlsDisabled(true);
            return;
        }

        renderItem(item);
        setBidControlsDisabled(true);
        loadItem(item.itemId);
        loadStatus(item.itemId);
    }

    private void bindControls() {
        GridPane root = (GridPane) description.getParent().getParent();

        VBox itemBox = (VBox) getNode(root, 0, 1);
        titleLabel = (Label) itemBox.getChildren().get(0);

        VBox autoBidBox = (VBox) getNode(root, 1, 1);
        ((CheckBox) autoBidBox.getChildren().get(1)).setOnAction(event -> showPlaceholder("Auto-bidding is not supported by the backend yet."));
        ((TextField) autoBidBox.getChildren().get(3)).setDisable(true);
        ((TextField) autoBidBox.getChildren().get(5)).setDisable(true);

        VBox statusBox = (VBox) getNode(root, 0, 2);
        currentPriceLabel = (Label) statusBox.getChildren().get(0);
        highestBidderLabel = (Label) statusBox.getChildren().get(1);
        startingPriceLabel = (Label) statusBox.getChildren().get(2);
        bidIncrementLabel = (Label) statusBox.getChildren().get(3);
        startTimeLabel = (Label) statusBox.getChildren().get(4);
        endTimeLabel = (Label) statusBox.getChildren().get(5);

        VBox bidBox = (VBox) getNode(root, 1, 2);
        lastBidLabel = (Label) bidBox.getChildren().get(0);
        newBidField = (TextField) bidBox.getChildren().get(2);
        confirmButton = (Button) bidBox.getChildren().get(3);
        confirmButton.setOnAction(event -> submitBid());

        VBox backBox = (VBox) getNode(root, 0, 0);
        ((Button) backBox.getChildren().get(0)).setOnAction(event -> back());

        VBox watchlistBox = (VBox) getNode(root, 1, 0);
        ((Button) watchlistBox.getChildren().get(0)).setOnAction(event -> showPlaceholder("Watchlist is not supported by the backend yet."));
    }

    private Node getNode(GridPane root, int column, int row) {
        for (Node node : root.getChildren()) {
            Integer nodeColumn = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if ((nodeColumn == null ? 0 : nodeColumn) == column && (nodeRow == null ? 0 : nodeRow) == row) {
                return node;
            }
        }
        throw new IllegalStateException("Cannot find node at " + column + "," + row);
    }

    private void loadItem(Long itemId) {
        ApiClient.api.getItem(itemId).enqueue(new Callback<BaseItemResponse>() {
            @Override
            public void onResponse(Call<BaseItemResponse> call, Response<BaseItemResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().item != null) {
                    Platform.runLater(() -> renderItem(response.body().item));
                }
            }

            @Override
            public void onFailure(Call<BaseItemResponse> call, Throwable throwable) {
                Platform.runLater(() -> showPlaceholder("Cannot refresh item: " + throwable.getMessage()));
            }
        });
    }

    private void loadStatus(Long itemId) {
        ApiClient.api.getItemStatus(itemId).enqueue(new Callback<ItemStatusGetResponse>() {
            @Override
            public void onResponse(Call<ItemStatusGetResponse> call, Response<ItemStatusGetResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().itemStatus == null) {
                    Platform.runLater(() -> showPlaceholder("Cannot load auction status. HTTP " + response.code()));
                    return;
                }

                Platform.runLater(() -> renderStatus(response.body().itemStatus));
            }

            @Override
            public void onFailure(Call<ItemStatusGetResponse> call, Throwable throwable) {
                Platform.runLater(() -> showPlaceholder("Network error: " + throwable.getMessage()));
            }
        });
    }

    private void submitBid() {
        if (item == null || item.itemId == null) {
            showPlaceholder("No auction selected.");
            return;
        }
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            showPlaceholder("You must login before bidding.");
            return;
        }

        double bidAmount;
        try {
            bidAmount = Double.parseDouble(newBidField.getText());
        } catch (NumberFormatException exception) {
            showPlaceholder("Your new bid must be a valid number.");
            return;
        }

        if (bidAmount <= 0) {
            showPlaceholder("Your new bid must be positive.");
            return;
        }

        confirmButton.setDisable(true);
        ApiClient.api.makeBid("Bearer " + TokenStorage.accessToken, new BidPostRequest(item.itemId, bidAmount))
                .enqueue(new Callback<BidPostResponse>() {
                    @Override
                    public void onResponse(Call<BidPostResponse> call, Response<BidPostResponse> response) {
                        Platform.runLater(() -> {
                            confirmButton.setDisable(false);
                            if (!response.isSuccessful() || response.body() == null) {
                                showPlaceholder("Bid failed. HTTP " + response.code());
                                return;
                            }
                            lastBidLabel.setText("Your last bid: " + formatMoney(bidAmount));
                            newBidField.clear();
                            showPlaceholder(valueOrFallback(response.body().message, "Bid submitted."));
                            loadStatus(item.itemId);
                        });
                    }

                    @Override
                    public void onFailure(Call<BidPostResponse> call, Throwable throwable) {
                        Platform.runLater(() -> {
                            confirmButton.setDisable(false);
                            showPlaceholder("Network error: " + throwable.getMessage());
                        });
                    }
                });
    }

    private void renderItem(ItemResponse item) {
        this.item = item;
        titleLabel.setText(valueOrFallback(item.title, "Untitled"));
        description.setText(valueOrFallback(item.description, "Description"));
    }

    private void renderStatus(ItemStatusResponse status) {
        this.status = status;
        currentPriceLabel.setText("Current price: " + formatMoney(status.currentPrice));
        highestBidderLabel.setText("Highest bidder: " + valueOrFallback(status.highestBidUser, "-"));
        startingPriceLabel.setText("Starting price: " + formatMoney(status.startingPrice));
        bidIncrementLabel.setText("Bid increment: " + formatMoney(status.bidIncrement));
        startTimeLabel.setText("Start time: " + formatTime(status.startTime));
        endTimeLabel.setText("End time: " + formatTime(status.endTime));
        setBidControlsDisabled(!"ACTIVE".equalsIgnoreCase(valueOrFallback(status.itemStatus, "")));
    }

    private void setBidControlsDisabled(boolean disabled) {
        newBidField.setDisable(disabled);
        confirmButton.setDisable(disabled);
    }

    private void showPlaceholder(String message) {
        lastBidLabel.setText("Your last bid: " + message);
    }

    private void back() {
        try {
            SceneManager.changeContent("/fxml/browseAuctionTab.fxml");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String formatMoney(Double value) {
        return value == null ? "-" : moneyFormat.format(value);
    }

    private String formatTime(Long value) {
        if (value == null) {
            return "-";
        }
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).format(timeFormat);
    }

    private String valueOrFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
