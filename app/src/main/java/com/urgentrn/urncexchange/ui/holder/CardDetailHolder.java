package com.urgentrn.urncexchange.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardDetail;

public class CardDetailHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle, txtDetail;
    private ImageView imgLock;

    public CardDetailHolder(View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtDetail = itemView.findViewById(R.id.txtDetail);
        imgLock = itemView.findViewById(R.id.imgLock);
    }

    public void updateView(CardDetail data) {
        if (TextUtils.isEmpty(data.getTitle())) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setText(data.getTitle());
            txtTitle.setVisibility(View.VISIBLE);
        }
        txtDetail.setText(data.getDetail());
        imgLock.setVisibility(data.getImage() == null || data.getImage().isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }
}
