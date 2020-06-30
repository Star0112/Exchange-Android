package com.urgentrn.urncexchange.ui.fragments.buy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.SendAssetRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinBalanceAdapter;
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

@EFragment(R.layout.fragment_send)
public class SendFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    RecyclerView assetBalance;


    private List<String> assetsName = new ArrayList<>();
    private List<AssetBalance> assetBalanceData = new ArrayList<>();
    private AssetBalance selectedAsset;
    private CoinBalanceAdapter adapterCoin;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_send);
        assetBalance.setHasFixedSize(true);
        assetBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        setupDrawer();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAsset = assetBalanceData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setupDrawer() {
        ApiClient.getInterface()
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(this));
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

    @Override
    public void updateView() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {

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
        if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
            if(data != null) {
                for (AssetBalance assetBalance : data) {
                    assetBalanceData.add(assetBalance);
                    assetsName.add(assetBalance.getCoin());
                }
            }
            adapterCoin = new CoinBalanceAdapter(getChildFragmentManager(), pos -> assetBalanceData.get(pos));
            adapterCoin.setData(assetBalanceData);
            assetBalance.setAdapter(adapterCoin);

            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, assetsName);
            spinner.setAdapter(spinnerAdapter);
        }

    }

    @Override
    public void onFailure(String message) {

    }

    void onSend() {
//        ApiClient.getInterface()
//                .sendByEmail(new SendAssetRequest(1,1,["aa"]))
    }
}
