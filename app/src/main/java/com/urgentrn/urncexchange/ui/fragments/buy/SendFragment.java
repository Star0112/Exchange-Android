package com.urgentrn.urncexchange.ui.fragments.buy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.SendAssetRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
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


    private List<String> AssetsName = new ArrayList<>();
    private List<AssetBalance> AssetsInfo = new ArrayList<>();
    private AssetBalance selectedAsset;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_send);
        AppData.getInstance().getAssetBalanceData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAsset = AssetsInfo.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
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

    }

    @Override
    public void onFailure(String message) {

    }

    void onSend() {
//        ApiClient.getInterface()
//                .sendByEmail(new SendAssetRequest(1,1,["aa"]))
    }
}
