package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.ui.holder.SelectAssetHolder;

import java.util.ArrayList;
import java.util.List;

public class SelectAssetAdapter extends RecyclerView.Adapter<SelectAssetHolder> {

    private List<AssetBalance> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public SelectAssetAdapter(OnItemClickListener listener, List<AssetBalance> assetBalances) {
        mListener = listener;
        this.data = assetBalances;
    }

    @NonNull
    @Override
    public SelectAssetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_select_asset, parent, false);

        return new SelectAssetHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAssetHolder holder, int position) {
        final AssetBalance assetBalance = data.get(position);
        holder.updateView(assetBalance, position);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null)
                mListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
