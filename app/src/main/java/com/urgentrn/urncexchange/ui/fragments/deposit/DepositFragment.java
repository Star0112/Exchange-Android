package com.urgentrn.urncexchange.ui.fragments.deposit;


import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.DepositHistory;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.DepositHistoryResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinDepositAdapter;
import com.urgentrn.urncexchange.ui.adapter.DepositHistoryAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_deposit)
public class DepositFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById
    RecyclerView recyclerDepositCoins, depositHistory;

    private CoinDepositAdapter adapterAsset;
    private DepositHistoryAdapter adapterTransaction;

    private List<AssetBalance> assetBalances = new ArrayList<>();
    private List<DepositHistory> depositHistories = new ArrayList<>();

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_deposit);
        recyclerDepositCoins.setHasFixedSize(true);
        recyclerDepositCoins.setLayoutManager(new LinearLayoutManager(getContext()));

        depositHistory.setHasFixedSize(true);
        depositHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        setupDrawer();

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

    private void setupDrawer() {
        ApiClient.getInterface()
                .getDepositHistory(0,30)
                .enqueue(new AppCallback<DepositHistoryResponse>(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        if(data != null) {
            assetBalances.clear();
            for (AssetBalance assetBalance : data) {
                assetBalances.add(assetBalance);

            }
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

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof DepositHistoryResponse) {
            final List<DepositHistory> data = ((DepositHistoryResponse) response).getData();
            if(data != null) {
                for (DepositHistory depositHistory : data) {
                    depositHistories.add(depositHistory);
                }
            }
            adapterTransaction = new DepositHistoryAdapter(depositHistories);
            depositHistory.setAdapter(adapterTransaction);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
