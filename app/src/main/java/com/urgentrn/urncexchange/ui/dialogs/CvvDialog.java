package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.ActivateCardRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.PinResponse;
//import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

@EFragment(R.layout.dialog_cvv)
public class CvvDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View llCvv, llTerms, btnActivate;

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    @ViewById
    WebView webView;

    @ViewById
    TextView txtTerms;

    private String reference, pinType, cvv;
    private static int CVV_LENGTH = 3;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;

        otpView.setItemCount(CVV_LENGTH);
        otpView.setOtpCompletionListener(otp -> onNext());

        padView.setTextLengthLimit(CVV_LENGTH);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            otpView.setText(text);
        });
        padView.clearDigits();

        llTerms.setVisibility(View.GONE);
        txtTerms.setText(R.string.cardholder_accept_terms);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getArguments().getString("terms"));
    }

    @Click({R.id.btnClose, R.id.btnCloseConfirm})
    void onClose() {
        dismiss();
    }

    @Click(R.id.btnBackConfirm)
    void onBack() {
        replaceView(llTerms, llCvv, true);
    }

    private void onNext() {
        cvv = otpView.getText().toString();
        if (cvv.length() != CVV_LENGTH) {
            showAlert(getString(R.string.error_input_digits));
            return;
        }
        replaceView(llCvv, llTerms, false, true);
        padView.clearDigits();
    }

    @CheckedChange(R.id.checkBox)
    void onCheckedChange(CompoundButton v, boolean isChecked) {
        btnActivate.setEnabled(isChecked);
    }

    @Click(R.id.btnActivate)
    void onActivate() {
        showProgressBar();
        reference = getArguments().getString("reference");
        pinType = getArguments().getString("pin_type");
        ApiClient.getInterface()
                .activateCard(new ActivateCardRequest(reference, 205012, cvv))
                .enqueue(new AppCallback<>(getContext(), this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (getContext() == null) return;
        if (response instanceof PinResponse) {
            final PINViewDialog dialog = new PINViewDialog_();
            final Bundle args = new Bundle();
            args.putString("title", getString(R.string.activated));
            args.putString("description", getString(R.string.your_pin, ((PinResponse)response).getData()));
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), Constants.VerifyType.CARD_ACTIVATE.name());
            dismiss();
        } else { // activate success
            if (pinType.equals("viewable")) {
                final HashMap<String, String> params = new HashMap<>();
                params.put("reference", reference);
                ApiClient.getInterface().getPin(params).enqueue(new AppCallback<>(getContext(), this));
            } else {
                hideProgressBar();

//                final Intent intent = new Intent(getContext(), VerifySuccessActivity_.class);
//                intent.putExtra("type", Constants.VerifyType.CARD_ACTIVATE);
//                intent.putExtra("reference", reference);
//                intent.putExtra("pin_type", pinType);
//                startActivity(intent);
//                dismiss();
            }
        }
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();

        if ("This card is not active".equals(message)) {
            // TODO:
        }
    }
}
