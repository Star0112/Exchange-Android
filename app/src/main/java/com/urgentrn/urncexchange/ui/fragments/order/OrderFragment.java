package com.urgentrn.urncexchange.ui.fragments.order;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.OrderRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
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

@EFragment(R.layout.fragment_order)
public class OrderFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    EditText buyPrice, buyAmount, sellPrice, sellAmount;

    private List<String> symbols = new ArrayList<>();
    private List<MarketInfo> marketInfos = new ArrayList<>();

    private int selectedNumber = 0;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_order);

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

        initView();
        updateView();
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

    private void initView() {}

    @Override
    public void updateView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {

    }

    private void setupDrawer() {
        ApiClient.getInterface()
                .getMarketInfo()
                .enqueue(new AppCallback<MarketInfoResponse>(this));
    }

    @EditorAction(R.id.buyAmount)
    @Click(R.id.btnBuy)
    void onBuy() {
        final String amount = buyAmount.getText().toString();
        final String price = buyPrice.getText().toString();

        if (amount.isEmpty()) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_empty));
        } else if (Integer.parseInt(amount) <= 0) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_invalid));
        } else if (price.isEmpty()) {
            buyPrice.requestFocus();
            buyPrice.setError(getString(R.string.error_amount_empty));
        } else if (Float.parseFloat(price) <= 0) {
            buyPrice.requestFocus();
            buyPrice.setError(getString(R.string.error_amount_invalid));
        } else {
            onOrderCoin(symbols.get(selectedNumber), 1, Integer.parseInt(amount), Float.parseFloat(price), "limit");
        }
    }

    @EditorAction(R.id.sellAmount)
    @Click(R.id.btnSell)
    void onSell() {
        final String amount = sellAmount.getText().toString();
        final String price = sellPrice.getText().toString();

        if (amount.isEmpty()) {
            sellAmount.requestFocus();
            sellAmount.setError(getString(R.string.error_amount_empty));
        } else if (Integer.parseInt(amount) <= 0) {
            sellAmount.requestFocus();
            sellAmount.setError(getString(R.string.error_amount_invalid));
        } else if (price.isEmpty()) {
            sellPrice.requestFocus();
            sellPrice.setError(getString(R.string.error_amount_empty));
        } else if (Float.parseFloat(price) <= 0) {
            sellPrice.requestFocus();
            sellPrice.setError(getString(R.string.error_amount_invalid));
        } else {
            onOrderCoin(symbols.get(selectedNumber), 1, Integer.parseInt(amount), Float.parseFloat(price), "limit");
        }
    }

    private void onOrderCoin (String market, int side, int amount, float price, String type) {
        ApiClient.getInterface()
                .orderCoin(new OrderRequest(market, side, amount, price, type ))
                .enqueue(new AppCallback<BaseResponse>( getContext(), new ApiCallback() {
                    @Override
                    public void onResponse(BaseResponse response) {
                        ((BaseActivity)getActivity()).showAlert(R.string.order_success);
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                }));
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
            if(data != null) {
                for (MarketInfo marketInfo : data) {
                    marketInfos.add(marketInfo);
                    symbols.add(marketInfo.getName());
                }
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, symbols);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
