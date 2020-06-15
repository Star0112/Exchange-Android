package com.urgentrn.urncexchange.ui.holder;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.utils.CardUtils;

public class LinkedAccountHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtAccount, txtStatus;
    private ImageView imgCard, imgStatus, imgArrow;

    public LinkedAccountHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);
        txtAccount = itemView.findViewById(R.id.txtAccount);
        imgCard = itemView.findViewById(R.id.imgCard);
        imgStatus = itemView.findViewById(R.id.imgStatus);
        txtStatus = itemView.findViewById(R.id.txtStatus);
        imgArrow = itemView.findViewById(R.id.imgArrow);
    }

    public void updateView(Account data, boolean selectable) {
        final boolean isCreditCard = data.getFlow().equals("stripes");

        if (isCreditCard) {
            imgCard.setImageResource(CardUtils.getBrandImage(data.getField().getBrand()));
            txtName.setText(CardUtils.getBrandName(data.getField().getBrand()));
        } else {
            imgCard.setImageResource(R.mipmap.ic_new_bank);
            txtName.setText(data.getField().getBankName());
        }

        if (!isCreditCard && selectable) {
            txtAccount.setText(data.getField().getAccountNumber());
            txtAccount.setVisibility(View.VISIBLE);
        } else {
            txtAccount.setVisibility(View.GONE);
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
            default: // "verified"
                colorRes = /*selectable ? R.color.colorWhite : */R.color.textColorGreen;
                if (isCreditCard) {
                    status = String.format("•••• %s (%s)", data.getField().getLast4(), data.getAsset().getSymbol());
                } else {
                    status = data.getAsset().getSymbol();
                }
                break;
        }
        if (isCreditCard && data.getStatus().equalsIgnoreCase("verified")) {
            imgStatus.setVisibility(View.GONE);
        } else {
            imgStatus.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(colorRes)));
            imgStatus.setVisibility(View.VISIBLE);
        }
        txtStatus.setText(status);

        imgArrow.setImageResource(selectable ? R.mipmap.ic_check : R.mipmap.ic_forward);
    }

    public void setChecked(boolean checked) {
        imgArrow.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
    }
}
