package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.models.InviteUser;
import com.urgentrn.urncexchange.ui.holder.BuyHistoryHolder;
import com.urgentrn.urncexchange.ui.holder.FriendHistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class FriendHistoryAdapter extends RecyclerView.Adapter<FriendHistoryHolder> {

    private List<InviteUser> data = new ArrayList<>();

    public FriendHistoryAdapter(List<InviteUser> histories) {
        this.data = histories;
    }

    @NonNull
    @Override
    public FriendHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_friend_history, parent, false);
        return new FriendHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHistoryHolder holder, int position) {
        final InviteUser inviteUser = data.get(position);
        holder.UpdateView(inviteUser, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
