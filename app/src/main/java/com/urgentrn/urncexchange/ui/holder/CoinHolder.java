package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.utils.Utils;

public class CoinHolder extends RecyclerView.ViewHolder {

    private ImageView imgCoin, imgCheck;
    private TextView txtCoin, txtBalance, txtBalanceCurrency;

    private boolean showCurrency;

    public CoinHolder(View itemView) {
        this(itemView, false);
    }

    public CoinHolder(View itemView, boolean showCurrency) {
        super(itemView);

        imgCoin = itemView.findViewById(R.id.imgCoin);
        txtCoin = itemView.findViewById(R.id.txtCoin);
        txtBalance = itemView.findViewById(R.id.txtBalance);
        txtBalanceCurrency = itemView.findViewById(R.id.txtBalanceCurrency);
        imgCheck = itemView.findViewById(R.id.imgCheck);

        this.showCurrency = showCurrency;
    }

    public void updateView(Wallet data) {
        setIcon(data.getSymbolData().getColoredImage());
        setTitle(data.getTitle());
        if (showCurrency) {
            setBalance(Utils.formattedNumber(data.getBalance(), 0, 5) + " " + data.getSymbol());
            setBalanceCurrency(data.getBalanceCurrencyFormatted());
        } else {
            setBalance(data.getBalanceCurrencyFormatted());
            setBalanceCurrency(null);
        }
    }

    public void setIcon(String icon) {
        Glide.with(itemView)
                .load(icon)
                .into(imgCoin);
    }

    public void setTitle(String title) {
        txtCoin.setText(title);
    }

    public void setBalance(String balance) {
        txtBalance.setText(balance);
    }

    public void setBalanceCurrency(String balance) {
        if (txtBalanceCurrency == null) return;
        txtBalanceCurrency.setText(balance);
        txtBalanceCurrency.setVisibility(balance != null ? View.VISIBLE : View.GONE);
    }

    public void setChecked(boolean checked) {
        if (imgCheck == null) return;
        imgCheck.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
    }
}
