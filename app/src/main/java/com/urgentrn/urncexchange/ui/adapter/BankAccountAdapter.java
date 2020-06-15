package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.bank.FieldOption;
import com.urgentrn.urncexchange.ui.holder.BankAccountHolder;

import java.util.ArrayList;
import java.util.List;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountHolder> {

    private List<FieldOption> data = new ArrayList<>();

    @Override
    @NonNull
    public BankAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_bank_account, parent, false);

        return new BankAccountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAccountHolder viewHolder, int position) {
        viewHolder.updateView(data.get(position));
    }

    public FieldOption getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<FieldOption> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
