package controller.auction;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.response.ItemPageObjectResponse;
import model.response.ItemResponse;
import model.response.ItemStatusGetResponse;
import model.response.ItemStatusResponse;
import model.response.PageResponse;
import network.ApiClient;
import network.TokenStorage;
import controller.navigation.SceneManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

// Note: Seller tab entry point.
public class MySaleTab {
    private static final int PAGE_SIZE = 10;

    @FXML private VBox root;

    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
    private VBox cardList;
    private Label statusLabel;

    public void initialize() {
        currencyFormat.setMaximumFractionDigits(2);
        buildListingContainer();
        loadListings();
    }

    @FXML public void create() throws IOException {
        SceneManager.changeContent("/fxml/createPage.fxml");
    }

    private void buildListingContainer() {
        statusLabel = new Label("Loading your auctions...");
        cardList = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(cardList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(320);
        root.getChildren().addAll(statusLabel, scrollPane);
    }

    private void loadListings() {
        if (TokenStorage.username == null || TokenStorage.username.isBlank()) {
            statusLabel.setText("Login username unavailable. Please login again.");
            return;
        }

        ApiClient.api.getListings(TokenStorage.username, 0, PAGE_SIZE).enqueue(new Callback<ItemPageObjectResponse>() {
            @Override
            public void onResponse(Call<ItemPageObjectResponse> call, Response<ItemPageObjectResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().entity == null) {
                    Platform.runLater(() -> statusLabel.setText("Cannot load your auctions. HTTP " + response.code()));
                    return;
                }

                Platform.runLater(() -> renderListings(response.body().entity));
            }

            @Override
            public void onFailure(Call<ItemPageObjectResponse> call, Throwable throwable) {
                Platform.runLater(() -> statusLabel.setText("Network error: " + throwable.getMessage()));
            }
        });
    }

    private void renderListings(PageResponse<ItemResponse> page) {
        cardList.getChildren().clear();
        List<ItemResponse> items = page.content;
        if (items == null || items.isEmpty()) {
            statusLabel.setText("No seller auctions found.");
            return;
        }

        statusLabel.setText(page.totalElements + " seller auctions");
        for (ItemResponse item : items) {
            addCard(item);
        }
    }

    private void addCard(ItemResponse item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card.fxml"));
            VBox card = loader.load();
            Card controller = loader.getController();
            controller.setItem(item);
            controller.setViewAction(() -> openSellerView(item));
            cardList.getChildren().add(card);
            loadStatus(item.itemId, controller);
        } catch (IOException exception) {
            statusLabel.setText("Cannot load seller auction card.");
        }
    }

    private void openSellerView(ItemResponse item) {
        try {
            AuctionViewContext.selectedItem = item;
            AuctionViewContext.selectedMode = AuctionViewContext.Mode.SELLER;
            SceneManager.changeContent("/fxml/sellerViewPage.fxml");
        } catch (IOException exception) {
            statusLabel.setText("Cannot open seller view.");
        }
    }

    private void loadStatus(Long itemId, Card card) {
        if (itemId == null) {
            card.setStatus("unavailable", "unavailable", "unavailable");
            return;
        }

        ApiClient.api.getItemStatus(itemId).enqueue(new Callback<ItemStatusGetResponse>() {
            @Override
            public void onResponse(Call<ItemStatusGetResponse> call, Response<ItemStatusGetResponse> response) {
                ItemStatusResponse itemStatus = response.body() == null ? null : response.body().itemStatus;
                Platform.runLater(() -> updateStatusLabels(card, itemStatus));
            }

            @Override
            public void onFailure(Call<ItemStatusGetResponse> call, Throwable throwable) {
                Platform.runLater(() -> updateStatusLabels(card, null));
            }
        });
    }

    private void updateStatusLabels(Card card, ItemStatusResponse status) {
        if (status == null) {
            card.setStatus("unavailable", "unavailable", "unavailable");
            return;
        }

        card.setStatus(
                formatMoney(status.currentPrice),
                formatTimeLeft(status.endTime),
                valueOrFallback(status.itemStatus, "Unknown") + " | Step " + formatMoney(status.bidIncrement)
        );
    }

    private String formatMoney(Double value) {
        return value == null ? "-" : currencyFormat.format(value);
    }

    private String formatTimeLeft(Long endTime) {
        if (endTime == null) {
            return "-";
        }

        long millisLeft = endTime - Instant.now().toEpochMilli();
        if (millisLeft <= 0) {
            return "Ended";
        }

        Duration duration = Duration.ofMillis(millisLeft);
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if (days > 0) {
            return days + "d " + hours + "h";
        }
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return Math.max(minutes, 1) + "m";
    }

    private String valueOrFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
