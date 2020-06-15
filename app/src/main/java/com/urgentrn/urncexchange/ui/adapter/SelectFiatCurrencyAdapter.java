package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.ui.holder.CurrencyHolder;

import java.util.ArrayList;
import java.util.List;

public class SelectFiatCurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder> {

    private List<Symbol> data = new ArrayList<>();
    private int selectedPosition = -1;
    private List<String> selectedSymbols = new ArrayList<>();
    private boolean multiSelectable;
    private OnItemClickListener mListener;

    public SelectFiatCurrencyAdapter(OnItemClickListener listener, boolean multiSelectable) {
        this.mListener = listener;
        this.multiSelectable = multiSelectable;
    }

    @Override
    @NonNull
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_select_currency, parent, false);

        return new CurrencyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder viewHolder, int position) {
        final Symbol symbol = getItem(position);
        viewHolder.updateView(symbol);
        if (multiSelectable) {
            viewHolder.setChecked(selectedSymbols.contains(symbol.getSymbol()));
            ((ImageView)viewHolder.itemView.findViewById(R.id.imgCheck)).setImageResource(R.mipmap.ic_checkmark);
        } else {
            viewHolder.setChecked(position == selectedPosition);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) mListener.onItemClick(position);
            setSelectedPosition(position);
        });
    }

    public Symbol getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Symbol> data) {
        this.data = data;
        if (!multiSelectable && selectedPosition == -1) {
            for (int i = 0; i < data.size(); i ++) {
                if (data.get(i).getId() == ExchangeApplication.getApp().getUser().getCurrencyAssetId()) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        if (multiSelectable) {
            final String symbol = getItem(position).getSymbol();
            if (selectedSymbols.contains(symbol)) {
                if (getItem(position).getId() == ExchangeApplication.getApp().getUser().getCurrencyAssetId()) { // cannot remove default currency
                    return;
                }
                selectedSymbols.remove(symbol);
            } else {
                selectedSymbols.add(symbol);
            }
            notifyItemChanged(position);
        } else {
            this.selectedPosition = position;
            notifyDataSetChanged();
        }
    }

    public Symbol getSelectedItem() {
        return selectedPosition >= 0 ? getItem(selectedPosition) : null;
    }

    public List<String> getSelectedSymbols() {
        return selectedSymbols;
    }
}
