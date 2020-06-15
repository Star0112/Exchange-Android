package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.view.animation.AnimationUtils;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pin_confirm)
public class PINConfirmActivity extends BaseActivity {

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    private String passcode;

    @AfterViews
    protected void init() {
        setToolBar(true);

        passcode = getIntent().getStringExtra("passcode");

        padView.setTextLengthLimit(Constants.PASSCODE_LENGTH);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            if (otpView.getText().length() < Constants.PASSCODE_LENGTH || text.length() < Constants.PASSCODE_LENGTH) {
                otpView.setText(text);
                updatePINView(true);
            }
        });
        otpView.setOtpCompletionListener(otp -> onNext());
    }

    private void onNext() {
        final String confirmPin = otpView.getText().toString();
        if (!confirmPin.equals(passcode)) {
            updatePINView(false);
            return;
        }

        ExchangeApplication.getApp().getPreferences().setPasscode(passcode);
        ExchangeApplication.getApp().getPreferences().setPasscodeEnabled(true);

        final Intent intent = new Intent(this, TouchIDActivity_.class);
        startActivity(intent);
    }

    private void updatePINView(boolean isCorrect) {
        otpView.setLineColor(getResources().getColor(isCorrect ? R.color.colorPrimary : R.color.colorError));
        if (!isCorrect) {
            otpView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PINCreateActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
