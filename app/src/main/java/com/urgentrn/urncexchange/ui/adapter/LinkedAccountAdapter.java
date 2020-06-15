package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.ui.holder.LinkedAccountHolder;

import java.util.ArrayList;
import java.util.List;

public class LinkedAccountAdapter extends RecyclerView.Adapter<LinkedAccountHolder> {

    private List<Account> data = new ArrayList<>();
    private int selectedPosition = -1;
    private boolean isSelectable;
    private OnItemClickListener mListener;

    public LinkedAccountAdapter(OnItemClickListener listener) {
        this(listener, false);
    }

    public LinkedAccountAdapter(OnItemClickListener listener, boolean isSelectable) {
        this.mListener = listener;
        this.isSelectable = isSelectable;
    }

    @Override
    @NonNull
    public LinkedAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_linked_account, parent, false);

        return new LinkedAccountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkedAccountHolder viewHolder, int position) {
        final Account account = getItem(position);
        viewHolder.updateView(account, isSelectable);
        if (isSelectable) viewHolder.setChecked(position == selectedPosition);

        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) mListener.onItemClick(position);
        });
    }

    public Account getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Account> data) {
        if (data == null) return;
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
}
