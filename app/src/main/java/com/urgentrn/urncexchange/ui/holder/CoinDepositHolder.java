package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.DepositCoin;


public class CoinDepositHolder extends RecyclerView.ViewHolder {
    private TextView coinName;
    public CoinDepositHolder(View itemView) {
        super(itemView);
        coinName = itemView.findViewById(R.id.coin_name);
    }
    public void updateView(DepositCoin data) {
        coinName.setText(data.getCoinName());
    }
}
