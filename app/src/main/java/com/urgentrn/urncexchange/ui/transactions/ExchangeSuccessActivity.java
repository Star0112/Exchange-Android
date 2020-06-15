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

@EActivity(R.layout.activity_exchange_success)
public class ExchangeSuccessActivity extends BaseActivity {

    @ViewById
    View llBackground;

    @ViewById
    TextView txtTitle, txtDescription;

    @ViewById
    ImageView imgCoinFrom, imgCoinTo;

    @ViewById
    TextView txtAmountFrom, txtAmountTo;

    @ViewById
    KonfettiView konfettiView;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        final String[] args1 = getIntent().getStringArrayExtra("args1");
        final double[] args2 = getIntent().getDoubleArrayExtra("args2");

        final int startColor = Utils.getTransparentColor(args1[0]);
        final int endColor = Utils.getTransparentColor(args1[3]);
        setStatusBarColor(startColor, endColor);
        llBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {startColor, endColor}));
        Glide.with(this)
                .load(args1[1])
                .into(imgCoinFrom);
        Glide.with(this)
                .load(args1[4])
                .into(imgCoinTo);
        txtAmountFrom.setText(String.format("%s %s", Utils.formattedNumber(args2[0]), args1[2]));
        txtAmountTo.setText(String.format("%s %s", Utils.formattedNumber(args2[1]), args1[5]));

        new Handler().postDelayed(() -> {
            konfettiView.build()
                    .addColors(
                            getResources().getColor(R.color.colorWhite),
                            getResources().getColor(R.color.colorPink),
                            getResources().getColor(R.color.colorBlueLight),
                            Color.parseColor(args1[0]),
                            Color.parseColor(args1[3])
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
        final Intent intent = new Intent(this, MainActivity_.class);
        intent.putExtra("type", WalletUtils.TransactionType.EXCHANGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {

    }
}
