package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderFragment;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.OrderData;
import com.urgentrn.urncexchange.ui.adapter.OrderbookAdapter;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.urgentrn.urncexchange.utils.Utils.doubleArraySort;
import static com.urgentrn.urncexchange.utils.Utils.doubleArraySortReverse;

@EFragment(R.layout.fragment_orderbook)
public class RecyclerViewFragment extends ViewPagerSlidingHeaderFragment {

    @ViewById
    RecyclerView recyclerOrderbook;

    private List<OrderData> orderbookData = new ArrayList<>();
    private OrderbookAdapter orderbookAdapter;

    private double[][] buyArray = new double[30][2];
    private double[][] sellArray = new double[30][2];


    private WebSocketFactory webSocketFactory;
    private WebSocket webSocket;

    public static RecyclerViewFragment newInstance() {
        Bundle args = new Bundle();
        RecyclerViewFragment_ fragment = new RecyclerViewFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View getScrollableView() {
        return recyclerOrderbook;
    }

    @AfterViews
    protected void init() {
        int i = 10;
        do {
            orderbookData.add(new OrderData(123,123,123,123));
            i--;
        } while (i > 0);
        recyclerOrderbook.setHasFixedSize(true);
        recyclerOrderbook.setLayoutManager(new LinearLayoutManager(getContext()));
        orderbookAdapter = new OrderbookAdapter(orderbookData);
        recyclerOrderbook.setAdapter(orderbookAdapter);
        initSocket();
    }

    private void updateOrderbook(JSONObject object) {
        String methodDeal = "deals.update";
        try {
            if (object.getString("method").equals(methodDeal)) {
                if(object.getJSONArray("params") == null) return;
                JSONArray data = object.getJSONArray("params");
                JSONArray dealData = data.getJSONArray(2);
                if (data.get(0).toString().equals("0")) {
                    return;
                } else if (data.get(0).toString().equals("1")) {
                    int buyCount = 0, sellCount = 0;
                    for (int loop = 0; loop < 100; loop++) {
                        if (dealData.getJSONObject(loop).getString("type").equals("sell")) {
                            if (sellCount >= 30) return;
                            sellArray[sellCount][0] = dealData.getJSONObject(loop).getDouble("price");
                            sellArray[sellCount][1] = dealData.getJSONObject(loop).getDouble("amount");
                            sellCount++;
                        } else {
                            if (buyCount >= 30) return;
                            buyArray[buyCount][0] = dealData.getJSONObject(loop * 2 + 1).getDouble("price");
                            buyArray[buyCount][1] = dealData.getJSONObject(loop * 2 + 1).getDouble("amount");
                            buyCount++;
                        }
                    }
                    sellArray = doubleArraySort(sellArray);
                    buyArray = doubleArraySortReverse(buyArray);
                } else {

                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSocket() {
        try {
            webSocketFactory = new WebSocketFactory().setConnectionTimeout(Constants.SOCKET_TIMEOUT);
            webSocket = webSocketFactory.createSocket(Constants.SOCKET_URI);
            webSocket.connectAsynchronously();

            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    webSocket.sendText("{\"id\": 1011, \"method\": \"deals.subscribe\", \"params\": [\"URNCUSD\"]}");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    JSONObject object = new JSONObject(text);
                    if(!object.getString("method").isEmpty()) {
                        updateOrderbook(object);
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
}