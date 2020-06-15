package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.ui.holder.FooterHolder;
import com.urgentrn.urncexchange.ui.holder.GiftCardAvailableHolder;
import com.urgentrn.urncexchange.ui.holder.HeaderHolder;
import com.urgentrn.urncexchange.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GiftCardAvailableAdapter extends UltimateViewAdapter {

    private List<GiftCard> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public GiftCardAvailableAdapter(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public FooterHolder newFooterHolder(View view) {
        return new FooterHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new HeaderHolder(view);
    }

    @Override
    @NonNull
    public GiftCardAvailableHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_gift_available, parent, false);

        return new GiftCardAvailableHolder(v);
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            final GiftCard detail = getItem(position);
            ((GiftCardAvailableHolder)viewHolder).updateView(detail);
            viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public GiftCard getItem(int position) {
        return data.get(Constants.USE_NESTED_LIST_HEADER ? position - 1 : position);
    }

    public void setData(List<GiftCard> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
