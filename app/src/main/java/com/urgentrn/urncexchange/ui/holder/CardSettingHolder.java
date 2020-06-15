package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;

public class CardSettingHolder extends RecyclerView.ViewHolder {

    private ImageView imgItem;
    private TextView txtItem;

    public CardSettingHolder(View itemView) {
        super(itemView);

        imgItem = itemView.findViewById(R.id.imgItem);
        txtItem = itemView.findViewById(R.id.txtItem);
    }

    public void updateView(int imgRes, int titleRes) {
        imgItem.setImageResource(imgRes);
        txtItem.setText(titleRes);
    }
}
