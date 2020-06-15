package com.urgentrn.urncexchange.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.GiftMeta;
import com.urgentrn.urncexchange.utils.Utils;

public class GiftCardHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView txtName, txtPrice;
    private View llProcessing;

    public GiftCardHolder(View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        txtName = itemView.findViewById(R.id.txtName);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        llProcessing = itemView.findViewById(R.id.llProcessing);
    }

    public void updateView(Card data) {
        String priceFormatted = null;
        for (GiftMeta meta : data.getMetas()) {
            switch (meta.getType()) {
                case "giftCardColor":
                    ((CardView)itemView).setCardBackgroundColor(Color.parseColor(meta.getValue()));
                    break;
                case "giftCardImage":
                    Glide.with(itemView).load(meta.getValue()).into(image);
                    break;
                case "giftCardAmount":
                    priceFormatted = String.format("$%s", Utils.formattedNumber(meta.getValue()));
                    break;
                case "giftCardName":
                    txtName.setText(meta.getValue());
                    break;
                default:
                    break;
            }
        }
        if (data.getStatus().equals("processing")) {
            txtPrice.setVisibility(View.INVISIBLE);
            llProcessing.setVisibility(View.VISIBLE);
        } else {
            txtPrice.setText(priceFormatted);
            txtPrice.setVisibility(View.VISIBLE);
            llProcessing.setVisibility(View.INVISIBLE);
        }
    }
}
