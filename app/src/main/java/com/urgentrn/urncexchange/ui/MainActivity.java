package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
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

        setActiveTab(R.id.navigation_dash);

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

    }


    public boolean isActiveFragment(BaseFragment fragment) {
        return active == fragment;
    }

    public void setActiveTab(int navigationId) {
        navigation.setSelectedItemId(navigationId);

    }



    @Override
    public void onResponse(BaseResponse response) {
    }

    @Override
    public void onFailure(String message) {

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
