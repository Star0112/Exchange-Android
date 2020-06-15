package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.ui.holder.CardTransactionHolder;
import com.urgentrn.urncexchange.ui.holder.FooterHolder;
import com.urgentrn.urncexchange.ui.holder.HeaderHolder;
import com.urgentrn.urncexchange.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardTransactionAdapter extends UltimateViewAdapter {

    private List<CardTransaction> data = new ArrayList<>();
    private boolean isSelectable;
    private OnItemClickListener mListener;

    public CardTransactionAdapter(OnItemClickListener listener) {
        this(listener, false);
    }

    public CardTransactionAdapter(OnItemClickListener listener, boolean isSelectable) {
        this.mListener = listener;
        this.isSelectable = isSelectable;
    }

    @Override
    public CardTransactionHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_card_transaction, parent, false);

        return new CardTransactionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            final CardTransaction transaction = getItem(position);
            ((CardTransactionHolder)viewHolder).updateView(transaction, isSelectable && transaction.isSelected());
            viewHolder.itemView.setOnClickListener(v -> {
                if (isSelectable) {
                    final boolean selected = !getItem(position).isSelected();
                    getItem(position).setSelected(selected);
                    v.setBackgroundColor(v.getResources().getColor(selected ? R.color.colorGrayLight : R.color.colorTransparent));
                }
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public HeaderHolder newHeaderHolder(View view) {
        return new HeaderHolder(view);
    }

    @Override
    public FooterHolder newFooterHolder(View view) {
        return new FooterHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItemViewType(position) != VIEW_TYPES.NORMAL) return 0;
        return Utils.daysFromNow(data.get(position).getCreatedAt());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_header_transaction, parent, false);

        return new HeaderHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPES.NORMAL) return;
        final int days = (int)generateHeaderId(position);
        String text;
        if (days == 0) {
            text = "Today";
        } else if (days == 1) {
            text = "Yesterday";
        } else if (days > 0) {
            text = String.format(Locale.US, "%d %s", days, "days ago");
        } else {
            text = String.format(Locale.US, "%s %d %s", "In", -days, "days");
        }
        ((TextView)holder.itemView).setText(text);
    }

    public CardTransaction getItem(int position) {
        return data.get(position);
    }

    public void setData(List<CardTransaction> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
