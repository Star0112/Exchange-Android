package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;

import static com.urgentrn.urncexchange.utils.Utils.addChar;


public class SelectSymbolHolder extends RecyclerView.ViewHolder {
    private TextView txtSymbol;

    public SelectSymbolHolder(View itemView) {
        super(itemView);
        txtSymbol = itemView.findViewById(R.id.txtSymbol);
    }


    public void updateView(String data, int position) {
        txtSymbol.setText(addChar(data, '/', 4));
    }
}
