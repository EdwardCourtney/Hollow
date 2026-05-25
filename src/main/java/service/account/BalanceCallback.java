package service.account;

import dto.account.BalanceResponse;

public interface BalanceCallback {
    void onSuccess(BalanceResponse response);

    void onError(String message);
}
