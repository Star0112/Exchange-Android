package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.contacts.BaseWalletAddress;
import com.urgentrn.urncexchange.models.request.ActivateCardRequest;
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
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ContactResponse;
import com.urgentrn.urncexchange.models.response.CreateWalletResponse;
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

    @POST("versions")
    Call<GetVersionsResponse> getVersions(@Body GetVersionsRequest request);

    // -------------------- Client Methods --------------------

    /* Accounts */

    @GET("account/flow") // Bank Account Flow
    Call<GetFlowResponse> getBankFlow();

    @PUT("account/{id}") // Bank Account Edit
    Call<BaseResponse> editBankAccount(@Path("id") int id, @Body HashMap<String, String> params);

    @DELETE("account/{id}") // Bank Account Delete
    Call<BaseResponse> removeBankAccount(@Path("id") int id);

    @GET("account") // Bank Account List
    Call<GetAccountsResponse> getBankAccounts();

    @POST("account/{id}/create") // Bank Account Add
    Call<BaseResponse> addBankAccount(@Path("id") int id, @Body HashMap<String, String> params);

    @GET
    Call<PlaidApiResponse> getPlaidApi(@Url String url);

    @POST
    Call<BaseResponse> addPlaidAccount(@Url String callbackUrl, @Body PlaidRequest request);

    /* Contacts */

    @POST("contacts") // Create Contact
    Call<ContactResponse> createContact(@Body ContactRequest request);

    @GET("contacts") // Get Contacts
    Call<GetContactsResponse> getContacts();

    @DELETE("contacts/{id}") // Delete Contact
    Call<ContactResponse> deleteContact(@Path("id") int id);

    @PUT("contacts/{id}") // Update Contact
    Call<ContactResponse> updateContact(@Path("id") int id, @Body ContactRequest request);

    /* Cards */

    @GET("cards") // Available Card List
    Call<GetAvailableCardsResponse> getAvailableCards();

    @GET("card") // List of Card
    Call<GetCardsResponse> getMyCards();

    @GET("card/transaction/{id}") // List of Transactions
    Call<GetCardTransactionsResponse> getCardTransactions(@Path("id") String id, @Query("page") int page, @Query("limit") int limit);

    @GET("card/gift") // List available gift cards
    Call<GetGiftCardsResponse> getAvailableGiftCards(@QueryMap HashMap<String, Object> options);

    @PUT("card/{id}") // Update Locked Status
    Call<GetCardResponse> lockCard(@Path("id") String reference, @Body HashMap<String, Object> request);

    @POST("card/issue") // Issue a card
    Call<BaseResponse> issueCard(@Body IssueCardRequest request);

    @POST("card/upgrade") // Upgrade a card
    Call<BaseResponse> upgradeCard(@Body UpgradeCardRequest request);

    @POST("card/fraud/{id}") // Fraud
    Call<BaseResponse> reportCard(@Path("id") int id, @Body HashMap<String, String> request);

    @POST("card/activate") // Activate a card
    Call<BaseResponse> activateCard(@Body ActivateCardRequest request);

    @POST("card/info") // Information of a card
    Call<GetCardResponse> getCardInfo(@Body HashMap<String, String> request);

    @POST("card/info?platformData=marqeta") // Information of a card with network
    Call<GetCardResponse> getNetwork(@Body HashMap<String, String> request);

    @POST("card/token") // Token Request
    Call<TokenResponse> requestToken(@Body TokenRequest request);

    @POST("card/pin/create") // Create Pin
    Call<BaseResponse> createPin(@Body HashMap<String, String> request);

    @POST("card/pin") // Reset and see Pin
    Call<PinResponse> getPin(@Body HashMap<String, String> request);

    @POST("card/load")
    Call<BaseResponse> loadCard(@Body LoadCardRequest request);

    /* Disbursements */

    @POST("disbursement/deposit") // Disbursement Deposit
    Call<BaseResponse> disbursementDeposit(@Body DisbursementRequest request);

    @POST("disbursement/withdraw") // Disbursement Withdraw
    Call<BaseResponse> disbursementWithdraw(@Body DisbursementRequest request);

    /* Exchanges */

    @GET("exchange/tickers") // Exchange Ticker
    Call<GetExchangeTickersResponse> getExchangeTickers();

    @GET("exchange/tickers")
    Call<GetExchangeTickersResponse> getExchangeTickers(@Query("collection") String collection);

    @POST("exchange/quote/{quoteId}/confirm") // Exchange Quote
    Call<ExchangeConfirmResponse> exchangeConfirm(@Path("quoteId") int quoteId);

    @POST("exchange/quote") // Exchange Quote
    Call<ExchangeQuoteResponse> exchangeQuote(@Body ExchangeQuoteRequest request);

    /* Transactions */

    @GET("transaction") // List Transactions
    Call<TransactionsResponse> getTransactions(@Query("symbol") String symbol, @Query("page") int page, @Query("limit") int limit);

    /* Reserve Entries */

    @PUT("reserveEntries/{reserveEntry}/release")
    Call<BaseResponse> releaseReserveEntry(@Path("reserveEntry") int reserveEntryId);

    /* Banners */

    @GET("banners") // Banners get
    Call<GetBannersResponse> getBanners();

    @POST("notification/device") // Notification Device Register
    Call<BaseResponse> registerDevice(@Body HashMap<String, String> request);

    /* Symbols */

    @GET("symbol") // Symbol
    Call<SymbolResponse> getSymbolData();

    @GET("symbol") // Symbol
    Call<SymbolResponse> getSymbols(@Query("type") String type);

    @GET("user/symbols/favorite") // Favorite Symbols
    Call<SymbolResponse> getFavoriteSymbols();

    @PUT("symbols/{symbol}") // Add Favorite Symbol
    Call<BaseResponse> addFavoriteSymbol(@Path("symbol") String symbol, @Body HashMap<String, Boolean> request);

    @PUT("symbol/{symbol}") // Set Default Currency
    Call<BaseResponse> setDefaultCurrency(@Path("symbol") String symbol);

    @PUT("symbol/{symbol}/remove") // Remove Default Currency
    Call<BaseResponse> removeDefaultCurrency(@Path("symbol") String symbol);

    /* Users */

    @PUT("user/update") // Update User
    Call<GetUserResponse> updateUser(@Body UpdateUserRequest request);

    @POST("user/activateSXP") // Activate SXP
    Call<ActivateResponse> activateSXP(@Body HashMap<String, Integer> request);

    @GET("user") // Get user info / profile
    Call<GetUserResponse> getUser();

    @GET("user/tier") // User tiers
    Call<GetTiersResponse> getTiers();

    @POST("user/docs") // User Docs
    Call<BaseResponse> uploadDocument(@Body ImageUploadRequest request);

    @POST("user/verify/phone") // Verify device
    Call<BaseResponse> verifyPhone(@Body CodeRequest request);

    @GET("user/referral") // Referral code
    Call<GetReferralResponse> getReferralCode();

    @GET("user/rewards") // Get Rewards
    Call<GetRewardsResponse> getRewards();

    @GET("user/lastUsedAddresses") // Get latest addresses used
    Call<GetLastUsedAddressesResponse> getLastUsedAddresses(@Query("symbol") String symbol, @Query("limit") int limit);

    @GET("user/logout") // User Logout
    Call<BaseResponse> logout();

    /* Wallet */

    @GET("wallet") // Wallet
    Call<WalletResponse> getWallet();

    @GET("wallet/address/{symbol}") // Generate Address
    Call<GetAddressResponse> getWalletAddress(@Path("symbol") String symbol);

    @GET("wallet/address/{symbol}") // Generate Address
    Call<GetDepositAddressResponse> getDepositAddress(@Path("symbol") String symbol);

    @POST("wallet/withdraw/{symbol}")
    Call<BaseResponse> withdraw(@Path("symbol") String symbol, @Body WithdrawRequest request);

    @PUT("wallet/{symbol}")
    Call<UpdateWalletResponse> updateDefaultWallet(@Path("symbol") String symbol, @Body UpdateWalletRequest request);

    /* Wallets */

    @POST("wallets") // Create Wallet
    Call<CreateWalletResponse> createWallet(@Body WalletRequest request);

    @GET("wallets") // Get Wallets
    Call<GetWalletsResponse> getWallets();

    @DELETE("wallets/{id}") // Delete Wallet
    Call<BaseResponse> deleteWallet(@Path("id") int id);

    @PUT("wallets/{id}") // Update Wallet
    Call<CreateWalletResponse> updateWallet(@Path("id") int id, @Body WalletRequest request);

    @POST("wallets/{walletId}/addresses") // Create Wallet Address
    Call<CreateWalletResponse> createWalletAddress(@Path("walletId") int walletId, @Body BaseWalletAddress request);

    @PUT("wallets/{walletId}/addresses/{id}") // Update Wallet Address
    Call<CreateWalletResponse> updateWalletAddress(@Path("walletId") int walletId, @Path("id") int id, @Body BaseWalletAddress request);

    @DELETE("wallets/{walletId}/addresses/{id}") // Delete Wallet Address
    Call<BaseResponse> deleteWalletAddress(@Path("walletId") int walletId, @Path("id") int id);

    @PUT("wallets/{symbol}") // Add Favorite Wallet
    Call<GetUserResponse> addFavoriteWallet(@Path("symbol") String symbol, @Body HashMap<String, Boolean> request);

    @PUT("currency/{symbol}") // Add Currency Wallet
    Call<BaseResponse> addCurrencyWallet(@Path("symbol") String symbol);

    @PUT("currency/{symbol}/remove") // Remove Currency Wallet
    Call<BaseResponse> removeCurrencyWallet(@Path("symbol") String symbol);

    @GET("wallet/favorite") // Get Favorite Wallets
    Call<WalletResponse> getFavoriteWallets();


    // -------------------- Public Methods --------------------

    @POST("user/signup") // Signup Request
    Call<SignupResponse> register(@Body SignupRequest request);

    @GET // Signup Confirmation
    Call<BaseResponse> verifyEmail(@Url String url);

    @POST("user/signup/confirm") // Resend confirmation code
    Call<LoginResponse> resend(@Body UserRequest request);

    @POST("user/username/validate") // Validate Username
    Call<BaseResponse> validateUsername(@Body HashMap<String, String> username);

    @POST("user/login/code") // Login 2FA Flow - If 2FA is active
    Call<LoginResponse> login(@Body TfaRequest request);

    @POST("auth/login") // Login Simple Flow
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("user/password") // Forgot password
    Call<ForgotPasswordResponse> forgotPassword(@Body UserRequest request);

    @POST("user/password/confirm") // Confirm code for forgot password
    Call<BaseResponse> forgotPasswordConfirm(@Body PasswordConfirmRequest request);

    @GET("location/countries") // List countries available in API
    Call<GetCountriesResponse> getCountries();

    @GET("location/states/{countryNameCode}") // Get states by country
    Call<GetStatesResponse> getStates(@Path("countryNameCode") String countryNameCode);

    @GET("marketData") // Get latest market data
    Call<MarketResponse> getGraphMarketData(@Query("symbol") String symbol, @Query("period") String period, @Query("periodFactor") int periodFactor, @Query("interval") int interval);

    @GET("portfolioData") // Get latest market data
    Call<PortfolioResponse> getPortfolioData(@Query("period") String period, @Query("periodFactor") int periodFactor, @Query("interval") int interval);

    @POST("user/username") // Forgot Username
    Call<BaseResponse> forgotUsername(@Body PhoneRequest request);

    @POST("user/username/confirm") // Forgot Username Confirm
    Call<PhoneConfirmResponse> forgotUsernameConfirm(@Body PhoneConfirmRequest request);

    @POST("user/referral/confirm") // Referral Code Confirmation
    Call<BaseResponse> referralCodeConfirm(@Body ReferralCodeRequest request);
}
