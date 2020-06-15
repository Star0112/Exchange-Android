package com.urgentrn.urncexchange.ui.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.DepositAddress;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetDepositAddressResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_deposit_currency)
public class DepositCurrencyDialog extends BaseDialog implements ApiCallback {

    @ViewById
    TextView txtTitle, txtCoin;

    @ViewById
    ImageView imgCoin, imgCopy, imgShare;

    @ViewById
    TextView txtRoutingNumberLabel, txtRoutingNumber, txtAccountNumberLabel, txtAccountNumber, txtBankInformationLabel, txtBankInformation;

    private DepositAddress address;

    @AfterViews
    protected void init() {
        final Wallet wallet = (Wallet)(getArguments().getSerializable("wallet"));
        final int color = Color.parseColor(wallet.getColor());

        txtTitle.setText(getString(R.string.title_dialog_deposit_currency, wallet.getTitle()));
        txtCoin.setText(wallet.getTitle());
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        imgCopy.setImageTintList(ColorStateList.valueOf(color));
        imgShare.setImageTintList(ColorStateList.valueOf(color));

        if (AppData.getInstance().getDepositAddress() == null) {
            ApiClient.getInterface().getDepositAddress(wallet.getSymbol()).enqueue(new AppCallback<>(this));
        } else {
            updateView();
        }
    }

    private void updateView() {
        final DepositAddress address = AppData.getInstance().getDepositAddress();
        if (address == null) return;
        this.address = address;
        txtRoutingNumberLabel.setText(address.getStrings().get("routingNumber"));
        txtRoutingNumber.setText(address.getData().get("routingNumber"));
        txtAccountNumberLabel.setText(address.getStrings().get("accountNumber"));
        txtAccountNumber.setText(address.getData().get("accountNumber"));
        txtBankInformation.setText(address.getStrings().get("bankInformation"));
    }

    private String getShareText() {
        String text = "";
        text += "Routing Number: " + address.getData().get("routingNumber");
        text += "\nAccount Number: " + address.getData().get("accountNumber");
        text += "\nBank Information:\n" + address.getStrings().get("bankInformation");
        return text;
    }

    @Click(R.id.llCopy)
    void onCopy() {
        if (address == null) return;
        final ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(getString(R.string.referral_code), getShareText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), R.string.address_copied, Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.llShare)
    void onShare() {
        if (address == null) return;
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getShareText());
        startActivity(Intent.createChooser(intent, "Share Deposit Address"));
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetDepositAddressResponse) {
            final DepositAddress depositAddress = ((GetDepositAddressResponse)response).getData();
            AppData.getInstance().setDepositAddress(depositAddress);
            updateView();
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
