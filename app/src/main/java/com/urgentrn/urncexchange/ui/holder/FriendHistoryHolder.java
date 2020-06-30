package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.InviteUser;

public class FriendHistoryHolder extends RecyclerView.ViewHolder {
    TextView txtEmail;

    public FriendHistoryHolder(@NonNull View itemView) {
        super(itemView);
        txtEmail = itemView.findViewById(R.id.txtEmail);
    }

    public void UpdateView(InviteUser data, int position) {
//        txtAsset.setText(data.getAsset());
    }
}
