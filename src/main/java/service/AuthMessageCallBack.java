package service;

public interface AuthMessageCallBack {
    void onSuccess(String message);

    void onError(String message);
}
