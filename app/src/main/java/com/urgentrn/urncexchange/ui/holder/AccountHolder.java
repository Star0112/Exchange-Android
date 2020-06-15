package com.urgentrn.urncexchange.ui.holder;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.utils.CardUtils;

public class AccountHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtAccount, txtPrice, txtStatus;
    private ImageView imgCard, imgStatus;
    public View llRemove;

    public AccountHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);
        imgCard = itemView.findViewById(R.id.imgCard);
        imgStatus = itemView.findViewById(R.id.imgStatus);
        txtStatus = itemView.findViewById(R.id.txtStatus);

        txtAccount = itemView.findViewById(R.id.txtAccount);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        llRemove = itemView.findViewById(R.id.llRemove);
    }

    public void updateView(Account data) {
        final boolean isCreditCard = data.getFlow().equals("stripes");

        if (isCreditCard) {
            imgCard.setImageResource(CardUtils.getBrandImage(data.getField().getBrand()));
            txtName.setText(CardUtils.getBrandName(data.getField().getBrand()));
        } else {
            imgCard.setImageResource(R.mipmap.ic_new_bank);
            txtName.setText(data.getField().getBankName());
        }

        int colorRes;
        String status;
        switch (data.getStatus().toLowerCase()) {
            case "pending":
                colorRes = R.color.colorPendingLight;
                status = itemView.getResources().getString(R.string.pending);
                break;
            case "failed":
                colorRes = R.color.colorRed;
                status = itemView.getResources().getString(R.string.failed);
                if (isCreditCard) txtName.setText(R.string.card_failed);
                break;
            default:
                colorRes = R.color.textColorGreen;
                status = itemView.getResources().getString(R.string.verified);
                break;
        }
        imgStatus.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(colorRes)));
        txtStatus.setText(status);

        if (isCreditCard) {
            txtAccount.setText(String.format("•••• %s", data.getField().getLast4()));
        } else {
            txtAccount.setText(data.getField().getAccountNumber());
        }
        String priceFormatted;
        switch (data.getFlow()) {
            case "default":
                priceFormatted = "$0.00";
                break;
            case "plaid":
            case "stripes":
                priceFormatted = data.getAsset().getSymbol();
                break;
            default:
                priceFormatted = null;
                break;
        }
        txtPrice.setText(String.format(" (%s)", priceFormatted));
    }
}
