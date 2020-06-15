package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.CardSetting;
import com.urgentrn.urncexchange.ui.holder.CardSettingHolder;

import java.util.ArrayList;
import java.util.List;

public class CardSettingAdapter extends RecyclerView.Adapter<CardSettingHolder> {

    private OnItemClickListener mListener;
    private List<CardSetting> data = new ArrayList<>();

    public CardSettingAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    @NonNull
    public CardSettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_card_setting, parent, false);

        return new CardSettingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSettingHolder viewHolder, int position) {
        final CardSetting item = data.get(position);
        viewHolder.updateView(item.getImgRes(), item.getTitleRes());

        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    public CardSetting getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CardSetting> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
