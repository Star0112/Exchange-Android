package com.urgentrn.urncexchange.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.AvailablePrice;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.utils.Utils;

public class GiftCardAvailableHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView txtName, txtPrice;

    public GiftCardAvailableHolder(View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        txtName = itemView.findViewById(R.id.txtName);
        txtPrice = itemView.findViewById(R.id.txtPrice);
    }

    public void updateView(GiftCard data) {
        if (data.getColor() != null) {
            ((CardView)itemView).setCardBackgroundColor(Color.parseColor(data.getColor()));
        }
        Glide.with(itemView).load(data.getImage()).into(image);
        txtName.setText(data.getName());
        String priceFormatted;
        switch (data.getPriceType()) {
            case "fixed":
                StringBuilder builder = new StringBuilder();
                for (AvailablePrice price : data.getAvailablePrices()) {
                    builder.append(price.getPriceFormatted());
                    builder.append(" ");
                }
                priceFormatted = builder.toString().trim();
                break;
            case "variable":
                priceFormatted = String.format("$%s - $%s", Utils.formattedNumber(data.getMinPrice()), Utils.formattedNumber(data.getMaxPrice()));
                break;
            default:
                priceFormatted = null;
                break;
        }
        txtPrice.setText(priceFormatted);
    }
}
