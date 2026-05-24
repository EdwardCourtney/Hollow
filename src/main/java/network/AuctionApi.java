package network;

import model.request.LoginRequest;
<<<<<<< HEAD
import model.request.BidPostRequest;
import model.request.PublishItemRequest;
=======
>>>>>>> parent of aff6f03 (feat: publish auction)
import model.request.RegisterRequest;
import model.response.AuthResponse;
import model.response.BaseResponse;
import model.response.BidPageResponse;
import model.response.BidPostResponse;
import model.response.GetItemPagesResponse;
import model.response.ItemPageObjectResponse;
import model.response.ItemStatusGetResponse;
import retrofit2.Call;
import retrofit2.http.Body;
<<<<<<< HEAD
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
=======
>>>>>>> parent of aff6f03 (feat: publish auction)
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuctionApi {
    @POST("/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/register")
    Call<BaseResponse> register(@Body RegisterRequest request);
<<<<<<< HEAD

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

    @GET("/items/{itemId}")
    Call<BaseItemResponse> getItem(@Path("itemId") Long itemId);

    @GET("/items/listings/{username}")
    Call<ItemPageObjectResponse> getListings(
            @Path("username") String username,
            @Query("page") int page,
            @Query("size") int size
    );

    @POST("/items/cancel/{itemId}")
    Call<BaseResponse> cancelItem(
            @Header("Authorization") String authorization,
            @Path("itemId") Long itemId
    );

    @GET("/item/status/{itemId}")
    Call<ItemStatusGetResponse> getItemStatus(@Path("itemId") Long itemId);

    @POST("/bid")
    Call<BidPostResponse> makeBid(
            @Header("Authorization") String authorization,
            @Body BidPostRequest request
    );

    @GET("/bids/{itemId}/bids")
    Call<BidPageResponse> getItemBids(
            @Header("Authorization") String authorization,
            @Path("itemId") Long itemId,
            @Query("page") int page,
            @Query("size") int size
    );
=======
>>>>>>> parent of aff6f03 (feat: publish auction)
}
