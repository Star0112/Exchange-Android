package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.UserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_phone_verification)
public class ForgotPasswordConfirmActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle;

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    private String username;

    @AfterViews
    protected void init() {
        setToolBar(false);
        username = getIntent().getStringExtra("username");

        txtTitle.setText(R.string.forgot_password);
        otpView.setItemCount(Constants.PHONE_VERIFICATION_CODE_LENGTH);
        otpView.setOtpCompletionListener(otp -> onSubmit(null));

        padView.setOnTextChangeListener((text, digits_remaining) -> otpView.setText(text));
    }

    public void onResend(View v) {
        final UserRequest request = new UserRequest();
        request.setUsername(username);
        ApiClient.getInterface()
                .forgotPassword(request)
                .enqueue(new AppCallback<>(this, this));
    }

    public void onSubmit(View v) {
        final String code = otpView.getText().toString();
        if (code.length() != Constants.PHONE_VERIFICATION_CODE_LENGTH) return;

        Intent intent = new Intent(this, ChangePasswordActivity_.class);
        intent.putExtra("username", username);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    @Override
    public void onResponse(BaseResponse response) {
        Toast.makeText(this, R.string.resend_code_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {

    }
}
