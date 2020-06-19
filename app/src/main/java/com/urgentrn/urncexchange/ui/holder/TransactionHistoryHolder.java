package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;

public class TransactionHistoryHolder extends RecyclerView.ViewHolder {

    private TextView txtAssetsName, txtTotalBalance, txtDate;

    public TransactionHistoryHolder(View itemView) {
        super(itemView);
        txtAssetsName = itemView.findViewById(R.id.txtAssetsName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void updateView(BuyHistory data, int position) {

        txtAssetsName.setText(data.getAssetName());
        txtTotalBalance.setText(data.getTotalBalance());
        txtDate.setText(data.getDate());
    }
}