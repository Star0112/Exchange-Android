package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Symbol;

public class CurrencyHolder extends RecyclerView.ViewHolder {

    private ImageView imgCoin, imgCheck;
    private TextView txtTitle, txtSymbol;

    public CurrencyHolder(View itemView) {
        super(itemView);

        imgCoin = itemView.findViewById(R.id.imgCoin);
        txtTitle = itemView.findViewById(R.id.txtCoin);
        txtSymbol = itemView.findViewById(R.id.txtBalance);
        imgCheck = itemView.findViewById(R.id.imgCheck);
    }

    public void updateView(Symbol data) {
        setIcon(data.getColoredImage());
        setTitle(data.getTitle());
        setSymbol(data.getSymbol());
    }

    public void setIcon(String icon) {
        Glide.with(itemView)
                .load(icon)
                .into(imgCoin);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setSymbol(String balance) {
        txtSymbol.setText(balance);
    }

    public void setChecked(boolean checked) {
        if (imgCheck == null) return;
        imgCheck.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
    }
}
