package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;

import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;

public class BuyHistoryHolder extends RecyclerView.ViewHolder {
    TextView txtAsset, txtAmount, txtBaseAsset, txtPrice, txtDate;

    public BuyHistoryHolder(@NonNull View itemView) {
        super(itemView);
        txtAsset = itemView.findViewById(R.id.txtAsset);
        txtAmount = itemView.findViewById(R.id.txtAmount);
        txtBaseAsset = itemView.findViewById(R.id.txtBaseAsset);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void UpdateView(BuyHistory data, int position) {
        txtAsset.setText(data.getAsset());
        txtAmount.setText(data.getAmount());
        txtBaseAsset.setText(data.getBaseAsset());
        txtPrice.setText(data.getPrice());
        txtDate.setText(formattedDateTime(data.getDate()));
    }
}
