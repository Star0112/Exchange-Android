package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.RecentWalletAddress;
import com.urgentrn.urncexchange.ui.holder.WalletAddressHolder;

import java.util.ArrayList;
import java.util.List;

public class WalletAddressAdapter extends RecyclerView.Adapter<WalletAddressHolder> {

    private List<RecentWalletAddress> data = new ArrayList<>();
    private OnItemClickListener mListener;
    private boolean sendEnabled;

    public WalletAddressAdapter(OnItemClickListener listener) {
        this(listener, false);
    }

    public WalletAddressAdapter(OnItemClickListener listener, boolean sendEnabled) {
        this.mListener = listener;
        this.sendEnabled = sendEnabled;
    }

    @Override
    @NonNull
    public WalletAddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_wallet_address, parent, false);

        return new WalletAddressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletAddressHolder viewHolder, int position) {
        final RecentWalletAddress address = data.get(position);
        viewHolder.updateView(address);

        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    public RecentWalletAddress getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<RecentWalletAddress> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
