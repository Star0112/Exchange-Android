package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.fragments.price.PriceContainerFragment;
import com.urgentrn.urncexchange.ui.holder.WalletHolder;

import java.util.ArrayList;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletHolder> {

    private Fragment parentFragment;
    private List<Wallet> data = new ArrayList<>();

    public WalletAdapter(Fragment fragment) {
        this.parentFragment = fragment;
    }

    @Override
    @NonNull
    public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_wallet, parent, false);

        return new WalletHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletHolder viewHolder, int position) {
        final Wallet wallet = data.get(position);
        viewHolder.updateView(wallet);

        final Market market = AppData.getInstance().getMarketCap(wallet.getSymbol());
        viewHolder.updateChart(market, wallet.getColor());

        viewHolder.itemView.setOnClickListener(v -> {
            ((PriceContainerFragment)this.parentFragment).updateWalletPosition(getItem(position));
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
}
