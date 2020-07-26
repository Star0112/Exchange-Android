package com.urgentrn.urncexchange.ui.fragments.deposit;


import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravbhola.viewpagerslidingheader.SlidingHeaderActivityCallbacks;
import com.gauravbhola.viewpagerslidingheader.SlidingHeaderCallbacks;
import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderRootView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.layout.StarPagerSlidingHeaderRootView;
import com.urgentrn.urncexchange.layout.StarSlidingTabLayout;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.DepositHistory;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinDepositAdapter;
import com.urgentrn.urncexchange.ui.adapter.DepositHistoryAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_deposit)
public class DepositFragment extends BaseFragment implements ApiCallback {

    @ViewById
    StarPagerSlidingHeaderRootView mRootView;

    @ViewById
    Toolbar mToolBar;

    @ViewById
    FrameLayout mPagerContainer;

    @ViewById
    LinearLayout mSlidingLayout;

    @ViewById
    FrameLayout llHeader;

    @ViewById
    RecyclerView recyclerDepositCoins, depositHistory;

    private int limit = 20;
    private int offset = 0;

    private CoinDepositAdapter adapterAsset;
    private DepositHistoryAdapter adapterTransaction;

    private List<AssetBalance> assetBalances = new ArrayList<>();
    private List<DepositHistory> depositHistories = new ArrayList<>();

    @AfterViews
    protected void init() {
        mRootView.setBackgroundResource(R.mipmap.background);
        recyclerDepositCoins.setHasFixedSize(true);
        recyclerDepositCoins.setLayoutManager(new LinearLayoutManager(getContext()));
        depositHistory.setHasFixedSize(true);
        depositHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        getAssetBalance();
        setupDrawer(offset, limit);

        setupViewPager();
        prepareViewPagerSlidingHeader();
    }

    private void setupViewPager() {

    }

    private void prepareViewPagerSlidingHeader() {
        mRootView.initHeaderViewPager(null, llHeader, mSlidingLayout, mPagerContainer);
        mRootView.setParallaxFactor(4);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupDrawer(int offset, int limit) {
        ApiClient.getInterface()
                .getDepositHistory(offset, limit)
                .enqueue(new AppCallback<DepositHistoryResponse>(this));
    }

    private void getAssetBalance() {
        ApiClient.getInterface()
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        if(data != null) {
            assetBalances.clear();
            assetBalances.addAll(data);
        }
        adapterAsset = new CoinDepositAdapter(getParentFragment(), pos -> assetBalances.get(pos));
        adapterAsset.setData(assetBalances);
        recyclerDepositCoins.setAdapter(adapterAsset);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Click(R.id.btnSend)
    void onSend() {
        ((BaseFragment)getParentFragment()).replaceFragment(new SendFragment_(), false);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof DepositHistoryResponse) {
            final List<DepositHistory> data = ((DepositHistoryResponse) response).getData();
            if(data != null) {
                depositHistories.addAll(data);
                if(data.size() == 20) {
                    offset += limit;
                    setupDrawer(offset, limit);
                }
            }
            if(offset == 0 || data.size() < 20) {
                adapterTransaction = new DepositHistoryAdapter(depositHistories);
                depositHistory.setAdapter(adapterTransaction);
            }
        } else if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
        }
    }

    @Override
    public void onFailure(String message) {

    }

}
