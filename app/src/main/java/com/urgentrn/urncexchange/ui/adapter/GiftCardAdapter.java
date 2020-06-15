package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.ui.holder.GiftCardHolder;

import java.util.ArrayList;
import java.util.List;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardHolder> {

    private List<Card> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public GiftCardAdapter(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public GiftCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_gift_available, parent, false);

        return new GiftCardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftCardHolder viewHolder, int position) {
        final Card detail = data.get(position);
        viewHolder.updateView(detail);
        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    public Card getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Card> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
