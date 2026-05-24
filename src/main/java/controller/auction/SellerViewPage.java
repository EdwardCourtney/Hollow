package controller.auction;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.response.BaseResponse;
import model.response.BidResponse;
import model.response.BidPageResponse;
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
import java.util.Comparator;
import java.util.Locale;

public class SellerViewPage {
    @FXML private Label description;

    private final NumberFormat moneyFormat = NumberFormat.getNumberInstance(Locale.US);
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Label titleLabel;
    private LineChart<String, Number> bidChart;
    private Label currentPriceLabel;
    private Label highestBidderLabel;
    private Label startingPriceLabel;
    private Label bidIncrementLabel;
    private Label startTimeLabel;
    private Label endTimeLabel;
    private Label editHintLabel;
    private Label endHintLabel;
    private Button endButton;

    private ItemResponse item;

    public void initialize() {
        moneyFormat.setMaximumFractionDigits(2);
        bindControls();
        item = AuctionViewContext.selectedItem;

        if (item == null || item.itemId == null) {
            setHint("No auction selected.");
            endButton.setDisable(true);
            return;
        }

        renderItem(item);
        loadStatus(item.itemId);
        loadBidHistory(item.itemId);
    }

    private void bindControls() {
        GridPane root = (GridPane) description.getParent().getParent();

        VBox itemBox = (VBox) getNode(root, 0, 1);
        titleLabel = (Label) itemBox.getChildren().get(0);

        VBox chartBox = (VBox) getNode(root, 1, 1);
        bidChart = (LineChart<String, Number>) chartBox.getChildren().get(1);
        bidChart.setLegendVisible(false);

        VBox statusBox = (VBox) getNode(root, 0, 2);
        currentPriceLabel = (Label) statusBox.getChildren().get(0);
        highestBidderLabel = (Label) statusBox.getChildren().get(1);
        startingPriceLabel = (Label) statusBox.getChildren().get(2);
        bidIncrementLabel = (Label) statusBox.getChildren().get(3);
        startTimeLabel = (Label) statusBox.getChildren().get(4);
        endTimeLabel = (Label) statusBox.getChildren().get(5);

        VBox actionBox = (VBox) getNode(root, 1, 2);
        editHintLabel = (Label) actionBox.getChildren().get(0);
        ((Button) actionBox.getChildren().get(1)).setOnAction(event -> setHint("Edit auction is not supported by the backend yet."));
        endHintLabel = (Label) actionBox.getChildren().get(2);
        endButton = (Button) actionBox.getChildren().get(3);
        endButton.setOnAction(event -> cancelAuction());

        VBox backBox = (VBox) getNode(root, 0, 0);
        ((Button) backBox.getChildren().get(0)).setOnAction(event -> back());
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

    private void loadStatus(Long itemId) {
        ApiClient.api.getItemStatus(itemId).enqueue(new Callback<ItemStatusGetResponse>() {
            @Override
            public void onResponse(Call<ItemStatusGetResponse> call, Response<ItemStatusGetResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().itemStatus == null) {
                    Platform.runLater(() -> setHint("Cannot load auction status. HTTP " + response.code()));
                    return;
                }

                Platform.runLater(() -> renderStatus(response.body().itemStatus));
            }

            @Override
            public void onFailure(Call<ItemStatusGetResponse> call, Throwable throwable) {
                Platform.runLater(() -> setHint("Network error: " + throwable.getMessage()));
            }
        });
    }

    private void loadBidHistory(Long itemId) {
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            setHint("Bid history requires login.");
            return;
        }

        ApiClient.api.getItemBids("Bearer " + TokenStorage.accessToken, itemId, 0, 20).enqueue(new Callback<BidPageResponse>() {
            @Override
            public void onResponse(Call<BidPageResponse> call, Response<BidPageResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().entity == null) {
                    Platform.runLater(() -> setHint("Bid history unavailable. HTTP " + response.code()));
                    return;
                }

                Platform.runLater(() -> renderBidHistory(response.body()));
            }

            @Override
            public void onFailure(Call<BidPageResponse> call, Throwable throwable) {
                Platform.runLater(() -> setHint("Bid history unavailable: " + throwable.getMessage()));
            }
        });
    }

    private void cancelAuction() {
        if (item == null || item.itemId == null) {
            setHint("No auction selected.");
            return;
        }
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            setHint("You must login before ending an auction.");
            return;
        }

        endButton.setDisable(true);
        ApiClient.api.cancelItem("Bearer " + TokenStorage.accessToken, item.itemId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Platform.runLater(() -> {
                    endButton.setDisable(false);
                    if (!response.isSuccessful() || response.body() == null) {
                        setHint("End auction failed. HTTP " + response.code());
                        return;
                    }
                    setHint(valueOrFallback(response.body().message, "Auction ended."));
                    loadStatus(item.itemId);
                });
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                Platform.runLater(() -> {
                    endButton.setDisable(false);
                    setHint("Network error: " + throwable.getMessage());
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
        currentPriceLabel.setText("Current price: " + formatMoney(status.currentPrice));
        highestBidderLabel.setText("Highest bidder: " + valueOrFallback(status.highestBidUser, "-"));
        startingPriceLabel.setText("Starting price: " + formatMoney(status.startingPrice));
        bidIncrementLabel.setText("Bid increment: " + formatMoney(status.bidIncrement));
        startTimeLabel.setText("Start time: " + formatTime(status.startTime));
        endTimeLabel.setText("End time: " + formatTime(status.endTime));
    }

    private void renderBidHistory(BidPageResponse response) {
        bidChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        if (response.entity.content != null) {
            response.entity.content.stream()
                    .sorted(Comparator.comparing(bid -> bid.time == null ? 0L : bid.time))
                    .forEach(bid -> series.getData().add(new XYChart.Data<>(formatTime(bid.time), bid.bidAmount)));
        }
        bidChart.getData().add(series);
        if (series.getData().isEmpty()) {
            setHint("No bid history yet.");
        }
    }

    private void setHint(String message) {
        editHintLabel.setText(message);
        endHintLabel.setText(message);
    }

    private void back() {
        try {
            SceneManager.changeContent("/fxml/mySaleTab.fxml");
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
