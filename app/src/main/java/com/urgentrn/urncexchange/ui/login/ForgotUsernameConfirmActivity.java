package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.PhoneConfirmRequest;
import com.urgentrn.urncexchange.models.request.PhoneRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.PhoneConfirmResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_phone_verification)
public class ForgotUsernameConfirmActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle;

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    private String phone;

    @AfterViews
    protected void init() {
        setToolBar(false);
        phone = getIntent().getStringExtra("phone");

        txtTitle.setText(R.string.forgot_username);
        otpView.setItemCount(Constants.PHONE_VERIFICATION_CODE_LENGTH);
        otpView.setOtpCompletionListener(otp -> onSubmit(null));

        padView.setOnTextChangeListener((text, digits_remaining) -> otpView.setText(text));
    }

    public void onResend(View v) {
        ApiClient.getInterface()
                .forgotUsername(new PhoneRequest(phone))
                .enqueue(new AppCallback<>(this, this));
    }

    public void onSubmit(View v) {
        final String code = otpView.getText().toString();
        if (code.length() != Constants.PHONE_VERIFICATION_CODE_LENGTH) return;
        PhoneConfirmRequest request = new PhoneConfirmRequest();
        request.setPhone(phone);
        request.setCode(code);
        ApiClient.getInterface().forgotUsernameConfirm(request).enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof PhoneConfirmResponse) {
            final String username = ((PhoneConfirmResponse)response).getUsername();
            showAlert(String.format("%s: %s\n%s", getString(R.string.your_username), username, getString(R.string.remember_username)), ((dialog, which) -> {
                Intent intent = new Intent(this, Constants.USE_COMBINED_LOGIN ? LoginActivity_.class : LoginUsernameActivity_.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }));
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
