package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;

public class HeaderHolder extends RecyclerView.ViewHolder {

    private TextView txtHeader;

    public HeaderHolder(View itemView) {
        super(itemView);

        txtHeader = itemView.findViewById(R.id.txtHeader);
    }
}
