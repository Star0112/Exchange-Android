package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.request.DisbursementRequest;
import com.urgentrn.urncexchange.models.request.WithdrawRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.ui.transactions.BuySellSuccessActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_send_confirm)
public class SendConfirmDialog extends BaseDialog implements ApiCallback {

    @ViewById
    ImageView imgCoin;

    @ViewById
    TextView txtAmount, txtPrice, txtDestination, txtExtraTitle, txtExtraContent, txtFeeTitle, txtFee, txtTotal;

    @ViewById
    View llExtra;

    private Wallet wallet;
    private String destination, extraName, extraValue;
    private double amount, price;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;
        wallet = (Wallet)getArguments().getSerializable("wallet");
        final Constants.SendType type = (Constants.SendType)getArguments().getSerializable("type");

        if (wallet == null) return;
        destination = getArguments().getString("address");
        amount = getArguments().getDouble("amount");
        price = getArguments().getDouble("price");
        extraName = getArguments().getString("extra_name");
        extraValue = getArguments().getString("extra_value");

        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);

        txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        txtPrice.setText(String.format("%s%s", wallet.getCurrencySymbol(), Utils.formattedNumber(price)));
        txtDestination.setText(destination);

        if (extraName != null) {
            llExtra.setVisibility(View.VISIBLE);
            txtExtraTitle.setText(extraName);
            txtExtraContent.setText(extraValue);
        } else {
            llExtra.setVisibility(View.GONE);
        }

        txtFeeTitle.setText(String.format("%s %s", wallet.getTitle(), getString(R.string.network_fee)));

        final double fee;
        if (type == Constants.SendType.ADDRESS) {
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            if (symbol != null) {
                fee = symbol.getFee();
            } else {
                fee = wallet.getSymbolData().getFee();
            }
        } else {
            fee = 0;
        }
        txtFee.setText(fee > 0 ? String.format("%s %s", Utils.formattedNumber(fee), wallet.getSymbol()) : getString(R.string.free));
        txtTotal.setText(String.format("%s %s", Utils.formattedNumber(amount - fee), wallet.getSymbol()));
        txtTotal.setTextColor(Color.parseColor(wallet.getColor()));

        initPassView(Constants.SecurityType.TRANSACTION, null, isSuccess -> {
            if (isSuccess) onSend();
        });
    }

    private void onSend() {
        showProgressBar();
        if (wallet.getSymbolData().isCurrency()) {
            final DisbursementRequest request = new DisbursementRequest();
            request.setAssetId(wallet.getSymbolData().getId());
            request.setAmount(amount);
            request.setReceiver(destination);
            ApiClient.getInterface().disbursementWithdraw(request).enqueue(new AppCallback<>(getContext(), this));
        } else {
            final WithdrawRequest request = new WithdrawRequest();
            request.setAmount(amount);
            String address = destination;
            if (!TextUtils.isEmpty(extraValue)) {
                address = address + ":" + extraValue;
            }
            request.setAddress(address);
            ApiClient.getInterface().withdraw(wallet.getSymbol(), request).enqueue(new AppCallback<>(getContext(), this));
        }
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;

        Intent intent = new Intent(getContext(), BuySellSuccessActivity_.class);
        intent.putExtra("wallet", wallet);
        intent.putExtra("type", WalletUtils.TransactionType.SEND);
        intent.putExtra("amount", amount);
        startActivity(intent);
        dismissAllowingStateLoss();

        hideProgressBar();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
