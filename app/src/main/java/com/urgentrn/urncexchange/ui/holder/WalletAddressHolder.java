package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.RecentWalletAddress;

public class WalletAddressHolder extends RecyclerView.ViewHolder {

    private TextView txtAddress;

    public WalletAddressHolder(View itemView) {
        super(itemView);

        txtAddress = itemView.findViewById(R.id.txtAddress);
    }

    public void updateView(RecentWalletAddress data) {
        txtAddress.setText(data.getAddress());
    }
}
