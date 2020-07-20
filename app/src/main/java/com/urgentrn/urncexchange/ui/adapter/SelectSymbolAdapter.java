package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.holder.SelectSymbolHolder;

import java.util.ArrayList;
import java.util.List;

public class SelectSymbolAdapter extends RecyclerView.Adapter<SelectSymbolHolder> {

    private List<String> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public SelectSymbolAdapter(OnItemClickListener listener,List<String> histories) {
        mListener = listener;
        this.data = histories;
    }

    @NonNull
    @Override
    public SelectSymbolHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_select_symbol, parent, false);

        return new SelectSymbolHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSymbolHolder holder, int position) {
        final String symbol = data.get(position);
        holder.updateView(symbol, position);

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
