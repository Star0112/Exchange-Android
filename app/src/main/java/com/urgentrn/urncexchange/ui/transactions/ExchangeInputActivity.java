package com.urgentrn.urncexchange.ui.transactions;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxn769.Numpad;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_exchange_input)
public class ExchangeInputActivity extends BaseActivity {

    @ViewById
    ImageView imgCoin;

    @ViewById
    TextView txtAmount, txtCoin;

    @ViewById
    Numpad padView;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        final Wallet wallet = (Wallet)getIntent().getSerializableExtra("wallet");

        if (wallet == null) {
            onBackPressed();
            return;
        }

        Glide.with(this)
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtCoin.setText(wallet.getSymbol());

        padView.setOnTextChangeListener((text, digits_remaining) -> {
            txtAmount.setText(text);
        });
        padView.setValue(getIntent().getDoubleExtra("amount", 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNext(View v) {
        final double amount = padView.getValue();
        if (amount > 0) {
            setResult(RESULT_OK, new Intent().putExtra("amount", amount));
            onBackPressed();
        } else {
            showAlert(R.string.error_amount_empty);
        }
    }

    public void onClose(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }
}
