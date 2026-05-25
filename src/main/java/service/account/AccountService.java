package service.account;

import dto.request.DepositRequest;
import dto.response.BalanceResponse;
import network.ApiClient;
import model.TokenStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountService {
    public void getBalance(BalanceCallback callback) {
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            callback.onError("You must login first");
            return;
        }

        String authorization = "Bearer " + TokenStorage.accessToken;

        ApiClient.api.getBalance(authorization).enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    return;
                }

                callback.onError("Get balance failed. HTTP code: " + response.code());
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable throwable) {
                callback.onError("Network error: " + throwable.getMessage());
            }
        });
    }

    public void deposit(String amountText, BalanceCallback callback) {
        if (TokenStorage.accessToken == null || TokenStorage.accessToken.isBlank()) {
            callback.onError("You must login first");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            callback.onError("Amount must be a valid number");
            return;
        }

        if (amount <= 0) {
            callback.onError("Amount must be positive");
            return;
        }

        String authorization = "Bearer " + TokenStorage.accessToken;
        DepositRequest request = new DepositRequest(amount);

        ApiClient.api.deposit(authorization, request).enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    return;
                }

                callback.onError("Deposit failed. HTTP code: " + response.code());
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable throwable) {
                callback.onError("Network error: " + throwable.getMessage());
            }
        });
    }
}
