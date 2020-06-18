package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.DepositCoin;


public class CoinDepositHolder extends RecyclerView.ViewHolder {
    private ImageView coinIcon;
    private TextView coinName;
    private TextView txtTotalBalance;
    private TextView txtFreeBalance;
    private Button btnAddress;

    private int [] coins = new int[5];

    public CoinDepositHolder(View itemView) {
        super(itemView);
        coinIcon = itemView.findViewById(R.id.coinIcon);
        coinName = itemView.findViewById(R.id.coinName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtFreeBalance = itemView.findViewById(R.id.txtFreeBalance);
        btnAddress = itemView.findViewById(R.id.btnAddress);

        coins[0] = R.mipmap.coin_urnc;
        coins[1] = R.mipmap.coin_btc;
        coins[2] = R.mipmap.coin_eth;
        coins[3] = R.mipmap.coin_usd;
    }

    public void updateView(DepositCoin data, int position) {

        Glide.with(this.itemView.getContext())
            .load(coins[position])
            .into(coinIcon);
        coinName.setText(data.getCoinName());
        txtTotalBalance.setText(data.getTotal());
        txtFreeBalance.setText(data.getFree());
    }
}
