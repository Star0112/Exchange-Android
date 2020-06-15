package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

@EActivity(R.layout.activity_card_success)
public class CardSuccessActivity extends BaseActivity {

    @ViewById
    TextView txtTitle, txtDescription;

    @ViewById
    View llBackground, imgBalloon, imgLine, llCloud, llText, btnContinue;

    @ViewById
    KonfettiView konfettiView;

    private Constants.VerifyType type;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        final int color1 = Color.parseColor("#FFC4B9"), color2 = Color.parseColor("#FFFDFC");
        setStatusBarColor(color1, color2);
        llBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {color1, color2}));

        type = (Constants.VerifyType) getIntent().getSerializableExtra("type");
        if (type == null) return;
        switch (type) {
            case CARD_ORDER:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_order_success);
                break;
            case CARD_UPGRADE:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_upgrade_success);
                break;
            case CARD_ORDER_PHYSICAL:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_order_physical_success);
                break;
            case CARD_ACTIVATE:
                txtTitle.setText(R.string.activated);
                txtDescription.setText(R.string.card_activate_success);
                break;
            default:
                txtTitle.setText(R.string.thank_you);
                txtDescription.setText(null);
                break;
        }

        final float density = getResources().getDisplayMetrics().density;

        final Animation animRotate = new RotateAnimation(0, 10, 50, 50);
        animRotate.setDuration(500);
        animRotate.setRepeatCount(1);
        animRotate.setRepeatMode(Animation.REVERSE);
        imgBalloon.startAnimation(animRotate);
        imgBalloon.animate()
                .setDuration(1000)
                .translationX(0)
                .translationY(0)
                .alpha(1)
                .start();
        imgLine.animate()
                .setDuration(800)
                .translationY(0)
                .alpha(1)
                .start();
        llCloud.animate()
                .setDuration(600)
                .translationX(0)
                .start();
        llText.animate()
                .setDuration(500)
                .translationY(0)
                .alpha(1)
                .start();
        btnContinue.animate()
                .setDuration(500)
                .translationY(0)
                .alpha(1)
                .start();

        new Handler().postDelayed(() -> {
            konfettiView.build()
                    .addColors(
                            getResources().getColor(R.color.colorGreen),
                            getResources().getColor(R.color.colorPink),
                            getResources().getColor(R.color.colorBlueLight),
                            getResources().getColor(R.color.colorYellow)
                    )
                    .setDirection(0, 359)
                    .setSpeed(5, 20)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(800)
                    .addShapes(Shape.RECT)
                    .addSizes(new Size(8, 10), new Size(12, 10))
                    .setPosition(-50, konfettiView.getWidth() + 50f, -50, -50f)
                    .streamFor(1000, 50);
        }, 50);
    }

    public void onNext(View v) {
        final Intent intent = new Intent(this, MainActivity_.class);
        intent.putExtra("verify_type", type);
        if (type == Constants.VerifyType.CARD_ACTIVATE) {
            intent.putExtra("reference", getIntent().getStringExtra("reference"));
            intent.putExtra("pin_type", getIntent().getStringExtra("pin_type"));
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
