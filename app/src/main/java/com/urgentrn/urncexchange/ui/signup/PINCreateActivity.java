package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pin_create)
public class PINCreateActivity extends BaseActivity {

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    @AfterViews
    protected void init() {
        setToolBar(true);

        padView.setTextLengthLimit(Constants.PASSCODE_LENGTH);
        padView.setOnTextChangeListener((text, digits_remaining) -> otpView.setText(text));
        otpView.setOtpCompletionListener(otp -> onNext());
    }

    private void onNext() {
        final String passcode = otpView.getText().toString();
        if (passcode.length() != Constants.PASSCODE_LENGTH) {
            return;
        }

        final Intent intent = new Intent(this, PINConfirmActivity_.class);
        intent.putExtra("passcode", passcode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
