package com.urgentrn.urncexchange.ui.transactions;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.MainActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

@EActivity(R.layout.activity_buy_sell_success)
public class BuySellSuccessActivity extends BaseActivity {

    @ViewById
    View llBackground;

    @ViewById
    ImageView icon;

    @ViewById
    TextView txtTitle, txtDescription, txtDescription2;

    @ViewById
    TextView txtAmount;

    @ViewById
    KonfettiView konfettiView;

    private WalletUtils.TransactionType type;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        final Wallet wallet = (Wallet)getIntent().getSerializableExtra("wallet");
        type = (WalletUtils.TransactionType)getIntent().getSerializableExtra("type");
        final double amount = getIntent().getDoubleExtra("amount", 0);
        final String amountFormatted;

        final int color;
        if (wallet != null) {
            color = Utils.getTransparentColor(wallet.getColor());
            Glide.with(this).load(wallet.getSymbolData().getColoredImage()).into(icon);
            if (type == WalletUtils.TransactionType.DEPOSIT || type == WalletUtils.TransactionType.WITHDRAW) {
                amountFormatted = String.format("$%s", Utils.formattedNumber(amount));
            } else {
                amountFormatted = String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol());
            }
        } else {
            color = getResources().getColor(R.color.colorYellow);
            amountFormatted = String.format("$%s", Utils.formattedNumber(amount));
        }
        if (amount == 0) {
            txtAmount.setVisibility(View.GONE);
        } else {
            txtAmount.setText(amountFormatted);
        }
        setStatusBarColor(color, Color.WHITE);
        llBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {color, Color.WHITE}));

        switch (type) {
            case BUY:
                txtTitle.setVisibility(View.GONE);
                txtDescription.setText(R.string.buy_success);
                break;
            case GIFT:
                txtDescription.setText(R.string.buy_gift_success);
                txtDescription2.setText(String.format("%s %s", "Gift card from", getIntent().getStringExtra("name")));
                txtDescription2.setVisibility(View.VISIBLE);
                return;
            case SELL:
                txtDescription.setText(R.string.sell_success);
                break;
            case DEPOSIT:
                txtDescription.setText(R.string.deposit_success);
                break;
            case WITHDRAW:
                txtDescription.setText(R.string.withdraw_success);
                break;
            case SEND:
                txtDescription.setText(R.string.send_success);
                break;
            case LOAD:
                txtDescription.setText(R.string.load_success);
                break;
            case NETWORK:
                if (getIntent().getStringExtra("name").equals("activated")) {
                    txtDescription.setText(R.string.network_activate_success);
                } else {
                    txtDescription.setText(R.string.network_deactivate_success);
                    return;
                }
                break;
            default:
                txtDescription.setText(null);
                break;
        }

        new Handler().postDelayed(() -> {
            konfettiView.build()
                    .addColors(
                            getResources().getColor(R.color.colorWhite),
                            getResources().getColor(R.color.colorPink),
                            getResources().getColor(R.color.colorBlueLight),
                            wallet != null ? Color.parseColor(wallet.getColor()) : getResources().getColor(R.color.colorYellow)
                    )
                    .setDirection(0, 359)
                    .setSpeed(1, 5)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000)
                    .addShapes(Shape.RECT)
                    .addSizes(new Size(8, 5), new Size(12, 5))
                    .setPosition(-50, konfettiView.getWidth() + 50f, -50, -50f)
                    .streamFor(30, 5000);
        }, 50);
    }

    public void onNext(View v) {
        Intent intent = new Intent(this, MainActivity_.class);
        if (type == WalletUtils.TransactionType.GIFT) { // sending gift type only, other types useless
            intent.putExtra("type", type);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {

    }
}
