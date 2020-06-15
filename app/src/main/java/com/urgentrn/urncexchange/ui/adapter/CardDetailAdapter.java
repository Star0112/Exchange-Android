package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.ui.holder.CardDetailHolder;

import java.util.ArrayList;
import java.util.List;

public class CardDetailAdapter extends RecyclerView.Adapter<CardDetailHolder> {

    private List<CardDetail> data = new ArrayList<>();

    @Override
    @NonNull
    public CardDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_card_detail, parent, false);

        return new CardDetailHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardDetailHolder viewHolder, int position) {
        final CardDetail detail = data.get(position);
        viewHolder.updateView(detail);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CardDetail> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
