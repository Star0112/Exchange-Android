package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.fragments.buy.BuyFragment_;
import com.urgentrn.urncexchange.ui.fragments.deposit.DepositContainerFragment_;
import com.urgentrn.urncexchange.ui.fragments.dashboard.DashboardFragment_;
import com.urgentrn.urncexchange.ui.fragments.order.OrderFragment_;
import com.urgentrn.urncexchange.ui.fragments.setting.SettingContainerFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ApiCallback {

    @ViewById(R.id.navigation)
    BottomNavigationView navigation;

    private final DashboardFragment_ fragment1 = new DashboardFragment_();
    private final DepositContainerFragment_ fragment2 = new DepositContainerFragment_();
    private final BuyFragment_ fragment3 = new BuyFragment_();
    private final OrderFragment_ fragment4 = new OrderFragment_();
    private final SettingContainerFragment_ fragment5 = new SettingContainerFragment_();
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
            case R.id.navigation_deposit:
                fm.beginTransaction().hide(active).show(fragment2).commitAllowingStateLoss();
                active = fragment2;
                break;
            case R.id.navigation_buy:
                fm.beginTransaction().hide(active).show(fragment3).commitAllowingStateLoss();
                active = fragment3;
                break;
            case R.id.navigation_order:
                fm.beginTransaction().hide(active).show(fragment4).commitAllowingStateLoss();
                active = fragment4;
                break;
            case R.id.navigation_setting:
                fm.beginTransaction().hide(active).show(fragment5).commitAllowingStateLoss();
                active = fragment5;
                break;
            default:
                return false;
        }
        active.updateStatusBarColor();
        updateTabIcons(item.getItemId());
        return true;
    };

    @AfterViews
    protected void init() {
        fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
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
        navigation.getMenu().findItem(R.id.navigation_dash).setIcon(itemId == R.id.navigation_dash ? R.mipmap.ic_tab_dashboard : R.mipmap.ic_tab_dashboard_inactive);
        navigation.getMenu().findItem(R.id.navigation_deposit).setIcon(itemId == R.id.navigation_deposit ? R.mipmap.ic_tab_deposit : R.mipmap.ic_tab_deposit_inactive);
        navigation.getMenu().findItem(R.id.navigation_buy).setIcon(itemId == R.id.navigation_buy ? R.mipmap.ic_tab_buy : R.mipmap.ic_tab_buy_inactive);
        navigation.getMenu().findItem(R.id.navigation_order).setIcon(itemId == R.id.navigation_order ? R.mipmap.ic_tab_order : R.mipmap.ic_tab_order_inactive);
        navigation.getMenu().findItem(R.id.navigation_setting).setIcon(itemId == R.id.navigation_setting ? R.mipmap.ic_tab_setting : R.mipmap.ic_tab_setting_inactive);
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
        final FragmentManager fragmentManager = active.getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            final String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            final BaseFragment fragment = (BaseFragment)fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null) {
                fragment.onBackPressed();
            }
        }
    }
}
