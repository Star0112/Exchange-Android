package com.urgentrn.urncexchange.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.utils.Utils;

public class CardTransactionHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtTime, txtPrice, txtStatus;

    public CardTransactionHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);
        txtTime = itemView.findViewById(R.id.txtTime);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        txtStatus = itemView.findViewById(R.id.txtStatus);
    }

    public void updateView(CardTransaction transaction, boolean isSelected) {
        txtName.setText(transaction.getMerchant());
        txtTime.setText(Utils.dateToString(Utils.stringToDate(transaction.getCreatedAt(), null), "hh:mm a"));
        txtPrice.setText(String.format("$%s", Utils.formattedNumber(transaction.getAmount())));
        final int colorRes;
        switch (transaction.getStatus()) {
            case "completed":
                colorRes = R.color.colorComplete;
                break;
            case "pending":
                colorRes = R.color.colorPending;
                break;
            default:
                colorRes = R.color.colorRed;
                break;
        }
        txtStatus.setText(!TextUtils.isEmpty(transaction.getStatus()) ? transaction.getStatus().substring(0, 1).toUpperCase() + transaction.getStatus().substring(1) : null);
        txtStatus.setTextColor(itemView.getContext().getResources().getColor(colorRes));
        itemView.setBackgroundColor(itemView.getResources().getColor(isSelected ? R.color.colorGrayLight : R.color.colorTransparent));
    }
}
