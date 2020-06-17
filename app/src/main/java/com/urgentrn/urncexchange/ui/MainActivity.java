package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.fcm.ExchangeMessagingService;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetExchangeTickersResponse;
import com.urgentrn.urncexchange.models.response.GetFlowResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.MarketResponse;
import com.urgentrn.urncexchange.models.response.SymbolResponse;
import com.urgentrn.urncexchange.models.response.WalletResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.PinDialog;
import com.urgentrn.urncexchange.ui.dialogs.PinDialog_;
import com.urgentrn.urncexchange.ui.fragments.buy.BuyFragment_;
import com.urgentrn.urncexchange.ui.fragments.card.CardContainerFragment;
import com.urgentrn.urncexchange.ui.fragments.card.CardContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.card.CardOrderFragment;
import com.urgentrn.urncexchange.ui.fragments.exchange.ExchangeFragment;
import com.urgentrn.urncexchange.ui.fragments.exchange.ExchangeFragment_;
import com.urgentrn.urncexchange.ui.fragments.gift.GiftContainerFragment;
import com.urgentrn.urncexchange.ui.fragments.gift.GiftContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.dashboard.DashboardFragment;
import com.urgentrn.urncexchange.ui.fragments.dashboard.DashboardFragment_;
import com.urgentrn.urncexchange.ui.fragments.order.OrderFragment_;
import com.urgentrn.urncexchange.ui.fragments.price.PriceContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.price.PriceFragment;
import com.urgentrn.urncexchange.ui.fragments.profile.ProfileContainerFragment;
import com.urgentrn.urncexchange.ui.fragments.profile.ProfileContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.profile.ProfileFragmentNew_;
import com.urgentrn.urncexchange.ui.fragments.wallet.WalletFragment;
import com.urgentrn.urncexchange.ui.transactions.BuySellSuccessActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ApiCallback {

    @ViewById(R.id.navigation)
    BottomNavigationView navigation;

    private final DashboardFragment_ fragment1 = new DashboardFragment_();
    private final BuyFragment_ fragment2 = new BuyFragment_();
//    private final OrderFragment fragment3 = new OrderFragment();
    private final OrderFragment_ fragment4 = new OrderFragment_();
    private final ProfileFragmentNew_ fragment5 = new ProfileFragmentNew_();
    private final FragmentManager fm = getSupportFragmentManager();
    private BaseFragment active = fragment1;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            getData(false);
//            handler.postDelayed(this, Constants.API_REQUEST_INTERVAL_DEFAULT);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_dash:
                fm.beginTransaction().hide(active).show(fragment1).commitAllowingStateLoss();
                active = fragment1;
                break;
            case R.id.navigation_buy:
                fm.beginTransaction().hide(active).show(fragment2).commitAllowingStateLoss();
                active = fragment2;
                break;
//            case R.id.navigation_gift:
//                fm.beginTransaction().hide(active).show(fragment3).commitAllowingStateLoss();
//                active = fragment3;
//                break;
            case R.id.navigation_order:
                fm.beginTransaction().hide(active).show(fragment4).commitAllowingStateLoss();
                active = fragment4;
                break;
            case R.id.navigation_profile:
                fm.beginTransaction().hide(active).show(fragment5).commitAllowingStateLoss();
                active = fragment5;
                break;
            default:

//                return false;
        }
        active.updateStatusBarColor();
        updateTabIcons(item.getItemId());
        return true;
    };

    @AfterViews
    protected void init() {
        fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
//        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container, fragment1, "1").hide(fragment1).commit(); // setting last because of status bar color

        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setActiveTab(R.id.navigation_dash, null);

//        getData(false);
//        getExchangeTickersData();

//        setupPushNotification();
    }

    private void updateTabIcons(int itemId) { // not recommended but because of bad icon designs. it should use opacity instead of color
        navigation.getMenu().findItem(R.id.navigation_dash).setIcon(itemId == R.id.navigation_dash ? R.mipmap.ic_tab_wallet_selected : R.mipmap.ic_tab_wallet);
        navigation.getMenu().findItem(R.id.navigation_buy).setIcon(itemId == R.id.navigation_buy ? R.mipmap.ic_tab_card_selected : R.mipmap.ic_tab_card);
        navigation.getMenu().findItem(R.id.navigation_gift).setIcon(itemId == R.id.navigation_gift ? R.mipmap.ic_tab_gift_selected : R.mipmap.ic_tab_gift);
        navigation.getMenu().findItem(R.id.navigation_order).setIcon(itemId == R.id.navigation_order ? R.mipmap.ic_tab_exchange_selected : R.mipmap.ic_tab_exchange);
        navigation.getMenu().findItem(R.id.navigation_profile).setIcon(itemId == R.id.navigation_profile ? R.mipmap.ic_tab_profile_selected : R.mipmap.ic_tab_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        handler.postDelayed(runnable, Constants.API_REQUEST_INTERVAL_DEFAULT);
    }

    @Override
    protected void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

//        final Constants.VerifyType verifyType = (Constants.VerifyType)intent.getSerializableExtra("verify_type");
//        if (verifyType == null) { // after transaction success
//            getData(true);
//
//            final WalletUtils.TransactionType type = (WalletUtils.TransactionType)intent.getSerializableExtra("type");
//            if (type == WalletUtils.TransactionType.GIFT) {
////                fragment2.getData(false);
//            } else if (type == WalletUtils.TransactionType.EXCHANGE) {
//                // reset values on Exchange screen
////                fragment4.resetAmount();
//            }
//        } else {
//            if (verifyType == Constants.VerifyType.BANK) {
////                fragment5.getAccounts();
//            } else if (verifyType.name().contains("CARD")) { // after card update
////                fragment2.getData(verifyType == Constants.VerifyType.CARD_ACTIVATE); // refresh my cards info
//                if (verifyType == Constants.VerifyType.CARD_ACTIVATE && intent.getStringExtra("pin_type").equals("settable")) {
//                    showPin(intent.getStringExtra("reference"), false);
//                }
//            } else { // after tier level update
//                getUser();
//            }
////            fragment4.resetAmount();
//        }
    }

    private void setupPushNotification() {
//        final String oldToken = ExchangeApplication.getApp().getPreferences().getPushToken();
//        if (oldToken != null) {
//            FirebaseMessaging.getInstance().subscribeToTopic(ExchangeMessagingService.DEFAULT_TOPIC);
//            return;
//        }
//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                return;
//            }
//            final String pushToken = task.getResult().getToken();
//            if (BuildConfig.DEBUG) Toast.makeText(this, pushToken, Toast.LENGTH_SHORT).show();
//
//            final HashMap<String, String> request = new HashMap<>();
//            request.put("service", "gcm");
//            request.put("token", pushToken);
//            request.put("device", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
//            ApiClient.getInterface().registerDevice(request).enqueue(new AppCallback<>(new ApiCallback() {
//                @Override
//                public void onResponse(BaseResponse response) {
//                    ExchangeApplication.getApp().getPreferences().setPushToken(pushToken);
//                    FirebaseMessaging.getInstance().subscribeToTopic(ExchangeMessagingService.DEFAULT_TOPIC);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    if (BuildConfig.DEBUG) Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                }
//            }));
//        });
    }

    public boolean isActiveFragment(BaseFragment fragment) {
        return active == fragment;
    }

    public void setActiveTab(int navigationId, Wallet wallet) {
        navigation.setSelectedItemId(navigationId);
//        if (wallet != null) {
//            if (navigationId == R.id.navigation_dash) {
////                fragment1.updateWalletPosition(wallet);
//            }
//        }
    }

    public void showPin(String reference, boolean isUpdating) { // Create or Update Card PIN Code
//        final PinDialog dialog = new PinDialog_();
//        final Bundle args = new Bundle();
//        args.putBoolean("is_updating", isUpdating);
//        args.putString("reference", reference);
//        dialog.setArguments(args);
//        dialog.show(getSupportFragmentManager(), "PIN");
    }

    private void getExchangeTickersData() {
//        ApiClient.getInterface().getExchangeTickers("blockChain").enqueue(new AppCallback<>(new ApiCallback() {
//            @Override
//            public void onResponse(BaseResponse response) {
////                if (response instanceof GetExchangeTickersResponse) {
////                    AppData.getInstance().setBlockChainTickers(((GetExchangeTickersResponse)response).getData());
////                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        }));
//        ApiClient.getInterface().getExchangeTickers("currency").enqueue(new AppCallback<>(new ApiCallback() {
//            @Override
//            public void onResponse(BaseResponse response) {
////                if (response instanceof GetExchangeTickersResponse) {
////                    AppData.getInstance().setCurrencyTickers(((GetExchangeTickersResponse)response).getData());
////                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        }));
//        ApiClient.getInterface().getExchangeTickers("stableCoin").enqueue(new AppCallback<>(new ApiCallback() {
//            @Override
//            public void onResponse(BaseResponse response) {
////                if (response instanceof GetExchangeTickersResponse) {
////                    AppData.getInstance().setStableCoinTickers(((GetExchangeTickersResponse) response).getData());
////                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        }));
    }

    public void getData(boolean shouldRestartThread) {
//        final User user = ExchangeApplication.getApp().getUser();
//        if (user == null) return;
//        if (true || user.isTierPending()) { // setting true because of setting on other devices, such as default currency
//            getUser();
//        }
//        getSymbols();
//        getMyWallets(false);
//
//        if (AppData.getInstance().getFlowData() == null || AppData.getInstance().getFlowData().getMessage() == null) {
//            ApiClient.getInterface().getBankFlow().enqueue(new AppCallback<>(this));
//        }
//        ApiClient.getInterface().getExchangeTickers("blockChain").enqueue(new AppCallback<>(this)); // TODO: implement currency, stableCoin
//
//        if (shouldRestartThread) {
//            handler.removeCallbacks(runnable);
//            handler.postDelayed(runnable, Constants.API_REQUEST_INTERVAL_DEFAULT);
//
//            // refresh transactions history
////            final FragmentManager fragmentManager = fragment1.getChildFragmentManager();
//            final FragmentManager fragmentManager = fragment4.getChildFragmentManager();
//            if (fragmentManager.getBackStackEntryCount() > 0) {
//                final String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
//                if (fragmentTag == null) return;
//                if (fragmentTag.equals("WalletFragment_")) {
//                    ((WalletFragment)fragmentManager.findFragmentByTag(fragmentTag)).onRefresh();
//                } else if (fragmentTag.equals("PriceDetailFragment_")) {
//                    ((BaseFragment)fragmentManager.findFragmentByTag(fragmentTag)).updateView();
//                }
//            }
//        }
    }

    private void getSymbols() {
//        final User user = ExchangeApplication.getApp().getUser();
//        if (user == null) return;
//        if (false && user.getFavoriteAssets() == 1) {
//            ApiClient.getInterface().getFavoriteSymbols().enqueue(new AppCallback<>(this));
//        } else {
//            ApiClient.getInterface().getSymbolData().enqueue(new AppCallback<>(this));
//        }
    }

    public void getMyWallets(boolean showLoading) {
//        final User user = ExchangeApplication.getApp().getUser();
//        if (user == null) return;
//        if (user.getFavoriteFunds() == 1) {
//            ApiClient.getInterface().getFavoriteWallets().enqueue(new AppCallback<>(this, showLoading, this));
//        } else {
//            ApiClient.getInterface().getWallet().enqueue(new AppCallback<>(this, showLoading, this));
//        }
    }

    private void getUser() {
//        ApiClient.getInterface().getUser().enqueue(new AppCallback<>(this));
    }

    @Override
    public void onResponse(BaseResponse response) {
//        if (response instanceof SymbolResponse) {
//            final List<Symbol> data = ((SymbolResponse)response).getData();
//            AppData.getInstance().setSymbols(data);
//            EventBus.getDefault().post(response);
//        } else if (response instanceof WalletResponse) {
//            final List<Wallet> data = ((WalletResponse)response).getData();
//            if (ExchangeApplication.getApp().getUser().getFavoriteFunds() == 1) {
//                final List<String> sortedSymbols = ExchangeApplication.getApp().getPreferences().getFavoriteSymbols();
//                if (sortedSymbols != null && sortedSymbols.size() == data.size()) {
//                    Collections.sort(data, (wallet1, wallet2) -> Integer.compare(sortedSymbols.indexOf(wallet1.getSymbol()), sortedSymbols.indexOf(wallet2.getSymbol())));
//                }
//                AppData.getInstance().setFavoriteWallets(data);
//            } else {
//                AppData.getInstance().setWallets(data);
//            }
//            for (Wallet wallet : data) {
//                if (wallet.getSymbolData().isCurrency()) {
//                    continue;
//                }
//                if (AppData.getInstance().getMarketCap(wallet.getSymbol()) != null) {
//                    continue;
//                }
//                if (!BuildConfig.DEBUG) {
//                    ApiClient.getInterface()
//                            .getGraphMarketData(wallet.getSymbol(), "minutes", 1440, 4)
//                            .enqueue(new AppCallback<>(new ApiCallback() {
//                                @Override
//                                public void onResponse(BaseResponse response) {
//                                    final Market market = ((MarketResponse)response).getData();
//                                    AppData.getInstance().addMarketCap(wallet.getSymbol(), market);
//                                }
//
//                                @Override
//                                public void onFailure(String message) {
//                                    AppData.getInstance().addMarketCap(wallet.getSymbol(), new Market());
//                                }
//                            }));
//                }
//            }
//        } else if (response instanceof GetFlowResponse) {
//            AppData.getInstance().setFlowData(((GetFlowResponse)response).getData());
//        } else if (response instanceof GetExchangeTickersResponse) {
//            final HashMap<String, ExchangeData> data = ((GetExchangeTickersResponse)response).getData();
//            AppData.getInstance().setBlockChainTickers(data);
//        } else if (response instanceof GetUserResponse) {
//            final User user = ((GetUserResponse)response).getData();
//            if (user == null) { // TODO: when does this happen?
//                return;
//            }
//            if (user.getActivateSXP() == 1 && ExchangeApplication.getApp().getUser().getActivateSXP() == 0 || user.getSXPStatus() == 0 && ExchangeApplication.getApp().getUser().getSXPStatus() == 1) {
//                final Intent intent = new Intent(this, BuySellSuccessActivity_.class);
//                intent.putExtra("type", WalletUtils.TransactionType.NETWORK);
//                intent.putExtra("name", user.getActivateSXP() == 1 ? "activated" : "deactivated");
//                startActivity(intent);
//            }
//            ExchangeApplication.getApp().setUser(user, true);
//        } else {
//
//        }
    }

    @Override
    public void onFailure(String message) {
//        if (BuildConfig.DEBUG) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        if (message != null && message.equals("The user has no wallets available")) {
//            if (ExchangeApplication.getApp().getUser().getFavoriteFunds() == 1) {
//                AppData.getInstance().setFavoriteWallets(new ArrayList<>());
//            } else {
//                AppData.getInstance().setWallets(new ArrayList<>());
//            }
//        }
    }

    @Override
    public void onBackPressed() {
//        final FragmentManager fragmentManager = active.getChildFragmentManager();
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            final String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
//            final BaseFragment fragment = (BaseFragment)fragmentManager.findFragmentByTag(fragmentTag);
//            if (fragment != null) {
//                fragment.onBackPressed();
//            }
//        } else if (navigation.getSelectedItemId() == R.id.navigation_dash) {
//            if (fragment1.getChildFragmentManager().getFragments().size() == 0) return;
//            final BaseFragment fragment = (BaseFragment)fragment1.getChildFragmentManager().getFragments().get(0);
//            if (fragment4.getChildFragmentManager().getFragments().size() == 0) return;
//            final BaseFragment fragment = (BaseFragment)fragment4.getChildFragmentManager().getFragments().get(0);
//            if (fragment instanceof PriceFragment) {
//                ((PriceFragment)fragment).onCloseDrawer();
//            }
//        } else if (BuildConfig.DEBUG) {
//            super.onBackPressed();
//        }
    }
}