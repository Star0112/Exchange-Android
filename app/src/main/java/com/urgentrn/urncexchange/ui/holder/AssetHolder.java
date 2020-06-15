package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;

public class AssetHolder extends RecyclerView.ViewHolder {

    private ImageView imgCoin;
    private TextView txtCoin;

    public AssetHolder(View itemView) {
        super(itemView);

        imgCoin = itemView.findViewById(R.id.imgCoin);
        txtCoin = itemView.findViewById(R.id.txtCoin);
    }

    public void updateView(Wallet data) {
        Glide.with(itemView)
                .load(data.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtCoin.setText(data.getSymbol());
    }
}
