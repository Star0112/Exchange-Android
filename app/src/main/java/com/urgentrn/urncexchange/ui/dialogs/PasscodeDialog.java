package com.urgentrn.urncexchange.ui.dialogs;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_passcode)
public class PasscodeDialog extends BaseDialog {

    @ViewById
    View btnClose, btnSignOut;

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    private Constants.SecurityType type;
    private String color;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;

        type = (Constants.SecurityType)getArguments().getSerializable("type");
        color = getArguments().getString("color");

        setCancelable(type != Constants.SecurityType.DEFAULT && type != Constants.SecurityType.SETTING);
        if (type == Constants.SecurityType.DEFAULT) {
            btnClose.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnClose.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }

        otpView.setOtpCompletionListener(otp -> onSubmit());

        padView.setTextLengthLimit(Constants.PASSCODE_LENGTH);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            otpView.setText(text);
            updatePINView(true);
        });
        padView.clearDigits();
    }

    private void updatePINView(boolean isCorrect) {
        otpView.setLineColor(getResources().getColor(isCorrect ? R.color.colorPrimary : R.color.colorError));
        if (!isCorrect) {
            final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    padView.clearDigits();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            otpView.startAnimation(animation);
        }
    }

    @Click(R.id.btnPassword)
    void onPassword() {
        dismissAllowingStateLoss();
        showPasswordDialog(type, color, mListener, "passcode");
    }

    @Click({R.id.btnClose, R.id.btnSignOut})
    void onClose() {
        if (mListener != null) mListener.onDismiss(false);
        dismissAllowingStateLoss();
    }

    private void onSubmit() {
        final String enteredPasscode = otpView.getText().toString();
        if (!enteredPasscode.equals(ExchangeApplication.getApp().getPreferences().getPasscode())) {
            updatePINView(false);
            return;
        }

        if (mListener != null) mListener.onDismiss(true);
        dismissAllowingStateLoss();
    }
}
