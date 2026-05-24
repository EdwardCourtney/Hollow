package network;

import model.request.LoginRequest;
import model.request.PublishItemRequest;
import model.request.RegisterRequest;
import model.response.*;
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
}
