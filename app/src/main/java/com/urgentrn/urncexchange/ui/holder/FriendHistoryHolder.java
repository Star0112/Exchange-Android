package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.InviteUser;

import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;

public class FriendHistoryHolder extends RecyclerView.ViewHolder {
    TextView txtEmail, confirmStatus;

    public FriendHistoryHolder(@NonNull View itemView) {
        super(itemView);
        txtEmail = itemView.findViewById(R.id.txtEmail);
        confirmStatus = itemView.findViewById(R.id.confirmStatus);
    }

    public void UpdateView(InviteUser data, int position) {
        txtEmail.setText(data.getRefereeEmail());
        confirmStatus.setText(formattedDateTime(data.getDate()));
    }
}
