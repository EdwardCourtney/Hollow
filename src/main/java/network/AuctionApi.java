package network;

import dto.request.BidPostRequest;
import dto.request.DepositRequest;
import dto.request.LoginRequest;
import dto.request.PublishItemRequest;
import dto.request.RegisterRequest;
import dto.response.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuctionApi {
    @POST("/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/register")
    Call<BaseResponse> register(@Body RegisterRequest request);

    @POST("/items")
    Call<BaseItemResponse> createItem(
            @Header("Authorization") String authorization,
            @Body PublishItemRequest request
    );

    @GET("/items")
    Call<GetItemPageResponse> getItems(@Query("page") int page,
                                       @Query("size") int size);

    @GET("/item/status/{itemId}")
    Call<ItemStatusResponse> getItemStatus(@Path("itemId") Long itemId);

    @GET("/users/me/balance")
    Call<BalanceResponse> getBalance(@Header("Authorization") String authorization);

    @POST("/users/me/deposit")
    Call<BalanceResponse> deposit(
            @Header("Authorization") String authorization,
            @Body DepositRequest request
    );

    @POST("/bid")
    Call<BidPostResponse> placeBid(
            @Header("Authorization") String authorization,
            @Body BidPostRequest request
    );
}
