package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.models.SendHistory;
import com.urgentrn.urncexchange.ui.holder.BuyHistoryHolder;
import com.urgentrn.urncexchange.ui.holder.SendHistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class SendHistoryAdapter extends RecyclerView.Adapter<SendHistoryHolder> {

    private List<SendHistory> data = new ArrayList<>();

    public SendHistoryAdapter(List<SendHistory> histories) {
        this.data = histories;
    }

    @NonNull
    @Override
    public SendHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_send_history, parent, false);
        return new SendHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SendHistoryHolder holder, int position) {
        final SendHistory sendHistory = data.get(position);
        holder.UpdateView(sendHistory, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
