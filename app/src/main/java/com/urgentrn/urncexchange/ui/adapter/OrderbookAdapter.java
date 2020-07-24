package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.OrderData;
import com.urgentrn.urncexchange.ui.holder.OrderbookHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderbookAdapter extends RecyclerView.Adapter<OrderbookHolder> {

    private List<OrderData> data = new ArrayList<>();

    public OrderbookAdapter(List<OrderData> histories) {
        this.data = histories;
    }

    @NonNull
    @Override
    public OrderbookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_orderbook, parent, false);

        return new OrderbookHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderbookHolder holder, int position) {
        final OrderData orderData = data.get(position);
        holder.updateView(orderData, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }
}
