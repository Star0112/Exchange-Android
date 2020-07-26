package com.urgentrn.urncexchange.ui.fragments.deposit;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.request.SendAssetRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.SendCoinBalanceAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.urgentrn.urncexchange.utils.Utils.isPasswordValid;

@EFragment(R.layout.fragment_send)
public class SendFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    RecyclerView assetBalance;

    @ViewById
    EditText sendEmail, sendAmount;


    private List<String> assetsName = new ArrayList<>();
    private List<AssetBalance> assetBalanceData = new ArrayList<>();
    private AssetBalance selectedAsset;
    private SendCoinBalanceAdapter adapterCoin;

    @AfterViews
    protected void init() {
        setToolBar(true);
        assetBalance.setHasFixedSize(true);
        assetBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAsset = assetBalanceData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        getAssetBalance();
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

    private void getAssetBalance() {
        ApiClient.getInterface()
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(getContext(),this));
    }

    @EditorAction(R.id.sendAmount)
    @Click(R.id.btnSend)
    void onSend() {
        final String email = sendEmail.getText().toString();
        final String amount = sendAmount.getText().toString();
        String[] emails = new String[1];
        emails[0] = email;
        sendAmount.setText(String.valueOf(Integer.parseInt(amount)));
        if(email.isEmpty()) {
            sendEmail.requestFocus();
            sendEmail.setError(getString(R.string.error_email_empty));
        } else if(!isPasswordValid(email)) {
            sendEmail.requestFocus();
            sendEmail.setError(getString(R.string.error_email_invalid));
        } else if(amount.isEmpty()) {
            sendAmount.requestFocus();
            sendAmount.setError(getString(R.string.error_amount_empty));
        } else if(Integer.parseInt(amount)<=0) {
            sendAmount.requestFocus();
            sendAmount.setError(getString(R.string.error_amount_invalid));
        } else if(Integer.parseInt(amount) > Double.parseDouble(selectedAsset.getAvailable())) {
            sendAmount.requestFocus();
            sendAmount.setError(getString(R.string.error_amount_limited));
        } else {
            ApiClient.getInterface()
                    .sendByEmail(new SendAssetRequest(selectedAsset.getAssetId(), Integer.parseInt(amount), emails))
                    .enqueue(new AppCallback<BaseResponse>(getContext(), new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            getAssetBalance();
                            ((BaseActivity)getActivity()).showAlert(R.string.send_success);
                        }
                        @Override
                        public void onFailure(String message) {

                        }
                    }));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        if(data != null) {
            assetBalanceData.clear();
            assetsName.clear();
            for (AssetBalance assetBalance : data) {
                assetBalanceData.add(assetBalance);
                assetsName.add(assetBalance.getCoin());
            }
        }
        adapterCoin = new SendCoinBalanceAdapter(getChildFragmentManager(), pos -> assetBalanceData.get(pos));
        adapterCoin.setData(assetBalanceData);
        assetBalance.setAdapter(adapterCoin);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, assetsName);
        spinner.setAdapter(spinnerAdapter);
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
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
