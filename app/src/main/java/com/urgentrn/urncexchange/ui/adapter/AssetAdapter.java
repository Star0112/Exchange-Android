package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.AssetHolder;

import java.util.ArrayList;
import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetHolder> {

    private List<Wallet> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public AssetAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    @NonNull
    public AssetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_asset, parent, false);

        return new AssetHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetHolder viewHolder, int position) {
        final Wallet wallet = data.get(position);
        viewHolder.updateView(wallet);

        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Wallet> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
