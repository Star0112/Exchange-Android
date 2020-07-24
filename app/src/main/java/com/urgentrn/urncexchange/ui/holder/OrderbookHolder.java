package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.OrderData;

import static com.urgentrn.urncexchange.utils.Utils.formatLargeNumber;
import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;
import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;

public class OrderbookHolder extends RecyclerView.ViewHolder {

    private TextView txtBidAmount, txtBidPrice, txtAskAmount, txtAskPrice;

    public OrderbookHolder(View itemView) {
        super(itemView);
        txtBidAmount = itemView.findViewById(R.id.txtBidAmount);
        txtBidPrice = itemView.findViewById(R.id.txtBidPrice);
        txtAskAmount = itemView.findViewById(R.id.txtAskAmount);
        txtAskPrice = itemView.findViewById(R.id.txtAskPrice);
    }

    public void updateView(OrderData data, int position) {
        txtBidAmount.setText(formattedNumber(data.getBidAmount()));
        txtBidPrice.setText(formattedNumber(data.getBidPrice()));
        txtAskAmount.setText(formattedNumber(data.getAskAmount()));
        txtAskPrice.setText(formattedNumber(data.getAskPrice()));
    }
}