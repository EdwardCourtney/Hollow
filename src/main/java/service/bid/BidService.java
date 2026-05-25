package service.bid;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.request.BidPostRequest;
import dto.response.BidPostResponse;
import model.TokenStorage;
import network.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class BidService {
    public void placeBid(Long itemId, String bidAmountText, BidCallback callback) {
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            callback.onError("You must login first");
            return;
        }

        if (itemId == null) {
            callback.onError("Missing item id");
            return;
        }

        double bidAmount;
        try {
            bidAmount = Double.parseDouble(bidAmountText);
        } catch (NumberFormatException e) {
            callback.onError("Bid amount must be a valid number");
            return;
        }

        if (bidAmount <= 0) {
            callback.onError("Bid amount must be positive");
            return;
        }

        String authorization = "Bearer " + TokenStorage.accessToken;
        BidPostRequest request = new BidPostRequest(itemId, bidAmount);

        ApiClient.api.placeBid(authorization, request).enqueue(new Callback<BidPostResponse>() {
            @Override
            public void onResponse(Call<BidPostResponse> call, Response<BidPostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    return;
                }

                callback.onError(errorMessage(response));
            }

            @Override
            public void onFailure(Call<BidPostResponse> call, Throwable throwable) {
                callback.onError("Network error: " + throwable.getMessage());
            }
        });
    }

    private String errorMessage(Response<?> response) {
        String message = readMessage(response.errorBody());
        if (message != null && !message.isBlank()) {
            return message;
        }

        return "Bid failed. HTTP code: " + response.code();
    }

    private String readMessage(ResponseBody errorBody) {
        if (errorBody == null) {
            return null;
        }

        try {
            JsonObject json = JsonParser.parseString(errorBody.string()).getAsJsonObject();
            if (json.has("message") && !json.get("message").isJsonNull()) {
                return json.get("message").getAsString();
            }
        } catch (IOException | IllegalStateException ignored) {
            return null;
        }

        return null;
    }
}
