package com.marqeta.marqetakit;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tapandpay.TapAndPay;
import com.google.android.gms.tapandpay.TapAndPayStatusCodes;
import com.google.android.gms.tapandpay.issuer.PushTokenizeRequest;
import com.marqeta.marqetakit.service.ICard;
import com.marqeta.marqetakit.service.IDigitalWalletToken;
import com.marqeta.marqetakit.service.IProvisionRequest;
import com.marqeta.marqetakit.service.IProvisionResponse;
import com.marqeta.marqetakit.service.ITokenizeData;
import com.marqeta.marqetakit.service.IUserAddress;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincentguerin on 8/2/17.
 */

public class MarqetaKit {

    public static final int REQUEST_CODE_SELECT_TOKEN = 23;
    public static final int REQUEST_CODE_DELETE_TOKEN = 24;
    public static final int REQUEST_CODE_TOKENIZE = 25;
    public static final int REQUEST_CODE_PUSH_TOKENIZE = 26;
    public static final int REQUEST_CODE_CREATE_WALLET = 27;

    private WeakReference<FragmentActivity> activity;

    private GoogleApiClient googleApiClient;
    private TapAndPay tapAndPay = TapAndPay.TapAndPay;

    private String stableHardwareId;
    private String walletId;

    // for token sync
    private int cardsToVerify;
    private int tokensToVerify;
    private boolean tokenSyncInProgress;


    public MarqetaKit(FragmentActivity context) {
        activity = new WeakReference<FragmentActivity>(context);
        initClient(context);
        tokenSyncInProgress = false;
        walletId = null;
        stableHardwareId = null;
    }

    /**
     * Asynchronously compares a token for a card against one registered with Android Pay
     *
     * @param card - Card to validate
     * @param callback - Callback that will receive response once all cards have been validated
     */
    public void syncToken(final ICard card, final TokenSyncCallback callback) {
        List cards = new ArrayList<ICard>() {{
            add(card);
        }};

        syncTokens(cards, callback);
    }

    /**
     * Asynchronously compares all tokens for cards against those registered with Android Pay
     *
     * @param cards - List of Cards to validate
     * @param callback - Callback that will receive response once all cards have been validated
     */
    public void syncTokens(List<? extends ICard> cards, final TokenSyncCallback callback) {

        // prevents method from getting called while another instance is already running
        if (tokenSyncInProgress)
            return;

        tokenSyncInProgress = true;

        //number of cards to verify
        cardsToVerify = cards.size();
        tokensToVerify = 0;

        for (final ICard card : cards) {
            if (card.getDigitalWalletTokens() != null && !card.getDigitalWalletTokens().isEmpty()) {

                // number of tokens to verify for this card
                tokensToVerify += card.getDigitalWalletTokens().size();

                for (int i=0; i < card.getDigitalWalletTokens().size(); i++) {
                    IDigitalWalletToken cardToken = card.getDigitalWalletTokens().get(i);

                    tapAndPay.getTokenStatus(
                            googleApiClient,
                            TapAndPayUtils.convertToConstant(card.getNetwork()),
                            cardToken.getTokenReferenceId()).setResultCallback(new ResultCallback<TapAndPay.GetTokenStatusResult>() {
                        @Override
                        public void onResult(@NonNull TapAndPay.GetTokenStatusResult tokenStatusResult) {
                            if (tokenStatusResult.getStatus().isSuccess()) {
                                card.setCanBeProvisioned(false);
                            }

                            // token checked; if all tokens checked, mark card as complete
                            if (--tokensToVerify == 0) {
                                cardsToVerify--;
                            }

                            if (tokensToVerify == 0 && cardsToVerify == 0) {
                                if (callback != null) {
                                    tokenSyncInProgress = false;
                                    callback.onSyncComplete(!tokenStatusResult.getStatus().isSuccess()); // parameter is effective only for one card
                                }
                            }
                        }

                    });

                }
            } else {
                // card has been verified (no tokens)
                cardsToVerify--;

                if (cardsToVerify == 0 && tokensToVerify == 0) {
                    tokenSyncInProgress = false;
                    callback.onSyncComplete(true);
                }
            }

        }
    }

    /**
     * Asynchronously fetches and sets Android Pay wallet parameters
     * required for push provisioning
     *
     * @param callback - Callback that will receive response when parameters are set (or failed)
     */
    public void initializeWallet(final WalletInitializationCallback callback) {

        tapAndPay.registerDataChangedListener(googleApiClient, new TapAndPay.DataChangedListener() {
            @Override
            public void onDataChanged() {
                getActiveWalletId(callback);
            }
        });

        if (stableHardwareId != null && walletId != null) {
            callback.onWalletInitialized();
        } else {
            getHardwareId(callback);
            getActiveWalletId(callback);
        }
    }


    /**
     * Provision given card for Android Pay
     *
     * @param card - card to provision
     * @param deviceType - device type (ie, "MOBILE_PHONE")
     * @param request - empty request object implementation instance
     * @param requestor - API request to backend service to fetch provisioning data
     */
    public void pushProvision(ICard card, String deviceType,
                              @NonNull IProvisionRequest request, PushProvisionRequestor requestor) {

        request.setCardToken(card.getToken());
        request.setDeviceType(deviceType);

        if (activity != null) {
            final FragmentActivity context = activity.get();
            if (context != null) {
                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    request.setProvisioningAppVersion(pInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    // Do nothing and don't set the value
                }

            }
        }

        request.setWalletAccountId(walletId);
        request.setDeviceId(stableHardwareId);

        requestor.callProvisionApi(request, new PushProvisionApiResponseDelegate() {
            @Override
            public void onResponse(IProvisionResponse response) {

                ITokenizeData tokenizeData = response.getTokenizeData();

                PushTokenizeRequest pushTokenizeRequest = new PushTokenizeRequest.Builder()
                        .setOpaquePaymentCard(tokenizeData.getOpaquePaymentCard().getBytes())
                        .setNetwork(TapAndPayUtils.convertToConstant(tokenizeData.getNetwork()))
                        .setTokenServiceProvider(TapAndPayUtils.convertTspToEnum(tokenizeData.getTokenServiceProvider()))
                        .setDisplayName(tokenizeData.getDisplayName())
                        .setLastDigits(tokenizeData.getLastDigits())
                        .setUserAddress(getUserAddress(tokenizeData))
                        .build();

                if (activity != null) {
                    FragmentActivity context = activity.get();
                    if (context != null) {
                        tapAndPay.pushTokenize(
                                googleApiClient,
                                context,
                                pushTokenizeRequest,
                                REQUEST_CODE_PUSH_TOKENIZE);
                    }
                }

            }
        });

    }


    /**
     * Destroys GoogleApiClient and detaches it from activity
     * Should be called in #onDestroy() of Activity
     */
    public void destroy() {

        // clean up references
        if (activity != null) {
            final FragmentActivity context = activity.get();
            if (context != null) {
                if (googleApiClient != null) {
                    googleApiClient.stopAutoManage(context);
                    googleApiClient.disconnect();
                    googleApiClient = null;
                }
            }
        }
    }


    public String getHardwareId() {
        return stableHardwareId;
    }


    private void getActiveWalletId(final WalletInitializationCallback listener) {

        tapAndPay.getActiveWalletId(googleApiClient).setResultCallback(new ResultCallback<TapAndPay.GetActiveWalletIdResult>() {
            @Override
            public void onResult(@NonNull TapAndPay.GetActiveWalletIdResult activeWalletIdResult) {
                if (activeWalletIdResult != null && activeWalletIdResult.getStatus().isSuccess()) {
                    walletId = activeWalletIdResult.getActiveWalletId();
                    if (stableHardwareId != null && listener != null) {
                        listener.onWalletInitialized();
                    }
                } else {
                    int code = activeWalletIdResult.getStatus().getStatusCode();

                    // no active wallet
                    if (code == TapAndPayStatusCodes.TAP_AND_PAY_NO_ACTIVE_WALLET) {
                        if (activity != null) {
                            final FragmentActivity context = activity.get();
                            if (context != null) {
                                // reset values so we can fetch them later
                                stableHardwareId = null;
                                walletId = null;

                                tapAndPay.createWallet(googleApiClient, context, REQUEST_CODE_CREATE_WALLET);
                            }
                        }

                        // return error to allow for new google wallet creation
                        if (listener != null)
                            listener.onWalletSetupFailed();
                    }

                }

            }
        });

    }

    private void getHardwareId(final WalletInitializationCallback listener) {
        tapAndPay.getStableHardwareId(googleApiClient).setResultCallback(
                new ResultCallback<TapAndPay.GetStableHardwareIdResult>() {
                    @Override
                    public void onResult(@NonNull TapAndPay.GetStableHardwareIdResult result) {

                        if (result != null && result.getStableHardwareId() != null) {
                            stableHardwareId = result.getStableHardwareId();

                            // if we already have wallet id, setup is complete
                            if (walletId != null && listener != null) {
                                listener.onWalletInitialized();
                            }

                        } else {
                            if (activity != null) {
                                final FragmentActivity context = activity.get();
                                if (context != null) {
                                    Toast.makeText(context, context.getString(R.string.retrieve_hardware_id_fail), Toast.LENGTH_LONG).show();
                                    if (listener != null)
                                        listener.onWalletSetupFailed();
                                }

                            }
                        }

                    }
                });

    }

    private void initClient(final FragmentActivity context) {
        Address.AddressOptions options = new Address.AddressOptions(1); // THEME_LIGHT

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(TapAndPay.TAP_AND_PAY_API)
                .addApi(Address.API, options)
                .enableAutoManage(context, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(context, context.getString(R.string.connection_failed) + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();

        googleApiClient.connect();
    }

    private UserAddress getUserAddress(ITokenizeData tokenizeData) {
        IUserAddress tokenizeAddress = tokenizeData.getUserAddress();

        if (tokenizeAddress != null) {
            UserAddress.Builder userAddressBuilder = UserAddress.newBuilder();

            if (tokenizeAddress.getAddress1() != null && !tokenizeAddress.getAddress1().isEmpty()) {
                userAddressBuilder.setAddress1(tokenizeAddress.getAddress1());
            }

            if (tokenizeAddress.getZip() != null && !tokenizeAddress.getZip().isEmpty()) {
                userAddressBuilder.setPostalCode(tokenizeAddress.getZip());
            }

            if (tokenizeAddress.getName() != null && !tokenizeAddress.getName().isEmpty()) {
                userAddressBuilder.setName(tokenizeAddress.getName());
            }

            if (tokenizeAddress.getCountry() != null && !tokenizeAddress.getCountry().isEmpty()) {
                userAddressBuilder.setCountryCode(tokenizeAddress.getCountry());
            }

            if (tokenizeAddress.getCity() != null && !tokenizeAddress.getCity().isEmpty()) {
                userAddressBuilder.setLocality(tokenizeAddress.getCity());
            }

            if (tokenizeAddress.getState() != null && !tokenizeAddress.getState().isEmpty()) {
                userAddressBuilder.setAdministrativeArea(tokenizeAddress.getState());
            }

            if (tokenizeAddress.getPhone() != null && !tokenizeAddress.getPhone().isEmpty()) {
                userAddressBuilder.setPhoneNumber(tokenizeAddress.getPhone());
            }

            return userAddressBuilder.build();
        }

        return null;
    }

//    protected void fetchGoogleWalletId(final ResultCallback<TapAndPay.GetActiveWalletIdResult> resultCallback) {
//        if (activity != null) {
//            final FragmentActivity context = activity.get();
//            if (context != null) {
//
//
//                getActiveWalletId(context, resultCallback);
//            }
//        }
//    }

//    protected void getActiveWalletId(final FragmentActivity context, ResultCallback<TapAndPay.GetActiveWalletIdResult> resultCallback) {
//        tapAndPay.getActiveWalletId(googleApiClient)
//                .setResultCallback(resultCallback);
//    }


    public interface TokenSyncCallback {
        void onSyncComplete(boolean canBeProvisioned);
    }

    public interface WalletInitializationCallback {
        void onWalletInitialized();
        void onWalletSetupFailed();
    }

    public interface PushProvisionRequestor {
        void callProvisionApi(IProvisionRequest provisionRequest, PushProvisionApiResponseDelegate delegate);
    }

    public interface PushProvisionApiResponseDelegate {
        void onResponse(IProvisionResponse response);
    }

}
