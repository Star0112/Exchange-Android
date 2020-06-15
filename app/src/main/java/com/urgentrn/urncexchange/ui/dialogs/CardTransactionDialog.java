package com.urgentrn.urncexchange.ui.dialogs;

import android.text.TextUtils;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

@EFragment(R.layout.dialog_card_transaction)
public class CardTransactionDialog extends BaseDialog {

    @ViewById
    TextView txtPrice, txtDate, txtName, txtStatus, txtTransactionId;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;
        final CardTransaction transaction = (CardTransaction)getArguments().getSerializable("transaction");
        if (transaction == null) return;

        txtPrice.setText(String.format("$%s %s", Utils.formattedNumber(transaction.getAmount()), transaction.getCurrency()));
        txtDate.setText(Utils.formattedDateTime(transaction.getCreatedAt()));
        txtName.setText(transaction.getMerchant());

        final int colorRes;
        switch (transaction.getStatus()) {
            case "completed":
                colorRes = R.color.colorComplete;
                break;
            case "pending":
                colorRes = R.color.colorPending;
                break;
            default:
                colorRes = R.color.colorRed;
                break;
        }
        txtStatus.setText(!TextUtils.isEmpty(transaction.getStatus()) ? transaction.getStatus().substring(0, 1).toUpperCase() + transaction.getStatus().substring(1) : null);
        txtStatus.setTextColor(getResources().getColor(colorRes));

        txtTransactionId.setText(String.format(Locale.US, "%s: %d", getString(R.string.transaction_id), transaction.getTransactionId()));
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }
}
