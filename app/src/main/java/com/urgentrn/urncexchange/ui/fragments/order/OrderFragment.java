package com.urgentrn.urncexchange.ui.fragments.order;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.OrderRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_order)
public class OrderFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById
    TextView bidAmount, askAmount;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    EditText buyPrice, buyAmount, sellPrice, sellAmount;

    @ViewById
    TabHost tabHost;

    private List<String> symbolsName = new ArrayList<>();
    private List<MarketInfo> marketInfos = new ArrayList<>();
    private int selectedNumber = 0;
    WebSocketFactory factory;
    WebSocket ws;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_order);
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("BUY");
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("SELL");
        tabHost.addTab(spec);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedNumber = position;
                buyPrice.setText(String.valueOf(marketInfos.get(selectedNumber).getPrice()));
                sellPrice.setText(String.valueOf(marketInfos.get(selectedNumber).getPrice()));
                ws.sendText("{\"id\": 1022, \"method\": \"depth_price.subscribe\", \"params\": [\"" + symbolsName.get(selectedNumber) + "\"]}");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        initSocket();
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

    private void initSocket() {
        try {
            factory = new WebSocketFactory().setConnectionTimeout(Constants.SOCKET_TIMEOUT);
            ws = factory.createSocket(Constants.SOCKET_URI);
            ws.connectAsynchronously();

            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    Log.i("Socket", "onConnected: success");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    onReceiveMessage(text);
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    Log.i("Socket", "onConnected: disconnect");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onReceiveMessage(String message) throws JSONException {
        JSONObject object = new JSONObject(message);
        String method = "depth_price.update";
        if(object.getString("method").equals(method)) {
            JSONObject temp = new JSONObject(object.getJSONArray("params").get(2).toString());
//            bidAmount.setText(temp.getString("bid") + " X " + temp.getString("bid_price"));
            askAmount.setText(temp.getString("ask") + " X " + temp.getString("ask_price"));
        }
    }

    @Override
    public void updateView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        List<AssetBalance> a = data;
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
            onOrderCoin(symbolsName.get(selectedNumber), 1, Integer.parseInt(amount), Float.parseFloat(price), "limit");
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
            onOrderCoin(symbolsName.get(selectedNumber), 1, Integer.parseInt(amount), Float.parseFloat(price), "limit");
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
                    symbolsName.add(marketInfo.getName());
                }
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, symbolsName);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
