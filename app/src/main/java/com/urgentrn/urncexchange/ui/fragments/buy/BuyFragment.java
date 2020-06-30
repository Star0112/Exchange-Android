package com.urgentrn.urncexchange.ui.fragments.buy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinBalanceAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Utils;

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

@EFragment(R.layout.fragment_buy)
public class BuyFragment extends BaseFragment implements ApiCallback {
    @ViewById(R.id.toolBar)
    Toolbar toolBar;

    private MenuItem btnSend;

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    EditText buyPrice, buyAmount;

    @ViewById
    RecyclerView assetBalance;

    private List<String> symbolsName = new ArrayList<>();
    private CoinBalanceAdapter adapterCoin;
    private ArrayList<AssetBalance> assetBalanceData = new ArrayList<>();
    private List<MarketInfo> marketInfoData = new ArrayList<>();

    private MarketInfo selectedAsset;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_buy);
        btnSend = toolBar.getMenu()
                .add(R.string.title_send)
                .setIcon(R.mipmap.ic_send_inactive)
                .setOnMenuItemClickListener(item -> onSend());
        btnSend.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAsset = marketInfoData.get(position);
                buyPrice.setText(String.valueOf(selectedAsset.getPrice()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        assetBalance.setHasFixedSize(true);
        assetBalance.setLayoutManager(new LinearLayoutManager(getContext()));

        setupDrawer();
        updateView();
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
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(this));

        ApiClient.getInterface()
                .getMarketInfo()
                .enqueue(new AppCallback<MarketInfoResponse>(this));
    }

    public void updateCoin(AssetBalance coin){

    }

    @Override
    public void updateView() {

    }

    @EditorAction(R.id.buyAmount)
    @Click(R.id.btnBuy)
    void onBuy() {
        final String amount = buyAmount.getText().toString();

        if (amount.isEmpty()) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_empty));
        } else if (Integer.parseInt(amount)<=0) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_invalid));
        } else {
            ApiClient.getInterface()
                    .buyCoin(new BuyCoinRequest(this.selectedAsset.getPair(), this.selectedAsset.getId(), Integer.parseInt(amount)))
                    .enqueue(new AppCallback<BaseResponse>(getContext(), new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            ((BaseActivity)getActivity()).showAlert(R.string.buy_success);
                        }

                        @Override
                        public void onFailure(String message) {
                        }
                    }));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean onSend() {
        ((BaseFragment)getParentFragment()).replaceFragment(new SendFragment_(), false);
        return true;
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof MarketInfoResponse) {
            final List<MarketInfo> data = ((MarketInfoResponse)response).getData();
            AppData.getInstance().setMarketInfoData(data);
            if(data != null) {
                for (MarketInfo marketInfo : data) {
                    marketInfoData.add(marketInfo);
                    symbolsName.add(marketInfo.getName());
                }
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, symbolsName);
            spinner.setAdapter(spinnerAdapter);
        } else if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
            if(data != null) {
                for (AssetBalance assetBalance : data) {
                    assetBalanceData.add(assetBalance);
                }
            }
            adapterCoin = new CoinBalanceAdapter(getChildFragmentManager(), pos -> updateCoin(assetBalanceData.get(pos)));
            adapterCoin.setData(assetBalanceData);
            assetBalance.setAdapter(adapterCoin);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
