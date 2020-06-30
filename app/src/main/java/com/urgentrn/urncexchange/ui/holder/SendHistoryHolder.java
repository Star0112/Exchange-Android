package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.models.SendHistory;

import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;
import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;

public class SendHistoryHolder extends RecyclerView.ViewHolder {
    TextView txtAsset, txtAmount, sendEmail, txtDate;

    public SendHistoryHolder(@NonNull View itemView) {
        super(itemView);
        txtAsset = itemView.findViewById(R.id.txtAsset);
        txtAmount = itemView.findViewById(R.id.txtAmount);
        sendEmail = itemView.findViewById(R.id.sendEmail);
        txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void UpdateView(SendHistory data, int position) {
        txtAsset.setText(data.getAsset());
        txtAmount.setText(formattedNumber(Double.parseDouble(data.getAmount())));
        sendEmail.setText(data.getReceiver());
        txtDate.setText(formattedDateTime(data.getDate()));
    }
}
