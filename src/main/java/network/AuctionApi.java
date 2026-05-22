package network;

import model.request.LoginRequest;
import model.request.PublishItemRequest;
import model.request.RegisterRequest;
import model.response.AuthResponse;
import model.response.BaseItemResponse;
import model.response.BaseResponse;
import model.response.GetItemPagesResponse;
import model.response.ItemStatusGetResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    Call<GetItemPagesResponse> getActiveItems(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("/item/status/{itemId}")
    Call<ItemStatusGetResponse> getItemStatus(@Path("itemId") Long itemId);
}
