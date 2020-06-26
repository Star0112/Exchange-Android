package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;


public class CoinDepositHolder extends RecyclerView.ViewHolder {
    private ImageView coinIcon;
    private TextView coinName;
    private TextView txtTotalBalance;
    private TextView txtFreeBalance;
    private TextView address;
    private Button btnAddress;
    private Button btnDeposit;

    public CoinDepositHolder(View itemView) {
        super(itemView);
        coinIcon = itemView.findViewById(R.id.coinIcon);
        coinName = itemView.findViewById(R.id.coinName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtFreeBalance = itemView.findViewById(R.id.txtFreeBalance);
        address = itemView.findViewById(R.id.address);
        btnAddress = itemView.findViewById(R.id.btnAddress);
        btnDeposit = itemView.findViewById(R.id.btnDeposit);

    }


    public void updateView(AssetBalance data, int position) {

        Glide.with(this.itemView.getContext())
            .load(data.getImage())
            .into(coinIcon);
        coinName.setText(data.getCoin());
        txtTotalBalance.setText(data.getAvailable());
        txtFreeBalance.setText(data.getFreeze());
        if(data.getType() == 0) {
            address.setVisibility(View.VISIBLE);
            btnAddress.setVisibility(View.VISIBLE);
            btnDeposit.setVisibility(View.GONE);
        } else if (data.getType() == 1) {
            address.setVisibility(View.GONE);
            btnAddress.setVisibility(View.GONE);
            btnDeposit.setVisibility(View.VISIBLE);
        }
    }
}
