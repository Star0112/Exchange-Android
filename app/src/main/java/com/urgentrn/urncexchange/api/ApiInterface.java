package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user") // Get user info / profile
    Call<GetUserResponse> getUser();

    @POST("api/m/v1/auth/login") // Login Simple Flow
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/m/v1/asset/info") // Get Deposit Balance
    Call<AssetResponse> getAssetBalance();

    @GET("api/m/v1/asset/depositHistory") // Get Deposit History
    Call<DepositHistoryResponse> getDepositHistory(@Query("offset") int offset, @Query("limit") int limit);

    @GET("api/m/v1/market/list") // Get Market Info
    Call<MarketInfoResponse> getMarketInfo();

    @POST("api/m/v1/buy/coin") // Buy Coin
    Call<BaseResponse> buyCoin(@Body BuyCoinRequest request);

}
