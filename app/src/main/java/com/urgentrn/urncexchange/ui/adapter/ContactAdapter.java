package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.contacts.BaseContact;
import com.urgentrn.urncexchange.ui.holder.ContactHolder;
import com.urgentrn.urncexchange.ui.holder.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends UltimateViewAdapter {

    private List<BaseContact> data = new ArrayList<>();
    private OnItemClickListener mListener;
    private boolean sendEnabled;

    public ContactAdapter(OnItemClickListener listener) {
        this(listener, false);
    }

    public ContactAdapter(OnItemClickListener listener, boolean sendEnabled) {
        this.mListener = listener;
        this.sendEnabled = sendEnabled;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public HeaderHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);

        return new ContactHolder(v);
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return data.get(position).getName().toUpperCase().charAt(0);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final BaseContact contact = data.get(position);
        ((ContactHolder)viewHolder).updateView(contact, sendEnabled);

        if (sendEnabled) {
            viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(-position - 1));
            ((ContactHolder)viewHolder).txtSend.setOnClickListener(v -> mListener.onItemClick(position));
        } else {
            viewHolder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
        }
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        if (sendEnabled) return null;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_header, parent, false);

        return new HeaderHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (sendEnabled) return;
        ((TextView)holder.itemView).setText(Character.toString((char)generateHeaderId(position)));
    }

    public BaseContact getItem(int position) {
        return data.get(position);
    }

    public void setData(List<BaseContact> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
