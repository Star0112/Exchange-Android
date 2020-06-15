package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.CoinHolder;
import com.urgentrn.urncexchange.ui.holder.HeaderHolder;
import com.urgentrn.urncexchange.ui.holder.TransactionHolder;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends UltimateViewAdapter {

    private List<Wallet> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public CoinAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public HeaderHolder newHeaderHolder(View view) {
        return new HeaderHolder(view);
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public TransactionHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    @NonNull
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_coin, parent, false);

        return new CoinHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final int currentType = getItemViewType(position);
        if (currentType == VIEW_TYPES.NORMAL) {
            final Wallet wallet = data.get(position);
            ((CoinHolder)viewHolder).updateView(wallet);

            viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
        }
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    public Wallet getItem(int position) {
        return data.get(position);
    }

    public List<Wallet> getData() {
        return data;
    }

    public void setData(List<Wallet> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
