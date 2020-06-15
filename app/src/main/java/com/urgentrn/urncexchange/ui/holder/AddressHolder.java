package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;

public class AddressHolder extends RecyclerView.ViewHolder {

    public View llDelete, imgQRCode;
    public TextView txtCoin;
    public EditText editAddress, editMemo;

    public AddressHolder(View itemView) {
        super(itemView);

        llDelete = itemView.findViewById(R.id.llDelete);
        imgQRCode = itemView.findViewById(R.id.imgQRCode);
        txtCoin = itemView.findViewById(R.id.txtCoin);
        editAddress = itemView.findViewById(R.id.editAddress);
        editMemo = itemView.findViewById(R.id.editMemo);
    }
}
