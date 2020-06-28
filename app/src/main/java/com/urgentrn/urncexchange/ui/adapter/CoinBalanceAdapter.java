package com.urgentrn.urncexchange.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.ui.dialogs.BuyHistoryDialog;
import com.urgentrn.urncexchange.ui.dialogs.BuyHistoryDialog_;
import com.urgentrn.urncexchange.ui.holder.CoinBalanceHolder;

import java.util.ArrayList;
import java.util.List;

public class CoinBalanceAdapter extends RecyclerView.Adapter<CoinBalanceHolder> {
    private final BuyHistoryDialog dialog = new BuyHistoryDialog_();
    private FragmentManager parentFragmentManager;
    private List<AssetBalance> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public CoinBalanceAdapter(FragmentManager parentFragment, OnItemClickListener listener) {
        this.parentFragmentManager = parentFragment;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CoinBalanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_coin_balance, parent, false);

        return new CoinBalanceHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinBalanceHolder holder, int position) {
        final AssetBalance assetBalance = data.get(position);
        holder.updateView(assetBalance, position);
        holder.itemView.setOnClickListener(v -> {
            if(dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
            final Bundle args = new Bundle();
            args.putString("coin", "BTC");
            dialog.setArguments(args);
            dialog.show(parentFragmentManager, "ETH");
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