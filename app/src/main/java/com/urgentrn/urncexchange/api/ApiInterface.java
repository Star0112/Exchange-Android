package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.request.MembershipRequest;
import com.urgentrn.urncexchange.models.request.OrderRequest;
import com.urgentrn.urncexchange.models.request.ProfileUpdateRequest;
import com.urgentrn.urncexchange.models.request.SendAssetRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.BuyHistoryResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.models.response.MembershipResponse;
import com.urgentrn.urncexchange.models.response.PurchaseStatusResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user")
        // Get user info / profile
    Call<GetUserResponse> getUser();

    @POST("api/m/v1/auth/login")
        // Login Simple Flow
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/m/v1/asset/info")
        // Get Deposit Balance
    Call<AssetResponse> getAssetBalance();

    @GET("api/m/v1/asset/depositHistory")
        // Get Deposit History
    Call<DepositHistoryResponse> getDepositHistory(@Query("offset") int offset, @Query("limit") int limit);

    @GET("api/m/v1/market/price")
        // Get Market Info
    Call<MarketInfoResponse> getMarketInfo();

    @POST("api/m/v1/buy/coin")
        // Buy Coin
    Call<BaseResponse> buyCoin(@Body BuyCoinRequest request);

    @POST("api/m/v1/order/create")
        // Order Coin
    Call<BaseResponse> orderCoin(@Body OrderRequest request);

    @POST("api/m/v1/user/profile/update")
        // Update User Profile
    Call<LoginResponse> updateProfile(@Body ProfileUpdateRequest request);

    @POST("api/m/v1/membership/purchase")// Purchase
    Call<MembershipResponse> purchase(@Body MembershipRequest request);

    @GET("api/m/v1/membership/status") //Purchase status
    Call<PurchaseStatusResponse> getPurchaseStatus();

    @GET("api/m/v1/buy/history")
    Call<BuyHistoryResponse> getBuyHistory(@Query("search") String search, @Query("offset") int offset, @Query("limit") int limit);

    @POST("api/m/v1/send/byemail")
    Call<BaseResponse> sendByEmail(@Body SendAssetRequest request);
}
