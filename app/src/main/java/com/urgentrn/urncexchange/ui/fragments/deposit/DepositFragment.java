package com.urgentrn.urncexchange.ui.fragments.deposit;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.CoinBalance;
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

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById
    RecyclerView recyclerDepositCoins;




    private CoinDepositAdapter adapterCoin;
    private ArrayList<CoinBalance> tempCoins = new ArrayList<>();

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_deposit);
        tempCoins.add(new CoinBalance("cypto","URNC","0x01029dko4", "51000","24453"));
        tempCoins.add(new CoinBalance("cypto","BTC","0x324dfds54", "25000","68253"));
        tempCoins.add(new CoinBalance("cypto","ETH","0xf102a5ko4", "30300","27423"));
        tempCoins.add(new CoinBalance("currency","USD","0xh1i2cdko4", "60200","26451"));
        tempCoins.add(new CoinBalance("cypto","COIN","0xu10d9dko4", "70400","18253"));
        setupDrawer();
//        updateView(null);
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

        recyclerDepositCoins.setHasFixedSize(true);
        recyclerDepositCoins.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCoin = new CoinDepositAdapter(pos -> updateCoin(tempCoins.get(pos)));
        adapterCoin.setData(tempCoins);
        recyclerDepositCoins.setAdapter(adapterCoin);
    }

    public void updateCoin(CoinBalance coin){

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
