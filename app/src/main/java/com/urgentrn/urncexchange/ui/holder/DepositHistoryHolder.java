package com.urgentrn.urncexchange.ui.holder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.DepositHistory;

import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;

public class DepositHistoryHolder extends RecyclerView.ViewHolder {

    private TextView txtAssetsName, txtTotalBalance, txtDate;

    public DepositHistoryHolder(View itemView) {
        super(itemView);
        txtAssetsName = itemView.findViewById(R.id.txtAssetsName);
        txtTotalBalance = itemView.findViewById(R.id.txtTotalBalance);
        txtDate = itemView.findViewById(R.id.txtDate);
    }

    public void updateView(DepositHistory data, int position) {

        txtAssetsName.setText(data.getSymbol());
        txtTotalBalance.setText("+" + data.getAmount());
        txtDate.setText(formattedDateTime(data.getDate()));
    }
}