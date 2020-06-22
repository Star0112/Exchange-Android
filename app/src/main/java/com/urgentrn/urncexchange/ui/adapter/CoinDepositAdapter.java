package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.DepositHistory;
import com.urgentrn.urncexchange.ui.holder.CoinDepositHolder;

import java.util.ArrayList;
import java.util.List;

public class CoinDepositAdapter extends RecyclerView.Adapter<CoinDepositHolder> {
    private List<AssetBalance> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public CoinDepositAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public CoinDepositAdapter(List<AssetBalance> transactions) {
        this.data = transactions;
    }

    @NonNull
    @Override
    public CoinDepositHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_deposit_coin, parent, false);

        return new CoinDepositHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinDepositHolder holder, int position) {
        final AssetBalance assetBalance = data.get(position);
        holder.updateView(assetBalance, position);
        holder.itemView.setOnClickListener(v->mListener.onItemClick(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<AssetBalance> data) {
        this.data = data;
    }

}