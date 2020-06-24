package com.urgentrn.urncexchange.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.DepositHistory;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
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
        holder.itemView.findViewById(R.id.btnAddress).setOnClickListener(v -> {
            final ClipboardManager clipboard = (ClipboardManager)v.getContext().getSystemService((Context.CLIPBOARD_SERVICE));
            final ClipData clipData = ClipData.newPlainText(v.getResources().getString(R.string.address), data.get(position).getAddress());
            clipboard.setPrimaryClip(clipData);
            Toast.makeText(v.getContext(), data.get(position).getAddress(), Toast.LENGTH_SHORT).show();
        });
        holder.itemView.findViewById(R.id.btnDeposit).setOnClickListener(v -> {

        });
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