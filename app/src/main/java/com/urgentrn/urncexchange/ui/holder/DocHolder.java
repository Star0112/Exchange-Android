package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;

public class DocHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;

    public DocHolder(View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }
}
