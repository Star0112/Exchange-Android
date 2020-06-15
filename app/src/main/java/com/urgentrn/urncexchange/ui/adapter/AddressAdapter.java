package com.urgentrn.urncexchange.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.contacts.WalletAddress;
import com.urgentrn.urncexchange.ui.holder.AddressHolder;
import com.urgentrn.urncexchange.utils.WalletUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressHolder> {

    private List<WalletAddress> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public AddressAdapter(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);

        return new AddressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder viewHolder, int position) {
        final WalletAddress wallet = getItem(position);
        final Symbol symbol = WalletUtils.getSymbolData(wallet.getAssetId());
        if (symbol == null) return;

        viewHolder.txtCoin.setText(symbol.getTitle());
        viewHolder.llDelete.setOnClickListener(v -> {
            data.remove(position);
            notifyDataSetChanged();
        });
        viewHolder.editAddress.setHint(String.format("%s %s", symbol.getTitle(), viewHolder.itemView.getResources().getString(R.string.wallet_address)));
        viewHolder.editAddress.setText(wallet.getAddress());
        viewHolder.editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getItem(viewHolder.getAdapterPosition()).setAddress(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.imgQRCode.setOnClickListener(v -> mListener.onItemClick(position));
        if (symbol.getExtra() != null && symbol.getExtra().get("fieldName") != null || wallet.getExtra() != null) {
            viewHolder.editMemo.setVisibility(View.VISIBLE);
            viewHolder.editMemo.setText(wallet.getExtra());
            viewHolder.editMemo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(viewHolder.getAdapterPosition()).setExtra(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            viewHolder.editMemo.setVisibility(View.GONE);
        }
    }

    public WalletAddress getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<WalletAddress> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
