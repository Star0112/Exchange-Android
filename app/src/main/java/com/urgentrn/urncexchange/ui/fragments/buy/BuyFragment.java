package com.urgentrn.urncexchange.ui.fragments.buy;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

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
    @ViewById
    View btnBuy;

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    EditText buyPrice, buyAmount;

    @ViewById
    RecyclerView assetBalance;

    private List<String> symbols = new ArrayList<>();
    private CoinBalanceAdapter adapterCoin;
    private ArrayList<AssetBalance> assetBalances = new ArrayList<>();
    private List<MarketInfo> marketInfos = new ArrayList<>();

    private int selectedNumber = 0;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_buy);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedNumber = position;
                buyPrice.setText(String.valueOf(marketInfos.get(selectedNumber).getPrice()));
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
                    .buyCoin(new BuyCoinRequest(this.marketInfos.get(this.selectedNumber).getPair(), this.marketInfos.get(this.selectedNumber).getId(), Integer.parseInt(amount)))
                    .enqueue(new AppCallback<BaseResponse>(getContext(), new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            ((BaseActivity)getActivity()).showAlert(R.string.buy_success);
                        }

                        @Override
                        public void onFailure(String message) {
                            ((BaseActivity)getActivity()).showAlert(R.string.buy_failed);
                        }
                    }));
        }
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
        if(response instanceof MarketInfoResponse) {
            final List<MarketInfo> data = ((MarketInfoResponse)response).getData();
            AppData.getInstance().setMarketInfoData(data);
            for(MarketInfo marketInfo : data ) {
                marketInfos.add(marketInfo);
                symbols.add(marketInfo.getName());
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, symbols);
            spinner.setAdapter(spinnerAdapter);
        } else if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
            for (AssetBalance assetBalance : data) {
                assetBalances.add(assetBalance);
            }
            adapterCoin = new CoinBalanceAdapter(pos -> updateCoin(assetBalances.get(pos)));
            adapterCoin.setData(assetBalances);
            assetBalance.setAdapter(adapterCoin);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
