package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.contacts.BaseWalletAddress;
import com.urgentrn.urncexchange.models.request.ActivateCardRequest;
import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.request.CodeRequest;
import com.urgentrn.urncexchange.models.request.ContactRequest;
import com.urgentrn.urncexchange.models.request.DisbursementRequest;
import com.urgentrn.urncexchange.models.request.ExchangeQuoteRequest;
import com.urgentrn.urncexchange.models.request.GetVersionsRequest;
import com.urgentrn.urncexchange.models.request.ImageUploadRequest;
import com.urgentrn.urncexchange.models.request.IssueCardRequest;
import com.urgentrn.urncexchange.models.request.LoadCardRequest;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.request.PasswordConfirmRequest;
import com.urgentrn.urncexchange.models.request.PhoneConfirmRequest;
import com.urgentrn.urncexchange.models.request.PhoneRequest;
import com.urgentrn.urncexchange.models.request.PlaidRequest;
import com.urgentrn.urncexchange.models.request.ReferralCodeRequest;
import com.urgentrn.urncexchange.models.request.SignupRequest;
import com.urgentrn.urncexchange.models.request.TfaRequest;
import com.urgentrn.urncexchange.models.request.TokenRequest;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.request.UpdateWalletRequest;
import com.urgentrn.urncexchange.models.request.UpgradeCardRequest;
import com.urgentrn.urncexchange.models.request.UserRequest;
import com.urgentrn.urncexchange.models.request.WalletRequest;
import com.urgentrn.urncexchange.models.request.WithdrawRequest;
import com.urgentrn.urncexchange.models.response.ActivateResponse;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ContactResponse;
import com.urgentrn.urncexchange.models.response.CreateWalletResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.models.response.ExchangeConfirmResponse;
import com.urgentrn.urncexchange.models.response.ExchangeQuoteResponse;
import com.urgentrn.urncexchange.models.response.ForgotPasswordResponse;
import com.urgentrn.urncexchange.models.response.GetAccountsResponse;
import com.urgentrn.urncexchange.models.response.GetAddressResponse;
import com.urgentrn.urncexchange.models.response.GetApiResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.models.response.GetBannersResponse;
import com.urgentrn.urncexchange.models.response.GetCardResponse;
import com.urgentrn.urncexchange.models.response.GetCardTransactionsResponse;
import com.urgentrn.urncexchange.models.response.GetCardsResponse;
import com.urgentrn.urncexchange.models.response.GetContactsResponse;
import com.urgentrn.urncexchange.models.response.GetCountriesResponse;
import com.urgentrn.urncexchange.models.response.GetDepositAddressResponse;
import com.urgentrn.urncexchange.models.response.GetExchangeTickersResponse;
import com.urgentrn.urncexchange.models.response.GetFlowResponse;
import com.urgentrn.urncexchange.models.response.GetGiftCardsResponse;
import com.urgentrn.urncexchange.models.response.GetLastUsedAddressesResponse;
import com.urgentrn.urncexchange.models.response.GetReferralResponse;
import com.urgentrn.urncexchange.models.response.GetRewardsResponse;
import com.urgentrn.urncexchange.models.response.GetStatesResponse;
import com.urgentrn.urncexchange.models.response.GetTiersResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.GetVersionsResponse;
import com.urgentrn.urncexchange.models.response.GetWalletsResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.models.response.MarketResponse;
import com.urgentrn.urncexchange.models.response.PhoneConfirmResponse;
import com.urgentrn.urncexchange.models.response.PinResponse;
import com.urgentrn.urncexchange.models.response.PlaidApiResponse;
import com.urgentrn.urncexchange.models.response.PortfolioResponse;
import com.urgentrn.urncexchange.models.response.SignupResponse;
import com.urgentrn.urncexchange.models.response.SymbolResponse;
import com.urgentrn.urncexchange.models.response.TokenResponse;
import com.urgentrn.urncexchange.models.response.TransactionsResponse;
import com.urgentrn.urncexchange.models.response.UpdateWalletResponse;
import com.urgentrn.urncexchange.models.response.WalletResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<GetApiResponse> getApiUrl(@Url String url);

    @GET("contacts") // Get Contacts
    Call<GetContactsResponse> getContacts();

    @POST("card/pin/create") // Create Pin
    Call<BaseResponse> createPin(@Body HashMap<String, String> request);

    /* Users */

    @PUT("user/update") // Update User
    Call<GetUserResponse> updateUser(@Body UpdateUserRequest request);

    @GET("user") // Get user info / profile
    Call<GetUserResponse> getUser();

    @POST("user/verify/phone") // Verify device
    Call<BaseResponse> verifyPhone(@Body CodeRequest request);

    // -------------------- Public Methods --------------------

    @POST("user/signup") // Signup Request
    Call<SignupResponse> register(@Body SignupRequest request);

    @POST("user/signup/confirm") // Resend confirmation code
    Call<LoginResponse> resend(@Body UserRequest request);

    @POST("user/username/validate") // Validate Username
    Call<BaseResponse> validateUsername(@Body HashMap<String, String> username);

    @POST("user/login/code") // Login 2FA Flow - If 2FA is active
    Call<LoginResponse> login(@Body TfaRequest request);

    @POST("user/password") // Forgot password
    Call<ForgotPasswordResponse> forgotPassword(@Body UserRequest request);

    @POST("user/password/confirm") // Confirm code for forgot password
    Call<BaseResponse> forgotPasswordConfirm(@Body PasswordConfirmRequest request);

    @GET("location/countries") // List countries available in API
    Call<GetCountriesResponse> getCountries();

    @POST("user/username") // Forgot Username
    Call<BaseResponse> forgotUsername(@Body PhoneRequest request);

    @POST("user/username/confirm") // Forgot Username Confirm
    Call<PhoneConfirmResponse> forgotUsernameConfirm(@Body PhoneConfirmRequest request);

    @POST("user/referral/confirm") // Referral Code Confirmation
    Call<BaseResponse> referralCodeConfirm(@Body ReferralCodeRequest request);




    // For URNC
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
