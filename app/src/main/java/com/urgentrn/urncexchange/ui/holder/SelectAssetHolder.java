package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AssetBalance;

import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;


public class SelectAssetHolder extends RecyclerView.ViewHolder {
    private TextView txtAsset, txtAssetBalance;
    private ImageView imgAsset;

    public SelectAssetHolder(View itemView) {
        super(itemView);
        imgAsset = itemView.findViewById(R.id.imgAsset);
        txtAsset = itemView.findViewById(R.id.txtAsset);
        txtAssetBalance = itemView.findViewById(R.id.txtAssetBalance);
    }


    public void updateView(AssetBalance data, int position) {
        Glide.with(this.itemView.getContext())
                .load(data.getImage())
                .into(imgAsset);
        txtAsset.setText(data.getCoin());
        txtAssetBalance.setText(formattedNumber(Double.parseDouble(data.getAvailable()) - Double.parseDouble(data.getFreeze())));
    }
}
