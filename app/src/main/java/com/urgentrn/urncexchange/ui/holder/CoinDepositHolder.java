package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;

import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;


public class CoinDepositHolder extends RecyclerView.ViewHolder {
    private ImageView coinIcon;
    private TextView coinName;
    private TextView txtTotalBalance;
    private TextView txtAvailableBalance;
    private TextView address;
    private Button btnAddress;
    private Button btnDeposit;

    public CoinDepositHolder(View itemView) {
        super(itemView);
        coinIcon = itemView.findViewById(R.id.coinIcon);
        coinName = itemView.findViewById(R.id.coinName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtAvailableBalance = itemView.findViewById(R.id.txtAvailableBalance);
        address = itemView.findViewById(R.id.address);
        btnAddress = itemView.findViewById(R.id.btnAddress);
        btnDeposit = itemView.findViewById(R.id.btnDeposit);

    }


    public void updateView(AssetBalance data, int position) {

        Glide.with(this.itemView.getContext())
            .load(data.getImage())
            .into(coinIcon);
        coinName.setText(data.getCoin());
        txtTotalBalance.setText(formattedNumber(Double.parseDouble(data.getAvailable())));
        txtAvailableBalance.setText(formattedNumber(Double.parseDouble(data.getAvailable()) - Double.parseDouble(data.getFreeze())));
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
