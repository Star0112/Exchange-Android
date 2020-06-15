package com.urgentrn.urncexchange.ui.transactions;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_send_input)
public class SendInputActivity extends BaseActivity {

    @ViewById
    TextView txtTitle, txtAvailable, txtPrice, txtAmount;

    @ViewById
    View imgSwitch;

    @ViewById
    Numpad padView;

    private Wallet wallet;
    private Symbol symbol;

    private double price/*USD*/, amount/*Coin*/;
    private boolean isCoin = false;
    private double[] percents = {0.25, 0.5, 1};

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        wallet = (Wallet)getIntent().getSerializableExtra("wallet");
        symbol = WalletUtils.getSymbolData(wallet.getSymbol());

        if (wallet == null) {
            onBackPressed();
            return;
        }

        txtTitle.setText(String.format("%s %s", getString(R.string.title_send), wallet.getTitle()));
        txtAvailable.setText(String.format("%s %s %s", Utils.formattedNumber(wallet.getBalance(), 0, 5), wallet.getSymbol(), getString(R.string.available)));
        final int color = Color.parseColor(wallet.getColor());
        txtPrice.setTextColor(color);

        if (wallet.getSymbolData().isCurrency()) {
            txtAmount.setVisibility(View.GONE);
            imgSwitch.setVisibility(View.GONE);
        }

        padView.setOnTextChangeListener((text, digits_remaining) -> {
            if (isCoin) {
                amount = padView.getValue();
                price = amount * getMarketPrice();
                txtAmount.setText(String.format("%c%s", wallet.getCurrencySymbol(), price));
                txtPrice.setText(String.format("%s %s", text, wallet.getSymbol()));
            } else {
                price = padView.getValue();
                amount = price / getMarketPrice();
                txtPrice.setText(String.format("%c%s", wallet.getCurrencySymbol(), text));
                txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
            }
        });
        padView.setValue(0);
    }

    public void onSwitch(View v) {
        isCoin = !isCoin;
        padView.setValue(isCoin ? amount : price);
    }

    public void onPercent1(View v) {
        onPercentClicked(0);
    }

    public void onPercent2(View v) {
        onPercentClicked(1);
    }

    public void onPercent3(View v) {
        onPercentClicked(2);
    }

    public void onNext(View v) {
        if (amount == 0) {
            showAlert(R.string.error_amount_empty);
        } else if (amount > wallet.getBalance()) {
            showAlert(getString(R.string.error_larger_amount_transaction, WalletUtils.TransactionType.SEND.name().toLowerCase()));
        } else {
            final Intent intent = new Intent(this, SendActivity_.class);
            intent.putExtra("wallet", wallet);
            intent.putExtra("type", getIntent().getSerializableExtra("type"));
            intent.putExtra("amount", amount);
            intent.putExtra("price", price);
            startActivity(intent);
        }
    }

    public void onClose(View v) {
        onBackPressed();
    }

    private double getMarketPrice() {
        return symbol != null && symbol.getMarketData() != null ? symbol.getMarketData().getPrice() : wallet.getSymbolData().getPrice();
    }

    private void onPercentClicked(int index) {
        final double balance = isCoin ? wallet.getBalance() : wallet.getBalanceCurrency();
        padView.setValue(balance * percents[index]);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }
}
