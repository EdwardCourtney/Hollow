package service.account;

import dto.response.BalanceResponse;

public interface BalanceCallback {
    void onSuccess(BalanceResponse response);

    void onError(String message);
}
