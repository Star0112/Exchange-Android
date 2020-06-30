package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.DepositHistory;
import com.urgentrn.urncexchange.ui.holder.DepositHistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class DepositHistoryAdapter extends RecyclerView.Adapter<DepositHistoryHolder> {

    private List<DepositHistory> data = new ArrayList<>();

    public DepositHistoryAdapter(List<DepositHistory> histories) {
        this.data = histories;
    }

    @NonNull
    @Override
    public DepositHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_deposit_history, parent, false);

        return new DepositHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DepositHistoryHolder holder, int position) {
        final DepositHistory transactionHistory = data.get(position);
        holder.updateView(transactionHistory, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setData(List<DepositHistory> data) {
        this.data = data;
    }
}
