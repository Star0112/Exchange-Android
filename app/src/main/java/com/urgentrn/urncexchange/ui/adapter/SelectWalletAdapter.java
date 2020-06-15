package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.CoinHolder;
import com.urgentrn.urncexchange.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SelectWalletAdapter extends RecyclerView.Adapter<CoinHolder> {

    private List<Wallet> data = new ArrayList<>();
    private Wallet selectedWallet; // not using selectedId because of search
    private List<Integer> selectedIds = new ArrayList<>();
    private boolean multiSelectable, showBalance = true, showCurrency;
    private OnItemClickListener mListener;

    public SelectWalletAdapter(OnItemClickListener listener) {
        this(listener, null);
    }

    public SelectWalletAdapter(OnItemClickListener listener, List<Wallet> data) {
        this(listener, data, false);
    }

    public SelectWalletAdapter(OnItemClickListener listener, List<Wallet> data, boolean showCurrency) {
        this.mListener = listener;
        if (data != null) this.data = data;
        this.showCurrency = showCurrency;
    }

    public void showBalance(boolean showBalance) {
        this.showBalance = showBalance;
    }

    public void setMultiSelectable(boolean multiSelectable) {
        this.multiSelectable = multiSelectable;
    }

    @Override
    @NonNull
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_select_wallet, parent, false);

        return new CoinHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder viewHolder, int position) {
        final Wallet wallet = getItem(position);
        viewHolder.setIcon(wallet.getSymbolData().getColoredImage());
        viewHolder.setTitle(wallet.getTitle());
        viewHolder.setBalance(showBalance ? Utils.formattedNumber(wallet.getBalance(), 0, 5) + " " + wallet.getSymbol() : null);
        viewHolder.setBalanceCurrency(showCurrency ? wallet.getBalanceCurrencyFormatted() : null);
        if (multiSelectable) {
            viewHolder.setChecked(selectedIds.contains(wallet.getSymbolData().getId()));
        } else {
            viewHolder.setChecked(wallet == this.selectedWallet);
            ((ImageView)viewHolder.itemView.findViewById(R.id.imgCheck)).setImageResource(R.mipmap.ic_check);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) mListener.onItemClick(position);
            setSelectedPosition(position);
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
        if (multiSelectable) {
            final int assetId = getItem(position).getSymbolData().getId();
            if (selectedIds.contains(assetId)) {
                selectedIds.remove((Integer)assetId);
            } else {
                selectedIds.add(assetId);
            }
            notifyItemChanged(position);
        } else {
            this.selectedWallet = getItem(position);
            notifyDataSetChanged();
        }
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }
}
