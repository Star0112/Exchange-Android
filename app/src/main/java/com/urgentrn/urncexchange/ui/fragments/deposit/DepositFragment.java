package com.urgentrn.urncexchange.ui.fragments.deposit;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.DepositCoin;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinDepositAdapter;
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
    @ViewById
    View llBackground;

    @ViewById
    RecyclerView recyclerDepositCoins;


    ////////////////////////////
//    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ////////////////////////////

    private CoinDepositAdapter adapterCoin;
    private ArrayList<DepositCoin> tempCoins = new ArrayList<>();
    @AfterViews
    protected void init() {
        setupDrawer();
//        updateView(null);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerDepositCoins.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerDepositCoins.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            input.add("Test" + i);
        }// define an adapter
        adapterCoin = new CoinDepositAdapter(input);
        recyclerDepositCoins.setAdapter(adapterCoin);
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
//        for (int i = 1; i <= 10; i++) {
//            tempCoins.add(new DepositCoin("", "", "", "", ""));
//        }
//        this.adapterCoin = new CoinDepositAdapter(tempCoins);
//        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
//        recyclerDepositCoins.setLayoutManager(layoutManager2);
//        recyclerDepositCoins.setAdapter(adapterCoin);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickersUpdated(HashMap<String, ExchangeData> data) {

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

    }

    @Override
    public void onFailure(String message) {

    }
}
