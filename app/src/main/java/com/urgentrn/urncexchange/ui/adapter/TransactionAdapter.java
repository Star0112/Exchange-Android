package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Transaction;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.FooterHolder;
import com.urgentrn.urncexchange.ui.holder.HeaderHolder;
import com.urgentrn.urncexchange.ui.holder.TransactionHolder;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends UltimateViewAdapter {

    private Wallet wallet;
    private List<Transaction> data = new ArrayList<>();
    private OnItemClickListener mListener;

    public TransactionAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);

        return new TransactionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            final Transaction transaction = getItem(position);
            ((TransactionHolder)viewHolder).updateView(wallet, transaction);
            if (mListener != null) {
                viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
            }
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
        return Utils.daysFromNow(getItem(position).getDateTime());
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_header_transaction, parent, false);

        return new HeaderHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPES.NORMAL) return;
        final int days = Utils.daysFromNow(getItem(position).getDateTime());
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

    public Transaction getItem(int position) {
        return data.get(Constants.USE_NESTED_LIST_HEADER ? position - 1 : position); // - 1 added because of normal header view
    }

    public void setData(Wallet wallet, List<Transaction> data, int page) {
        if (data.size() == 0) return;
        this.wallet = wallet;
        if (page == 1) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
