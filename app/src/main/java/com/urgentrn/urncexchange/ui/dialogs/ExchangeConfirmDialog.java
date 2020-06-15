package com.urgentrn.urncexchange.ui.dialogs;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ExchangeConfirmResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_exchange_confirm)
public class ExchangeConfirmDialog extends BaseDialog implements ApiCallback {

    @ViewById
    ImageView imgCoinFrom, imgCoinTo;

    @ViewById
    TextView txtAmountFrom, txtAmountTo, txtAmount, txtExchangingTo, txtExchangingFrom, txtFee;

    @AfterViews
    protected void init() {
        final String[] args1 = getArguments().getStringArray("args1");
        final double[] args2 = getArguments().getDoubleArray("args2");
        final int quoteId = getArguments().getInt("quote_id");

        final String color = args1[0];
        Glide.with(getContext())
                .load(args1[1])
                .into(imgCoinFrom);
        Glide.with(getContext())
                .load(args1[4])
                .into(imgCoinTo);

        final double fromQuantity = args2[0];
        final double toQuantity = args2[1];
        final String exchangingFrom = String.format("%s %s", Utils.formattedNumber(fromQuantity), args1[2]);
        final String exchangingTo = String.format("%s %s", Utils.formattedNumber(toQuantity), args1[5]);

        txtAmountFrom.setText(exchangingFrom);
        txtExchangingFrom.setText(exchangingFrom);
        txtAmountTo.setText(exchangingTo);
        txtExchangingTo.setText(exchangingTo);
        txtFee.setText(args1[6]);

        double conversionRate = (double) Math.round((toQuantity / fromQuantity) * 100) / 100;

        if (conversionRate == 1.0) {
            txtAmount.setText(String.format("1 %s = %s %s", args1[2], (int)conversionRate, args1[5]));
        } else {
            txtAmount.setText(String.format("1 %s = %s %s", args1[2], Utils.formattedNumber(toQuantity / fromQuantity), args1[5]));
        }

        initPassView(Constants.SecurityType.TRANSACTION, null, isSuccess -> {
            if (isSuccess) onConfirm(quoteId);
        });
    }

    private void onConfirm(int quoteId) {
        showProgressBar();
        ApiClient.getInterface().exchangeConfirm(quoteId).enqueue(new AppCallback<>(getContext(), this));
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        hideProgressBar();
        if (response instanceof ExchangeConfirmResponse) {
            mListener.onDismiss(true);
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
