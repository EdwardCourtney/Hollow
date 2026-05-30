package service.auth;

public interface RegisterCallBack {
    void onSuccess(String message);

    void onError(String message);
}
