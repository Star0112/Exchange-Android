package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.CoinHolder;

import java.util.ArrayList;
import java.util.List;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<CoinHolder> {

    private List<Wallet> data = new ArrayList<>();
    private int selectedPosition = -1;
    private OnItemClickListener mListener;

    public SelectCurrencyAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    @NonNull
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_select_currency, parent, false);

        return new CoinHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder viewHolder, int position) {
        final Wallet wallet = getItem(position);
        viewHolder.updateView(wallet);
        viewHolder.setTitle(wallet.getTitle() + " " + viewHolder.itemView.getResources().getString(R.string.title_wallet));
        viewHolder.setChecked(position == selectedPosition);

        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) mListener.onItemClick(position);
        });
    }

    public Wallet getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Wallet> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
}
