package com.urgentrn.urncexchange.ui.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.WalletAddress;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAddressResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.QRGenerator;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_deposit)
public class DepositDialog extends BaseDialog implements ApiCallback {

    @ViewById
    TextView txtTitle, txtCoin, txtAddress, txtQRCode, txtExtra, txtExtraValue;

    @ViewById
    ImageView imgCoin, imgQRCode;

    @ViewById
    View llExtra, progressQRCode;

    private WalletAddress walletAddress;
    private String extraName, extraValue;
    private QRGenerator qrGenerator;

    @AfterViews
    protected void init() {
        final Wallet wallet = (Wallet)(getArguments().getSerializable("wallet"));
        walletAddress = AppData.getInstance().getWalletAddress(wallet.getSymbol());
        final int color = Color.parseColor(wallet.getColor());

        txtTitle.setText(getString(R.string.title_dialog_deposit, wallet.getTitle()));
        txtCoin.setText(wallet.getTitle());
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtAddress.setText(getString(R.string.your_deposit_address, wallet.getSymbol()));

        llExtra.setVisibility(View.GONE);
        final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
        if (symbol != null && symbol.getExtra() != null) {
            extraName = symbol.getExtra().get("fieldName");
            if (extraName != null && !extraName.isEmpty()) {
                llExtra.setVisibility(View.VISIBLE);
                txtExtra.setText(extraName);
                txtExtraValue.setText(null);
            }
        }

        if (walletAddress == null) {
            ApiClient.getInterface().getWalletAddress(wallet.getSymbol()).enqueue(new AppCallback<>(this));
        } else {
            showExtra();
            showQRCode();
        }
    }

    private void showExtra() {
        llExtra.setVisibility(View.GONE);
        if (walletAddress.getExtra() != null) {
            extraValue = walletAddress.getExtra().get("fieldValue");
            if (extraValue != null && !extraValue.isEmpty()) {
                llExtra.setVisibility(View.VISIBLE);
                extraName = walletAddress.getExtra().get("fieldName");
                txtExtra.setText(extraName);
                txtExtraValue.setText(extraValue);
            }
        }
    }

    private void showQRCode() {
        if (TextUtils.isEmpty(walletAddress.getAddress())) return;

        txtQRCode.setText(walletAddress.getAddress());

        if (qrGenerator != null && qrGenerator.getStatus() == AsyncTask.Status.RUNNING) {
            qrGenerator.cancel(true);
        }

        final int width = getResources().getDimensionPixelSize(R.dimen.qr_code) / 2;
        final int height = getResources().getDimensionPixelSize(R.dimen.qr_code) / 2;

        qrGenerator = new QRGenerator(width, height, new QRGenerator.QRGeneratorListener() {
            @Override
            public void qrGenerationStarted() {
                progressQRCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void qrGenerated(Bitmap bitmap) {
                if (progressQRCode != null) progressQRCode.setVisibility(View.GONE);
                if (imgQRCode != null) imgQRCode.setImageBitmap(bitmap);
            }
        });
        qrGenerator.execute(walletAddress.getAddress());
    }

    @Click(R.id.llCopy)
    void onCopy() {
        if (walletAddress == null || TextUtils.isEmpty(walletAddress.getAddress())) return;
        final ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(getString(R.string.referral_code), walletAddress.getAddress());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), R.string.address_copied, Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.llExtraValue)
    void onCopyExtra() {
        if (extraValue == null) return;
        final ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(extraName, extraValue);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), getString(R.string.memo_copied, extraName), Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.llShare)
    void onShare() {
        if (walletAddress == null || TextUtils.isEmpty(walletAddress.getAddress())) return;
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, walletAddress.getShare() != null ? walletAddress.getShare() : walletAddress.getAddress());
        startActivity(Intent.createChooser(intent, "Share Deposit Address"));
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetAddressResponse) {
            final WalletAddress walletAddress = ((GetAddressResponse)response).getData();
            if (walletAddress == null) { // TODO: when does this happen?
                Toast.makeText(getContext(), "Failed to get your deposit address", Toast.LENGTH_SHORT).show();
                return;
            }
            this.walletAddress = walletAddress;
            AppData.getInstance().addWalletAddress(walletAddress);
            showExtra();
            showQRCode();
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        progressQRCode.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
