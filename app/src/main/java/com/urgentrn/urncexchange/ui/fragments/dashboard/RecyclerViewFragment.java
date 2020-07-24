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
import com.neovisionaries.ws.client.WebSocketFactory;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.OrderData;
import com.urgentrn.urncexchange.ui.adapter.OrderbookAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_orderbook)
public class RecyclerViewFragment extends ViewPagerSlidingHeaderFragment {

    @ViewById
    RecyclerView recyclerOrderbook;

    private List<OrderData> orderbookData = new ArrayList<>();
    private OrderbookAdapter orderbookAdapter;

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
    }
}