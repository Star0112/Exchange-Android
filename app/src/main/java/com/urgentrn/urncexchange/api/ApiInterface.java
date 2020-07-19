package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.request.InviteUserRequest;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.request.MembershipRequest;
import com.urgentrn.urncexchange.models.request.OrderRequest;
import com.urgentrn.urncexchange.models.request.ProfileUpdateRequest;
import com.urgentrn.urncexchange.models.request.SendAssetRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.BuyHistoryResponse;
import com.urgentrn.urncexchange.models.response.ChartDataResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.models.response.FriendHistoryResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.InviteUserResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.models.response.MembershipResponse;
import com.urgentrn.urncexchange.models.response.PurchaseStatusResponse;
import com.urgentrn.urncexchange.models.response.SendHistoryResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user")
        // Get user info / profile
    Call<GetUserResponse> getUser();

    @POST("auth/login")
        // Login Simple Flow
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("market/kline") //Get Chart Data
    Call<ChartDataResponse> getChartData(@Query("market") String market, @Query("start") String start, @Query("end") String end, @Query("interval") int interval);

    @GET("asset/info")
        // Get Deposit Balance
    Call<AssetResponse> getAssetBalance();

    @GET("asset/depositHistory")
        // Get Deposit History
    Call<DepositHistoryResponse> getDepositHistory(@Query("offset") int offset, @Query("limit") int limit);

    @GET("market/price")
        // Get Market Info
    Call<MarketInfoResponse> getMarketInfo();

    @POST("buy/coin")
        // Buy Coin
    Call<BaseResponse> buyCoin(@Body BuyCoinRequest request);

    @POST("order/create")
        // Order Coin
    Call<BaseResponse> orderCoin(@Body OrderRequest request);

    @POST("user/profile/update")
        // Update User Profile
    Call<LoginResponse> updateProfile(@Body ProfileUpdateRequest request);

    @POST("membership/purchase")// Purchase
    Call<MembershipResponse> purchase(@Body MembershipRequest request);

    @GET("membership/status") //Purchase status
    Call<PurchaseStatusResponse> getPurchaseStatus();

    @POST("user/referrals")
    Call<InviteUserResponse> inviteUser(@Body InviteUserRequest request);

    @GET("user/referrals")
    Call<FriendHistoryResponse> getReferral(@Query("offset") int offset, @Query("limit") int limit);

    @GET("buy/history")
    Call<BuyHistoryResponse> getBuyHistory(@Query("search") String search, @Query("offset") int offset, @Query("limit") int limit);

    @GET("asset/sendhistory")
    Call<SendHistoryResponse> getSendHistory(@Query("search") String search, @Query("offset") int offset, @Query("limit") int limit);

    @POST("send/byemail")
    Call<BaseResponse> sendByEmail(@Body SendAssetRequest request);
}
