package com.urgentrn.urncexchange.ui.fragments.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
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
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.OrderRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.intellij.lang.annotations.JdkConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;

@EFragment(R.layout.fragment_order)
public class OrderFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById
    TextView bidAmount, askAmount, buyTotalPrice, sellTotalPrice, myBalance;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById
    EditText buyPrice, buyAmount, sellPrice, sellAmount;

    @ViewById
    TabHost tabHost;

    private ArrayList<AssetBalance> assetBalanceData = new ArrayList<>();
    private List<String> symbolsName = new ArrayList<>();
    private List<MarketInfo> marketInfoData = new ArrayList<>();
    private int selectedNumber = 0;
    private boolean isBuy = true;
    private double myBalanceData = 0;
    private WebSocketFactory factory;
    private WebSocket ws;

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
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                isBuy = tabId.equals("tag1");
                setMyBalance();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedNumber = position;
                buyPrice.setText(String.valueOf(marketInfoData.get(selectedNumber).getPrice()));
                sellPrice.setText(String.valueOf(marketInfoData.get(selectedNumber).getPrice()));
                setMyBalance();
                ws.sendText("{\"id\": 1022, \"method\": \"depth_price.subscribe\", \"params\": [\"" + symbolsName.get(selectedNumber) + "\"]}");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        buyAmount.setText("1");
        sellAmount.setText("1");
        initSocket();
        setupDrawer();
    }

    private void setMyBalance() {
        if(isBuy) {
            for (AssetBalance assetBalance : assetBalanceData) {
                if(marketInfoData.get(selectedNumber).getBase().equals(assetBalance.getCoin())) {
                    myBalanceData = Double.parseDouble(assetBalance.getAvailable());
                    myBalance.setText(formattedNumber(myBalanceData));
                }
            }
        } else {
            for (AssetBalance assetBalance : assetBalanceData) {
                if(marketInfoData.get(selectedNumber).getPair().equals(assetBalance.getCoin())) {
                    myBalanceData = Double.parseDouble(assetBalance.getAvailable());
                    myBalance.setText(formattedNumber(myBalanceData));
                }
            }
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        if(data != null) {
            assetBalanceData.clear();
            assetBalanceData.addAll(data);
        }
        setMyBalance();
    }

    private void updateAskBid(JSONObject object) {
        String method = "depth_price.update";
        try {
            if(object.getString("method").equals(method)) {
                JSONObject temp = new JSONObject(object.getJSONArray("params").get(2).toString());
                String bid = temp.getString("bid") + " X " + temp.getString("bid_price");
                String ask = temp.getString("ask") + " X " + temp.getString("ask_price");

                bidAmount.post(new Runnable() {
                    @Override
                    public void run() {
                        bidAmount.setText(bid);
                        askAmount.setText(ask);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    JSONObject object = new JSONObject(text);
                    if(!object.getString("method").isEmpty()) {
                        updateAskBid(object);
                    }
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

    private void setupDrawer() {
        ApiClient.getInterface()
                .getMarketInfo()
                .enqueue(new AppCallback<MarketInfoResponse>(this));
    }

    private void getAssetBalance() {
        ApiClient.getInterface()
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(this));
    }

    @TextChange(R.id.buyAmount)
    void onBuyChange1(CharSequence s) {
        setBuyTotalPrice();
    }
    @TextChange(R.id.buyPrice)
    void onBuyChange2(CharSequence s) {
        setBuyTotalPrice();
    }
    private void setBuyTotalPrice() {
        double price = !buyPrice.getText().toString().isEmpty()? Double.parseDouble(buyPrice.getText().toString()) : 0;
        int amount = !buyAmount.getText().toString().isEmpty()? Integer.parseInt(buyAmount.getText().toString()) : 0;
        buyTotalPrice.setText(formattedNumber(price * amount));
    }

    @TextChange(R.id.sellAmount)
    void onSellChange1(CharSequence s) {
        setSellTotalPrice();
    }
    @TextChange(R.id.sellPrice)
    void onSellChange2(CharSequence s) {
        setSellTotalPrice();
    }
    private void setSellTotalPrice() {
        double price = !sellPrice.getText().toString().isEmpty()? Double.parseDouble(sellPrice.getText().toString()) : 0;
        int amount = !sellAmount.getText().toString().isEmpty()? Integer.parseInt(sellAmount.getText().toString()) : 0;
        sellTotalPrice.setText(formattedNumber(price * amount));
    }

    @EditorAction(R.id.buyAmount)
    @Click(R.id.btnBuy)
    void onBuy() {
        final String amount = buyAmount.getText().toString();
        final String price = buyPrice.getText().toString();
        buyAmount.setText(String.valueOf(Integer.parseInt(amount)));
        buyPrice.setText(String.valueOf(Double.parseDouble(price)));
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
        } else if(Double.parseDouble(amount) * Double.parseDouble(price) > myBalanceData) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_limited));
        } else
        {
            onOrderCoin(symbolsName.get(selectedNumber), 1, Integer.parseInt(amount), Float.parseFloat(price), "limit");
        }
    }

    @EditorAction(R.id.sellAmount)
    @Click(R.id.btnSell)
    void onSell() {
        final String amount = sellAmount.getText().toString();
        final String price = sellPrice.getText().toString();
        sellAmount.setText(String.valueOf(Integer.parseInt(amount)));
        sellPrice.setText(String.valueOf(Double.parseDouble(price)));
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
        } else if(Double.parseDouble(amount) > myBalanceData) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_limited));
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
                        getAssetBalance();
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
                    marketInfoData.add(marketInfo);
                    symbolsName.add(marketInfo.getName());
                }
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.item_spinner, symbolsName);
            spinner.setAdapter(spinnerAdapter);
        } else if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
