package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.bank.FieldOption;

public class BankAccountHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtValue;

    public BankAccountHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName);
        txtValue = itemView.findViewById(R.id.txtValue);
    }

    public void updateView(FieldOption option) {
        txtName.setText(option.getName());
        txtValue.setText(option.getValue());
    }
}
