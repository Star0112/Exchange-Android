package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
//import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.PinValidator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

@EFragment(R.layout.dialog_pin)
public class PinDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View btnBack, btnClose;

    @ViewById
    TextView txtTitle, txtEnter;

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    private String pin;
    private boolean isUpdating, isConfirming;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;
        isUpdating = getArguments().getBoolean("is_updating");

        setCancelable(isUpdating);

        otpView.setOtpCompletionListener(otp -> onNext());

        padView.setTextLengthLimit(Constants.PIN_LENGTH);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            otpView.setText(text);
            updatePINView(true);
        });
        padView.clearDigits();

        btnClose.setVisibility(isUpdating ? View.VISIBLE : View.GONE);

        updateView();
    }

    private void updateView() {
        if (!isConfirming) {
            btnBack.setVisibility(View.GONE);
            txtTitle.setText(isUpdating ? R.string.change_pin : R.string.create_pin);
            txtEnter.setText(isUpdating ? R.string.enter_new_pin : R.string.enter_digits);
        } else {
            btnBack.setVisibility(View.VISIBLE);
            txtTitle.setText(isUpdating ? R.string.change_pin : R.string.confirm_pin);
            txtEnter.setText(R.string.confirm_digits);
        }
    }

    private void updatePINView(boolean isCorrect) {
        otpView.setLineColor(getResources().getColor(isCorrect ? R.color.colorPrimary : R.color.colorError));
        if (!isCorrect) {
            otpView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
        }
    }

    @Click(R.id.btnBack)
    void onBack() {
        isConfirming = false;
        updateView();
    }

    private void onNext() {
        final String pin = otpView.getText().toString();
        if (pin.length() != Constants.PIN_LENGTH) {
            showAlert(getString(R.string.error_input_digits));
            return;
        }
        if (PinValidator.isRepeated(pin)) {
            showAlert(getString(R.string.error_invalid_pin));
            return;
        }
        if (!isConfirming) {
            isConfirming = true;
            this.pin = pin;
            padView.clearDigits();
            updateView();
        } else if (!pin.equals(this.pin)) {
            updatePINView(false);
        } else {
            showProgressBar();
            final HashMap<String, String> request = new HashMap<>();
            final String reference = getArguments().getString("reference");
            request.put("reference", reference);
            request.put("pin", pin);
            ApiClient.getInterface().createPin(request).enqueue(new AppCallback<>(getContext(), this));
        }
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        hideProgressBar();
//        final Intent intent = new Intent(getContext(), VerifySuccessActivity_.class);
//        intent.putExtra("type", isUpdating ? Constants.VerifyType.CARD_UPDATE_PIN : Constants.VerifyType.CARD_CREATE_PIN);
//        startActivity(intent);
//        dismiss();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
        btnClose.setVisibility(View.VISIBLE);
    }
}
