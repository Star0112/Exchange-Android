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

public class CoinBalanceHolder extends RecyclerView.ViewHolder {
    private ImageView coinIcon;
    private TextView coinName;
    private TextView txtTotalBalance;
    private TextView txtAvailableBalance;
    private Button btnAddress;


    public CoinBalanceHolder(View itemView) {
        super(itemView);
        coinIcon = itemView.findViewById(R.id.coinIcon);
        coinName = itemView.findViewById(R.id.coinName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtAvailableBalance = itemView.findViewById(R.id.txtAvailableBalance);
        btnAddress = itemView.findViewById(R.id.btnAddress);

    }

    public void updateView(AssetBalance data, int position) {

        Glide.with(this.itemView.getContext())
                .load(data.getImage())
                .into(coinIcon);
        coinName.setText(data.getCoin());
        txtTotalBalance.setText(formattedNumber(Double.parseDouble(data.getAvailable())));
        txtAvailableBalance.setText(formattedNumber(Double.parseDouble(data.getAvailable()) - Double.parseDouble(data.getFreeze())));
    }
}