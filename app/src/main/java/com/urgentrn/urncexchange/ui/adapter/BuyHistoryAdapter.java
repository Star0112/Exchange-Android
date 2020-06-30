package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.ui.holder.BuyHistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class BuyHistoryAdapter extends RecyclerView.Adapter<BuyHistoryHolder> {

    private List<BuyHistory> data = new ArrayList<>();

    public BuyHistoryAdapter(List<BuyHistory> histories) {
        this.data = histories;
    }

    @NonNull
    @Override
    public BuyHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_buy_history, parent, false);
        return new BuyHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyHistoryHolder holder, int position) {
        final BuyHistory buyHistory = data.get(position);
        holder.UpdateView(buyHistory, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
