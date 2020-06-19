package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.ui.holder.TransactionHistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryHolder> {

    private List<BuyHistory> data = new ArrayList<>();

    public TransactionHistoryAdapter(List<BuyHistory> transactions) {
        this.data = transactions;
    }

    @NonNull
    @Override
    public TransactionHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_transaction_history, parent, false);

        return new TransactionHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryHolder holder, int position) {
        final BuyHistory transactionHistory = data.get(position);
        holder.updateView(transactionHistory, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setData(List<BuyHistory> data) {
        this.data = data;
    }
}
